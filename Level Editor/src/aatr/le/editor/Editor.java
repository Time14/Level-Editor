package aatr.le.editor;

import java.nio.ByteBuffer;
import java.util.HashMap;

import javafx.scene.image.PixelWriter;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.Pbuffer;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import aatr.le.gfx.mesh.Transform;
import aatr.le.gfx.shader.OrthographicShaderProgram;
import aatr.le.panel.ToolWindow;
import aatr.le.panel.edit.EditPanel;
import aatr.le.world.Chunk;
import aatr.le.world.World;
import aatr.le.world.WorldData;
import aatr.le.world.tile.Tile;
import aatr.le.world.tile.TileSet;

public class Editor {
	
	public static final int MAX_BUFFER_SIZE = 10000;
	
	public static final Transform TRANSFORM = new Transform();
	
	public static final float MIN_SCALE = .01f;
	public static final float MAX_SCALE = 100f;
	
	public static final Vector4f BGC = new Vector4f(0, 0, 0, 1);
	
	public static final int OUTPUT_FORMAT = GL11.GL_RGB;
	public static final int OUTPUT_TYPE = GL11.GL_UNSIGNED_BYTE;
	public static final int BYTES_PIXEL = 3;
	
	private ToolWindow app;
	private EditPanel ep;
	
	private ByteBuffer worldBuffer;
	private Pbuffer pBuffer;
	
	private double prevX = 0, prevY = 0;
	
	private float scale = 1f;
	
	private World world;
	
	private EditorTile editorTile;
	
	private static EditMode currentMode = EditMode.TILE;
	
	public Editor(ToolWindow app) {
		this.app = app;
	}
	
	public void init(EditPanel ep) {
		this.ep = ep;
		
		try {
			pBuffer = new Pbuffer(MAX_BUFFER_SIZE, MAX_BUFFER_SIZE,
					new PixelFormat(), null);
			pBuffer.makeCurrent();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		worldBuffer = ByteBuffer.allocateDirect(EditPanel.PANEL_WIDTH
				* EditPanel.PANEL_HEIGHT * BYTES_PIXEL);
		
		world = new World();
		
		initEmptyWorld();
		
		editorTile = new EditorTile(app, ep, this);
		
		updateWorld();
	}
	
	public void updateWorld() {
		
		OrthographicShaderProgram.initProjection(0, EditPanel.PANEL_WIDTH, 0,
				EditPanel.PANEL_HEIGHT);
		
		OrthographicShaderProgram.INSTANCE.sendBoolean("b_scale", true);
		
		OrthographicShaderProgram.INSTANCE.sendMatrix("m_projection",
				OrthographicShaderProgram.getProjection());
		
		GL11.glViewport(0, 0, EditPanel.PANEL_WIDTH, EditPanel.PANEL_HEIGHT);
		
		GL11.glClearColor(BGC.x, BGC.y, BGC.z, BGC.w);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		OrthographicShaderProgram.INSTANCE.sendMatrix("m_view",
				TRANSFORM.getMatrix());
		
		world.draw();
		
		switch (currentMode) {
		default:
			break;
		}
		
		sendPixels();
	}
	
	public void drag(double currentX, double currentY) {
		double dx = (currentX - prevX);
		double dy = (currentY - prevY);
		
		TRANSFORM.translate((float) dx, (float) dy);
		
		prevX = currentX;
		prevY = currentY;
	}
	
	public void setPrevCoords(double x, double y) {
		prevX = x;
		prevY = y;
	}
	
	public void zoom(double deltaY, double mx, double my) {
		
		scale += deltaY / 1000;
		
		Vector2f delta = new Vector2f(
				-(float) (EditPanel.PANEL_WIDTH / 2 - mx),
				-(float) (EditPanel.PANEL_HEIGHT / 2 - my));
		
		if (deltaY < 0)
			delta = new Vector2f();
		
		TRANSFORM.translate(-(delta.x * Math.abs(delta.length()) / 100)
				* (scale / (MAX_SCALE - MIN_SCALE)), -(delta.y
				* Math.abs(delta.length()) / 100)
				* (scale / (MAX_SCALE - MIN_SCALE)));
		
		if (scale < MIN_SCALE)
			scale = MIN_SCALE;
		
		if (scale > MAX_SCALE)
			scale = MAX_SCALE;
		
		OrthographicShaderProgram.INSTANCE.sendFloat("f_scale", scale);
	}
	
	private void sendPixels() {
		worldBuffer.rewind();
		
		GL11.glReadPixels(0, 0, EditPanel.PANEL_WIDTH, EditPanel.PANEL_HEIGHT,
				OUTPUT_FORMAT, OUTPUT_TYPE, worldBuffer);
		
		worldBuffer.rewind();
		
		ep.sendPixels(worldBuffer);
	}
	
	public synchronized World getWorld() {
		return world;
	}
	
	public static final synchronized EditMode getEditMode() {
		return currentMode;
	}
	
	public static final synchronized void setEditMode(EditMode em) {
		currentMode = em;
	}
	
	public synchronized EditorTile getTileEditor() {
		return editorTile;
	}
	
	public void loadWorld(String path) {
		world.loadMap(path);
		
		editorTile.init();
		app.getRightPanel().getTilePanel().init();
	}
	
	public void initEmptyWorld() {
		
		Chunk[][] chunks = new Chunk[][] { { new Chunk().initEmptyChunk(0) } };
		
		Tile[] border = new Tile[] { new Tile(0), new Tile(0), new Tile(0),
				new Tile(0) };
		
		Tile[][] borderChunkTiles = new Tile[Chunk.GRID_DIMENSIONS][Chunk.GRID_DIMENSIONS];
		
		for (int i = 0; i < Chunk.GRID_DIMENSIONS; i++) {
			for (int j = 0; j < Chunk.GRID_DIMENSIONS; j++) {
				borderChunkTiles[i][j] = new Tile(0, i, j);
			}
		}
		
		Chunk borderChunk = new Chunk(borderChunkTiles, TileSet.tileSets.get(0));
		
		world.loadMap(new WorldData((short) 1, (short) 1, (short) 0, chunks,
				border, borderChunk));
	}
	
	public float getScale() {
		return scale;
	}
	
	public final void destroy() {
		world.destroy();
		pBuffer.destroy();
	}
	
	public static enum EditMode {
		TILE, PROPERTIES, ENTITY, EVENT, HEADER;
	}
}