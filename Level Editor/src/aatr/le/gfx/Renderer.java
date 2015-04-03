package aatr.le.gfx;

import java.nio.ByteBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.Pbuffer;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import aatr.le.window.CenterPanel;
import aatr.le.window.ToolWindow;

public class Renderer implements Runnable {
	
	public static final float MIN_SCALE = .01f;
	public static final float MAX_SCALE = 100f;
	
	public static final Vector4f BGC = new Vector4f(0, 0, 0, 1);

	public static final int OUTPUT_FORMAT = GL11.GL_RGB;
	public static final int OUTPUT_TYPE = GL11.GL_UNSIGNED_BYTE;
	public static final int BYTES_PIXEL = 3;

	private volatile boolean running = true;

	private ByteBuffer frameBuffer;
	private Pbuffer pbuffer;

	private ToolWindow app;
	
	public Vector2f translation = new Vector2f();
	public Vector2f scale = new Vector2f(1, 1);
	public float rotation = 0;
	
	public Renderer(ToolWindow app) {
		this.app = app;
	}

	public void run() {

		init();

		while (running)
			loop();

		destroy();
	}

	private void loop() {
		GL11.glClearColor(BGC.x, BGC.y, BGC.z, BGC.w);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

		render();

		sendPixels();
	}

	private void render() {
		GL11.glLoadIdentity();
		GL11.glRotatef(rotation, 0, 0, 1);
		GL11.glTranslatef(translation.x, translation.y, 0);
		GL11.glScalef(scale.x, scale.y, 0);
		
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glVertex2f(0, 0);
			GL11.glVertex2f(100, 0);
			GL11.glVertex2f(100, 100);
			GL11.glVertex2f(0, 100);
		}
		GL11.glEnd();
		
	}
	
	private void sendPixels() {
		frameBuffer.rewind();
		
		GL11.glReadPixels(0, 0, CenterPanel.PANEL_WIDTH,
				CenterPanel.PANEL_HEIGHT, OUTPUT_FORMAT, OUTPUT_TYPE,
				frameBuffer);
		
		frameBuffer.rewind();
		
		app.sendPixels(frameBuffer);
	}

	private void init() {
		try {

			/*--OpenGL Initialization--*/

			// Initializing PBuffer
			pbuffer = new Pbuffer(CenterPanel.PANEL_WIDTH,
					CenterPanel.PANEL_HEIGHT, new PixelFormat(), null, null);
			pbuffer.makeCurrent();

			// Initializing OpenGL
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0, CenterPanel.PANEL_WIDTH, 0,
					CenterPanel.PANEL_HEIGHT, 1, -1);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();

			/*--End--*/

			frameBuffer = ByteBuffer.allocateDirect(CenterPanel.PANEL_WIDTH
					* CenterPanel.PANEL_HEIGHT * BYTES_PIXEL);

		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

	public final synchronized void stop() {
		running = false;
	}

	private final void destroy() {
		try {
			pbuffer.releaseContext();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
}