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
	public static final int DEFAULT_WINDOW_WIDTH = 1280;
	public static final int DEFAULT_WINDOW_HEIGHT = 720;
	
	// Panels
	LeftPanel lpanel;
	RightPanel rpanel;
	TopPanel tpanel;
	CenterPanel cpanel;
	
	
	private Stage window;
	private Scene scene;

	private Renderer renderer;
	private Thread renderThread;
	
	private BorderPane bp;
	
	public void start(Stage stage) {
		window = stage;
		
		initialize();
	}

	public synchronized void sendPixels(ByteBuffer pixels) {
		cpanel.sendPixels(pixels);
	}

	private void initialize() {
		
		bp = new BorderPane();
		
		lpanel = new LeftPanel(this);
		bp.setLeft(lpanel.getBox());
		rpanel = new RightPanel(this);
		bp.setRight(rpanel.getBox());
		tpanel = new TopPanel(this, window);
		bp.setTop(tpanel.getBox());
		cpanel = new CenterPanel(this, bp);
		bp.setCenter(cpanel.getBox());
		
		scene = new Scene(bp, DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
		
		window.setScene(scene);
		
		renderer = new Renderer(this);
		
		renderThread = new Thread(renderer, "Renderer");

		renderThread.start();
		
		window.show();
	}
	
	public synchronized Renderer getRenderer() {
		return renderer;
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