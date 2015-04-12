package aatr.le.world;

import aatr.le.world.tile.Tile;
import aatr.le.world.tile.TileSet;

public class WorldData {
	
	public final short TILESET_ID;
	public final TileSet TILESET;
	
	public final short WIDTH, HEIGHT;
	public final Chunk[][] CHUNKS;
	
	public final Tile[] BORDER;
	public final Chunk BORDER_CHUNK;
	
	public WorldData(short width, short height, short tileSetID, Chunk[][] chunks, Tile[] border, Chunk borderChunk) {
		WIDTH = width;
		HEIGHT = height;
		CHUNKS = chunks;
		BORDER = border;
		BORDER_CHUNK = borderChunk;
		TILESET_ID = tileSetID;
		TILESET = TileSet.tileSets.get(TILESET_ID);
	}
}