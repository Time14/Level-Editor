package aatr.le.gfx.mesh;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import aatr.le.gfx.shader.OrthographicShaderProgram;
import aatr.le.util.Util;

public class Mesh {
	
	private static int currentVAO = -2;
	
	private int vao = -2;
	private int vbo = -2;
	private int ibo = -2;
	
	private int mode = GL_TRIANGLES;
	private int hint = GL_STATIC_DRAW;
	
	private Vertex[] vertices;
	private int[] indices;
	
	public Mesh() {}
	
	public Mesh(Vertex[] vertices) {
		createVAO(vertices, GL_STATIC_DRAW);
	}
	
	public Mesh(Vertex[] vertices, int hint) {
		createVAO(vertices, hint);
	}
	
	public Mesh(Vertex[] vertices, int[] indices) {
		createVAO(vertices, indices, GL_STATIC_DRAW);
	}
	
	public Mesh(Vertex[] vertices, int[] indices, int hint) {
		createVAO(vertices, indices, hint);
	}
	
	public void createVAO(Vertex[] vertices, int hint) {
		this.vertices = vertices;
		this.hint = hint;
		
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		
		vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, Util.toFloatBuffer(vertices), hint);
		
		OrthographicShaderProgram.INSTANCE.initAttributes();
		
		System.out.println("Created new mesh! VAO: " + vao + ", VBO: " + vbo);
	}
	
	public void createVAO(Vertex[] vertices, int[] indices, int hint) {
		this.vertices = vertices;
		this.indices = indices;
		this.hint = hint;
		
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		
		vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		
		glBufferData(GL_ARRAY_BUFFER, Util.toFloatBuffer(vertices), hint);
		
		ibo = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, Util.toIntBuffer(indices), hint);
		
		OrthographicShaderProgram.INSTANCE.initAttributes();
		
		System.out.println("Created new mesh! VAO: " + vao + ", VBO: " + vbo + ", IBO: " + ibo);
	}
	
	public void changeVBOData(long offset, FloatBuffer data) {
		if(hint == GL_STATIC_DRAW)
			throw new IllegalStateException("Cannot change data of a GL_STATIC_DRAW VBO");
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferSubData(GL_ARRAY_BUFFER, offset, data);
	}
	
	public void changeIBOData(long offset, IntBuffer data) {
		if(hint == GL_STATIC_DRAW)
			throw new IllegalStateException("Cannot change data of a GL_STATIC_DRAW VBO");
		if(indices == null)
			throw new IllegalStateException("This mesh does not contain any IBOs");
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, offset, data);
	}
	
	public void draw() {
		
		if(currentVAO != vao) {
			glBindVertexArray(vao);
			currentVAO = vao;
		}
		
		if(indices == null) {
			glDrawArrays(mode, 0, vertices.length);
		} else {
			
			glDrawElements(mode, indices.length, GL_UNSIGNED_INT, 0);
		}
	}
	
	public Mesh setMode(int mode) {
		this.mode = mode;
		return this;
	}
	
	public int getHint() {
		return hint;
	}
	
	public void destroy() {
		glDeleteVertexArrays(vao);
		glDeleteBuffers(vbo);
		
		System.out.print("Destroyed mesh! VAO: " + vao);
		System.out.print(", VBO: " + vbo);
		
		if(indices != null) {
			glDeleteBuffers(ibo);
			System.out.print(",IBO: " + ibo);
		}
		System.out.println();
	}
}