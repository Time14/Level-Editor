package aatr.le.world;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import aatr.le.gfx.mesh.Mesh;
import aatr.le.gfx.mesh.Vertex;
import aatr.le.gfx.renderer.Renderer;
import aatr.le.util.Util;
import aatr.le.world.tile.Tile;
import aatr.le.world.tile.TileSet;

public class Chunk {

	public static final int GRID_DIMENSIONS = 16;
	public static final int PIXEL_WIDTH = GRID_DIMENSIONS * World.GRID_SIZE;
	public static final int PIXEL_HEIGHT = GRID_DIMENSIONS * World.GRID_SIZE;
	
	private TileSet tileSet;
	
	private Tile[][] tiles = new Tile[GRID_DIMENSIONS][GRID_DIMENSIONS];
	
	private Vertex[] verts;
	
	private Renderer renderer;
	
	//#EdvardQuickFix!
	private static Tile[][] convertTiles(Tile[] tiles) {
		Tile[][] tiles2D = new Tile[GRID_DIMENSIONS][GRID_DIMENSIONS];
		
		for (int i = 0; i < GRID_DIMENSIONS; i++)
			for (int j = 0; j < GRID_DIMENSIONS; j++)
				tiles2D[i][j] = tiles[i + j * GRID_DIMENSIONS];
		
		return tiles2D;
	}

	public Chunk() {}
	
	public Chunk(Tile[] tiles, TileSet tileSet) {
		this(convertTiles(tiles), tileSet);
	}


	public Chunk(Tile[][] tiles, TileSet tileSet) {
		this.tiles = tiles;
		this.tileSet = tileSet;
		init();
	}
	
	public Chunk initEmptyChunk(int ts) {
		
		tileSet = TileSet.tileSets.get(ts);
		
		tiles = new Tile[GRID_DIMENSIONS][GRID_DIMENSIONS];
		
		for(int x = 0; x < GRID_DIMENSIONS; x++) {
			for(int y = 0; y < GRID_DIMENSIONS; y++) {
				tiles[x][y] = new Tile(0, x, y);
			}
		}
		
		init();
		
		return this;
	}

	public void init() {
		verts = new Vertex[((int) Math.pow(GRID_DIMENSIONS, 2)) * 4];

		for (int x = 0; x < tiles.length; x++) {

			for (int y = 0; y < tiles[x].length; y++) {

				Vertex[] vertHolder = tiles[x][y].getVertices(tileSet, x, y);
				
				for (int i = 0; i < vertHolder.length; i++) {
					verts[(int) (x * (vertHolder.length) + i + GRID_DIMENSIONS
							* vertHolder.length * y)] = vertHolder[i];
				}
			}
		}

		renderer = new Renderer(new Mesh(verts, GL15.GL_DYNAMIC_DRAW).setMode(GL11.GL_QUADS), tiles[0][0]
				.getTileSet().getTexture());
	}
	
	public void setTile(Tile tile) {
		
		int x = tile.getX() % GRID_DIMENSIONS;
		int y = tile.getY() % GRID_DIMENSIONS;
		
		tiles[x][y] = tile.setTileSet(tileSet);
		
		renderer.getMesh().changeVBOData(Vertex.SIZE * Vertex.LENGTH * (y * GRID_DIMENSIONS + x), Util.toFloatBuffer(tile.getVertices()));
	}
	
	public Tile getTile(int x, int y) {
		return tiles[x][y];
	}
	
	public Tile[][] getTiles() {
		return tiles;
	}
	
	public void destroy() {
		for (Tile[] ts : tiles)
			for (Tile t : ts)
				t.destroy();
		renderer.destroy();
	}

	public int getActiveTiles() {
		int i = 0;
		for (Tile[] ts : tiles)
			for (Tile t : ts)
				if (t != null)
					i++;

		return i;
	}
	
	public TileSet getTileSet() {
		return tileSet;
	}

	public Chunk draw() {
		renderer.draw();
		return this;
	}

	public Renderer getEntity() {
		return renderer;
	}
}
