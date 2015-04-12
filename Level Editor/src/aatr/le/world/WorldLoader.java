package aatr.le.world;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

import aatr.le.util.Util;
import aatr.le.world.tile.Tile;
import aatr.le.world.tile.TileSet;

public final class WorldLoader {
	
	public static final int BYTES_HEADER = 14;
	
	/* ChunksX		2 Bytes
	 * ChunksY		2 Bytes
	 * Tileset		2 Bytes
	 * Border		4*2 Bytes
	 * Chunks		16^2*2*x bytes
	 * 
	 */

	public static final WorldData loadMapFromFile(String path) {
		try {

			File mapFile = new File(path);

			ByteBuffer mapData = loadFile(mapFile);

			// Loading width and height
			short width = mapData.getShort();
			short height = mapData.getShort();

			// Loading tileset
			short ts = mapData.getShort();

			// Loading border chunk
			short id1 = mapData.getShort();
			short id2 = mapData.getShort();
			short id3 = mapData.getShort();
			short id4 = mapData.getShort();
			
			Tile[][] borderChunkTiles = new Tile[Chunk.GRID_DIMENSIONS][Chunk.GRID_DIMENSIONS];
			for (int x = 0; x < Chunk.GRID_DIMENSIONS; x += 2) {
				for (int y = 0; y < Chunk.GRID_DIMENSIONS; y += 2) {
					borderChunkTiles[x][y] = new Tile(id1);
					borderChunkTiles[x + 1][y] = new Tile(id2);
					borderChunkTiles[x + 1][y + 1] = new Tile(id4);
					borderChunkTiles[x][y + 1] = new Tile(id3);
				}
			}
			
			Tile[] border = new Tile[] { new Tile(id1), new Tile(id2),
					new Tile(id3), new Tile(id4) };

			// Loading chunks
			Chunk[][] map = new Chunk[width][height];
			
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					
					Tile[][] chunkData = new Tile[Chunk.GRID_DIMENSIONS][Chunk.GRID_DIMENSIONS];
					
					boolean repeat = false;
					short repeatAmt = 0;
					short repeatCount = 0;
					short repeatTd = 0;
					for(int i = 0; i < chunkData.length; i++) {
						for(int j = 0; j < chunkData[i].length; j++) {
							short td = 0;
							if(!repeat) {
								td = mapData.getShort();
								
								if(td == -1) {
									repeat = true;
									repeatCount = 0;
									repeatAmt = mapData.getShort();
									repeatTd = mapData.getShort();
									td = repeatTd;
								}
							} else {
								if(++repeatCount == repeatAmt) {
									td = mapData.getShort();
									repeat = false;
									if(td == -1) {
										repeat = true;
										repeatCount = 0;
										repeatAmt = mapData.getShort();
										repeatTd = mapData.getShort();
										td = repeatTd;
									}
								}
							}
							
							if(repeat)
								td = repeatTd;
							
							chunkData[j][i] = new Tile(td, x * Chunk.GRID_DIMENSIONS + i, y * Chunk.GRID_DIMENSIONS + j);
						}
					}
					
					map[x][y] = new Chunk(chunkData, TileSet.tileSets.get(ts));
				}
			}

			return new WorldData(width, height, ts, map, border, new Chunk(
					borderChunkTiles, TileSet.tileSets.get(ts)));

		} catch (IOException e) {
			System.err.println("Error 404 file not found \"" + path + "\"");
			e.printStackTrace();
			return null;
		}
	}

	private static final ByteBuffer loadFile(File mapFile) throws IOException {

		RandomAccessFile raf = new RandomAccessFile(mapFile, "r");
		
		ByteBuffer buffer = Util.createByteBuffer((int) raf.length()).order(
				ByteOrder.LITTLE_ENDIAN);

		FileChannel fc = raf.getChannel();
		fc.read(buffer);
		raf.close();
		buffer.flip();

		return buffer;
	}

}
