package aatr.le.world;

import aatr.le.editor.Editor;
import aatr.le.world.player.Player;
import aatr.le.world.tile.Tile;
import aatr.le.world.tile.TileProperty;
import aatr.le.world.tile.TileSet;

public class World {
	public static final int GRID_SIZE = 16;
	public static final int MAX_DRAWDISTANCE = 1;
	public static final int MAX_WIDTH = 9999;
	public static final int MAX_HEIGHT = 9999;

	private Chunk[][] map;
	
	private Tile[] border;
	
	private Chunk borderChunk;
	
	private WorldData worldData;
	
	// public World() {
	// initTestChunk();
	// }
	
	public World() {}
	
	public World(String mapPath) {
		loadMap(mapPath);
	}

	// public void initTestChunk() {
	//
	// int ts = 1;
	// int dim = 1;
	// map = new Chunk[dim][dim];
	//
	// for (int cx = 0; cx < dim; cx++) {
	// for (int cy = 0; cy < dim; cy++) {
	// Tile[][] tileArray = new
	// Tile[Chunk.GRID_DIMENSIONS][Chunk.GRID_DIMENSIONS];
	// for (int x = 0; x < Chunk.GRID_DIMENSIONS; x++) {
	// for (int y = 0; y < Chunk.GRID_DIMENSIONS; y++) {
	// tileArray[x][y] = new Tile(ts, x + cx
	// * Chunk.GRID_DIMENSIONS
	// + (y + cy * Chunk.GRID_DIMENSIONS) * 40, x, y);
	// }
	// }
	// map[cx][cy] = new Chunk(tileArray);
	// map[cx][cy].getEntity().translate(
	// cx * GRID_SIZE * Chunk.GRID_DIMENSIONS,
	// cy * GRID_SIZE * Chunk.GRID_DIMENSIONS);
	// }
	// }
	// }

	/*
	 * All .map files have 3 shorts in the header short width = The width of the
	 * map short height = THe height of the map short tileSet = The id number
	 * reffering to the tileset to be used
	 */
	public World loadMap(String mapPath) {
		if (mapPath == null)
			return this;
		
		return loadMap(WorldLoader.loadMapFromFile(mapPath));
	}
	
	public World loadMap(WorldData worldData) {
		
		map = worldData.CHUNKS;
		borderChunk = worldData.BORDER_CHUNK;
		
		border = worldData.BORDER;
		
		this.worldData = worldData;
		
		return this;
	}

	public void setTile(Tile tile) {
		
		int cx = (int)Math.floor(((float) tile.getX() / Chunk.GRID_DIMENSIONS));
		int cy = (int)Math.floor(((float) tile.getY() / Chunk.GRID_DIMENSIONS));
		
		if(cx > map.length - 1 || cy > map[0].length - 1 || cx < 0 || cy < 0)
			return;
		
		tile.setX(tile.getX() - cx * Chunk.GRID_DIMENSIONS);
		tile.setY(tile.getY() - cy * Chunk.GRID_DIMENSIONS);
		
		map[cx][cy].setTile(tile);
	}

	public Tile getTile(int x, int y) {

		if (x < 0 || y < 0) {
			return new Tile(0).addProperty(TileProperty.SOLID);
		}

		int cx = ((int) (x - x % Chunk.GRID_DIMENSIONS) / Chunk.GRID_DIMENSIONS);
		int cy = ((int) (y - y % Chunk.GRID_DIMENSIONS) / Chunk.GRID_DIMENSIONS);

		x = (x + Chunk.GRID_DIMENSIONS) % Chunk.GRID_DIMENSIONS;
		y = (y + Chunk.GRID_DIMENSIONS) % Chunk.GRID_DIMENSIONS;
		if (0 > cx || cx > map.length - 1 || 0 > cy || cy > map[cx].length - 1)
			return new Tile(0).addProperty(TileProperty.SOLID);
		return map[cx][cy].getTile(x, y);
	}
	
	public World setDimensions(int amtX, int amtY) {
		return addChunks(amtX - map.length, amtY - map[0].length);
	}
	
	public World addChunks(int amtX, int amtY) {
		
		Chunk[][] chunks = new Chunk[map.length + amtX][map[0].length + amtY];
		
		for(int x = 0; x < map.length; x++) {
			for(int y = 0; y < map[x].length; y++) {
				chunks[x][y] = map[x][y];
			}
		}
		
		for(int x = 0; x < chunks.length; x++) {
			for(int y = 0; y < chunks[x].length; y++) {
				if(chunks[x][y] == null)
					chunks[x][y] = new Chunk().initEmptyChunk(worldData.TILESET_ID);
			}
		}
		
		map = chunks;
		
		return this;
	}
	
	public TileSet getTileSet() {
		return map[0][0].getTileSet();
	}
	
	public WorldData getWorldData() {
		
		worldData = new WorldData((short)map.length, (short)map[0].length, (short)map[0][0].getTileSet().getID(), map, border, borderChunk);
		
		return worldData;
	}
	
	public World setBorderTile(int tile, int index) {
		if(index > 3)
			throw new IllegalArgumentException("Index must be equal to 3 or less");
		
		border[index] = new Tile(tile, index % 2, Math.floorDiv(index, 2));
		
		genBorderChunk();
		
		return this;
	}
	
	public Tile getBorderTile(int index) {
		if(index > 3)
			throw new IllegalArgumentException("Index must be equal to 3 or less");
		
		return border[index];
	}
	
	public Chunk getBorderChunk() {
		return borderChunk;
	}
	
	public void draw() {
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[i].length; j++) {
				map[i][j].getEntity().setPosition(i * Chunk.PIXEL_WIDTH, j * Chunk.PIXEL_HEIGHT);
				map[i][j].draw();
			}
		}
	}
	
	public void genBorderChunk() {
		Tile[][] borderChunkTiles = new Tile[Chunk.GRID_DIMENSIONS][Chunk.GRID_DIMENSIONS];
		for (int x = 0; x < Chunk.GRID_DIMENSIONS; x += 2) {
			for (int y = 0; y < Chunk.GRID_DIMENSIONS; y += 2) {
				borderChunkTiles[x][y] = new Tile(border[0].getID(), x, y);
				borderChunkTiles[x + 1][y] = new Tile(border[1].getID(), x + 1, y);
				borderChunkTiles[x][y + 1] = new Tile(border[2].getID(), x, y + 1);
				borderChunkTiles[x + 1][y + 1] = new Tile(border[3].getID(), x + 1, y + 1);
			}
		}
		
		borderChunk = new Chunk(borderChunkTiles, worldData.TILESET);
	}
	
	public int getWidth() {
		return map.length;
	}
	
	public int getHeight() {
		return map[0].length;
	}

	public void destroy() {
		for (Chunk[] cs : map)
			for (Chunk c : cs)
				c.destroy();
	}

}
