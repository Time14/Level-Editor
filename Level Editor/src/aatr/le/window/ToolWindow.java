package aatr.le.window;

import java.nio.ByteBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Drawable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.Pbuffer;
import org.lwjgl.opengl.PixelFormat;

import aatr.le.gfx.Renderer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ToolWindow extends Application {

	// Main Window Dimensions
	public static final int WINDOW_WIDTH = 1280;
	public static final int WINDOW_HEIGHT = 720;

	// OpenGL Window Dimensions
	public static final int GL_WINDOW_WIDTH = 400;
	public static final int GL_WINDOW_HEIGHT = 400;

	private Stage window;

	private ImageView vimage;
	private WritableImage wimage;
	private PixelWriter pwriter;

	private Renderer renderer;
	private Thread renderThread;

	public void start(Stage stage) {
		window = stage;

		initialize();

		BorderPane borderPane = new BorderPane();

		borderPane.setCenter(vimage);

		Scene scene = new Scene(borderPane, WINDOW_WIDTH, WINDOW_HEIGHT);

		window.setScene(scene);

		window.show();
	}

	public synchronized void sendPixels(ByteBuffer pixels) {
		pwriter.setPixels(0, 0, GL_WINDOW_WIDTH, GL_WINDOW_HEIGHT,
				javafx.scene.image.PixelFormat.getByteRgbInstance(), pixels,
				GL_WINDOW_WIDTH * Renderer.BYTES_PIXEL);
		// System.out.println(pixels.get(1));
	}

	private void initialize() {
		wimage = new WritableImage(GL_WINDOW_WIDTH, GL_WINDOW_HEIGHT);
		vimage = new ImageView(wimage);
		pwriter = wimage.getPixelWriter();

		renderer = new Renderer(this);

		renderThread = new Thread(renderer, "Renderer");

		renderThread.start();
	}

	public void stop() {
		try {
			window.close();
			renderer.stop();
			renderThread.join();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}