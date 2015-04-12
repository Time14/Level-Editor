package aatr.le.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.util.vector.Matrix4f;

import aatr.le.debug.Debug;
import aatr.le.gfx.mesh.Vertex;
import aatr.le.world.Chunk;
import aatr.le.world.tile.Tile;

public final class Util {

	
	public static final FloatBuffer toFloatBuffer(float[] data) {
		FloatBuffer buffer = createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	public static final FloatBuffer toFloatBuffer(Vertex[] data) {
		FloatBuffer buffer = createFloatBuffer(data.length * Vertex.LENGTH);
		for(Vertex v : data) {
//			System.out.println(v.getData()[0]);
			buffer.put(v.getData());
		}
		
		buffer.flip();
		return buffer;
	}
	
	public static final FloatBuffer toFloatBuffer(Matrix4f matrix) {
		FloatBuffer buffer = createFloatBuffer(16);
		matrix.store(buffer);
		buffer.flip();
		return buffer;
	}
	
	public static final Vertex[] toArray(Vertex[][] vertices) {
		Vertex[] data = new Vertex[vertices.length * vertices[0].length];
		
		for(int i = 0; i < vertices.length; i++) {
			for(int j = 0; j < vertices[i].length; j++) {
				data[i * vertices[i].length + j] = vertices[i][j];
			}
		}
		
		return data;
	}
	
	public static final Tile[][] toMultiArray(Tile[] tiles) {
		if(tiles.length != Math.pow(Chunk.GRID_DIMENSIONS, 2))
			throw new IllegalArgumentException("Tile array length must be the squaure of " + Chunk.GRID_DIMENSIONS);
		
		Tile[][] data = new Tile[Chunk.GRID_DIMENSIONS][Chunk.GRID_DIMENSIONS];
		
		for(int i = 0; i < Chunk.GRID_DIMENSIONS; i++) {
			for(int j = 0; j < Chunk.GRID_DIMENSIONS; j++) {
				data[i][j] = tiles[i + j * Chunk.GRID_DIMENSIONS];
			}
		}
		
		return data;
	}
	
	public static final Tile[] toArray(Tile[][] tiles) {
		if(tiles.length != Chunk.GRID_DIMENSIONS || tiles[0].length != Chunk.GRID_DIMENSIONS)
			throw new IllegalArgumentException("Tile array must have the dimensions of " + Chunk.GRID_DIMENSIONS + "x" + Chunk.GRID_DIMENSIONS);
		
		Tile[] data = new Tile[(int)Math.pow(Chunk.GRID_DIMENSIONS, 2)];
		
		for(int i = 0; i < Chunk.GRID_DIMENSIONS; i++) {
			for(int j = 0; j < Chunk.GRID_DIMENSIONS; j++) {
				data[i + j * Chunk.GRID_DIMENSIONS] = tiles[i][j];
			}
		}
		
		return data;
	}
	
	public static final Tile[] toArray(ArrayList<Tile> al) {
		Tile[] data = new Tile[]{};
		
		return al.toArray(data);
	}
	
	public static final IntBuffer toIntBuffer(int[] data) {
		IntBuffer buffer = createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	public static final FloatBuffer createFloatBuffer(int size) {
		return createByteBuffer(size << 2).asFloatBuffer();
	}
	
	public static final IntBuffer createIntBuffer(int size) {
		return createByteBuffer(size << 2).asIntBuffer();
	}
	
	public static final ByteBuffer createByteBuffer(int size) {
		return ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder());
	}
}