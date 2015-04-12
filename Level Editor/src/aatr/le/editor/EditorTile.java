package aatr.le.editor;

import java.nio.ByteBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.Pbuffer;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Matrix4f;

import aatr.le.debug.Debug;
import aatr.le.gfx.mesh.Mesh;
import aatr.le.gfx.mesh.Vertex;
import aatr.le.gfx.renderer.QuadRenderer;
import aatr.le.gfx.renderer.Renderer;
import aatr.le.gfx.shader.OrthographicShaderProgram;
import aatr.le.panel.ToolWindow;
import aatr.le.panel.edit.EditPanel;
import aatr.le.util.Util;
import aatr.le.world.World;
import aatr.le.world.tile.Tile;
import aatr.le.world.tile.TileSet;

public class EditorTile {
	
	public static final int BORDER_TILE_SIZE = World.GRID_SIZE << 1;
	public static final int BORDER_SIZE = BORDER_TILE_SIZE << 1;
	
	private ToolWindow app;
	private EditPanel ep;
	private Editor editor;
	private World world;
	
	private int selectedTile = 0;
	
	private QuadRenderer tileSetRenderer;
	private Renderer borderRenderer;
	
	private ByteBuffer tileSetBuffer;
	private ByteBuffer borderBuffer;
	
	private int tileSetWidth;
	private int tileSetHeight;
	
	public EditorTile(ToolWindow app, EditPanel ep, Editor editor) {
		this.app = app;
		this.ep = ep;
		this.editor = editor;
		world = editor.getWorld();
		init();
	}
	
	public void updateBorder() {
		
		for(int i = 0; i < 4; i++) {
			Vertex[] vertices = world.getBorderTile(i).getVertices(world.getTileSet(), i % 2, Math.floorDiv(i, 2), BORDER_TILE_SIZE);
			
			Debug.log(vertices[0].getData()[0], vertices[0].getData()[1], vertices[0].getData()[2], vertices[0].getData()[3]);
			
			borderRenderer.updateVertices(i * 4, vertices);
		}
		
		OrthographicShaderProgram.initProjection(0, BORDER_SIZE, 0, BORDER_SIZE);
		
		OrthographicShaderProgram.INSTANCE.sendMatrix("m_projection",
				OrthographicShaderProgram.getProjection());
		
		OrthographicShaderProgram.INSTANCE.sendBoolean("b_scale", false);
		
		GL11.glViewport(0, 0, BORDER_SIZE, BORDER_SIZE);
		
		GL11.glClearColor(0, 0, 0, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		OrthographicShaderProgram.INSTANCE.sendMatrix("m_view", (Matrix4f)new Matrix4f().setIdentity());
		
		borderRenderer.draw();
		
		borderBuffer.rewind();
		
		GL11.glReadPixels(0, 0, BORDER_SIZE, BORDER_SIZE, Editor.OUTPUT_FORMAT, Editor.OUTPUT_TYPE, borderBuffer);
		
		borderBuffer.rewind();
		
		app.getRightPanel().getTilePanel().sendBorder(borderBuffer);
	}
	
	public void updateTileSet() {
		
		OrthographicShaderProgram.initProjection(0, tileSetWidth, 0, tileSetHeight);
		
		OrthographicShaderProgram.INSTANCE.sendMatrix("m_projection",
				OrthographicShaderProgram.getProjection());
		
		OrthographicShaderProgram.INSTANCE.sendBoolean("b_scale", false);
				
		GL11.glViewport(0, 0, tileSetWidth, tileSetHeight);
		
		GL11.glClearColor(0, 0, 0, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		OrthographicShaderProgram.INSTANCE.sendMatrix("m_view",
				(Matrix4f) new Matrix4f().setIdentity());
		
		tileSetRenderer.draw();
		
		tileSetBuffer.rewind();
		
		GL11.glReadPixels(0, 0, tileSetWidth, tileSetHeight,
				Editor.OUTPUT_FORMAT, Editor.OUTPUT_TYPE, tileSetBuffer);
		
		tileSetBuffer.rewind();
		
		app.getRightPanel().getTilePanel().sendPixels(tileSetBuffer);
	}
	
	public void init() {
		tileSetWidth = world.getTileSet().getTilesX() * World.GRID_SIZE;
		
		tileSetHeight = world.getTileSet().getTilesY() * World.GRID_SIZE;
		
		int tw = world.getTileSet().getTexture().getWidth();
		int th = world.getTileSet().getTexture().getHeight();
		
		tileSetRenderer = new QuadRenderer(0, 0, tileSetWidth, tileSetHeight,
				1, 1, world.getTileSet().getTexture());
		
		if (tileSetBuffer != null)
			tileSetBuffer.clear();
		
		tileSetBuffer = ByteBuffer.allocateDirect(tileSetWidth * tileSetHeight
				* Editor.BYTES_PIXEL);
		
		borderRenderer = new Renderer(new Mesh(Util.toArray(new Vertex[][]{
				world.getBorderTile(0).getVertices(world.getTileSet(), 0, 0, BORDER_TILE_SIZE),
				world.getBorderTile(1).getVertices(world.getTileSet(), 1, 0, BORDER_TILE_SIZE),
				world.getBorderTile(2).getVertices(world.getTileSet(), 0, 1, BORDER_TILE_SIZE),
				world.getBorderTile(3).getVertices(world.getTileSet(), 1, 1, BORDER_TILE_SIZE)
		}), GL15.GL_DYNAMIC_DRAW).setMode(GL11.GL_QUADS), world.getTileSet().getTexture());
		
		borderBuffer = Util.createByteBuffer((int)Math.pow(BORDER_SIZE, 2) * Editor.BYTES_PIXEL);
		
		
	}
	
	public void paintBorder(int index) {
		world.setBorderTile(selectedTile, index);
		
		updateBorder();
	}
	
	public void paint(double mx, double my) {
		
		int tx, ty;
		
		tx = (int) Math.floor((mx - Editor.TRANSFORM.position.x)
				/ (Tile.HEIGHT * editor.getScale()));
		ty = (int) Math.floor((my - Editor.TRANSFORM.position.y)
				/ (Tile.HEIGHT * editor.getScale()));
		
		world.setTile(new Tile(selectedTile, tx, ty));
		
		app.getFileHandler().setEdited();
	}
	
	public int getSelectedTile() {
		return selectedTile;
	}
	
	public int getTileSetWidth() {
		return tileSetWidth;
	}
	
	public int getTileSetHeight() {
		return tileSetHeight;
	}
	
	public void selectTile(int i) {
		selectedTile = i;
	}
	
	public void destroyTileSetRenderer() {
		tileSetRenderer.destroy();
	}
	
}