package aatr.le.gfx.renderer;

import org.lwjgl.opengl.GL11;

import aatr.le.gfx.mesh.Mesh;
import aatr.le.gfx.mesh.Transform;
import aatr.le.gfx.mesh.Vertex;
import aatr.le.gfx.texture.Texture;
import aatr.le.gfx.texture.TextureLibrary;

public class QuadRenderer extends Renderer {
	
	public QuadRenderer(float x, float y, float width, float height, Texture texture) {
		super(new Mesh(new Vertex[]{
				new Vertex(0, 0, 0, 0),
				new Vertex(width, 0, 1, 0),
				new Vertex(width, height, 1, 1),
				new Vertex(0, height, 0, 1)
		}).setMode(GL11.GL_QUADS), texture, new Transform(x, y));
	}
	
	public QuadRenderer(float x, float y, float width, float height, float s, float t, Texture texture) {
		super(new Mesh(new Vertex[]{
				new Vertex(0, 0, 0, 0),
				new Vertex(width, 0, s, 0),
				new Vertex(width, height, s, t),
				new Vertex(0, height, 0, t)
		}).setMode(GL11.GL_QUADS), texture, new Transform(x, y));
	}
	
}