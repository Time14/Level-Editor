package aatr.le.gfx;

import java.nio.ByteBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.Pbuffer;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Vector4f;

import aatr.le.window.ToolWindow;

public class Renderer implements Runnable {

	public static final Vector4f BGC = new Vector4f(0, 0, 0, 1);

	public static final int OUTPUT_FORMAT = GL11.GL_RGB;
	public static final int OUTPUT_TYPE = GL11.GL_UNSIGNED_BYTE;
	public static final int BYTES_PIXEL = 3;

	private volatile boolean running = true;

	private ByteBuffer frameBuffer;
	private Pbuffer pbuffer;

	private ToolWindow app;

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

		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glColor3f(1, 0, 0);
			GL11.glVertex2f(0, 0);
			GL11.glVertex2f(200, 0);
			GL11.glVertex2f(200, 200);
			GL11.glVertex2f(0, 200);
		}
		GL11.glEnd();
	}

	private void sendPixels() {
		frameBuffer.rewind();

		GL11.glReadPixels(0, 0, ToolWindow.GL_WINDOW_WIDTH,
				ToolWindow.GL_WINDOW_HEIGHT, OUTPUT_FORMAT, OUTPUT_TYPE,
				frameBuffer);

		frameBuffer.rewind();

		app.sendPixels(frameBuffer);
	}

	private void init() {
		try {

			/*--OpenGL Initialization--*/

			// Initializing PBuffer
			pbuffer = new Pbuffer(ToolWindow.GL_WINDOW_WIDTH,
					ToolWindow.GL_WINDOW_HEIGHT, new PixelFormat(), null, null);
			pbuffer.makeCurrent();

			// Initializing OpenGL
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0, ToolWindow.GL_WINDOW_WIDTH, 0,
					ToolWindow.GL_WINDOW_HEIGHT, 1, -1);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();

			/*--End--*/

			frameBuffer = ByteBuffer.allocateDirect(ToolWindow.GL_WINDOW_WIDTH
					* ToolWindow.GL_WINDOW_HEIGHT * BYTES_PIXEL);

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