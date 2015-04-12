package aatr.le.gfx.mesh;

import org.lwjgl.util.vector.Vector2f;

public class Vertex {
	
	public static final int LENGTH = 4;
	public static final int SIZE = LENGTH * Float.BYTES;
	
	private Vector2f pos;
	
	private Vector2f texCoords;
	
	public Vertex() {
		this(new Vector2f());
	}
	
	public Vertex(float x, float y) {
		this(x, y, 0, 0);
	}
	
	public Vertex(float x, float y, float s, float t) {
		this(new Vector2f(x, y), new Vector2f(s, t));
	}
	
	public Vertex(float x, float y, Vector2f texCoords) {
		this(new Vector2f(x, y), texCoords);
	}
	
	public Vertex(Vector2f pos) {
		this(pos, new Vector2f());
	}
	
	public Vertex(Vector2f pos, Vector2f texCoords) {
		this.pos = pos;
		this.texCoords = texCoords;
	}
	
	public int[] getComponents() {
		return new int[]{2, 2};
	}
	
	public float[] getData() {
		return new float[]{pos.x, pos.y, texCoords.x, texCoords.y};
	}
	
	public int getSize() {
		return SIZE;
	}
	
	public int getLength() {
		return LENGTH;
	}
	
}