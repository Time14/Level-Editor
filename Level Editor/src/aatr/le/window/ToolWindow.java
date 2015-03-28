package aatr.le.window;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.Drawable;
import org.lwjgl.opengl.Pbuffer;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ToolWindow extends Application {
	
	//Main Window Dimensions
	public static final int WINDOW_WIDTH = 1280;
	public static final int WINDOW_HEIGHT = 720;
	
	//OpenGL Window Dimensions
	public static final int GL_WINDOW_WIDTH = 400;
	public static final int GL_WINDOW_HEIGHT = 400;
	
	private Stage window;
	
	private Pbuffer pbuffer;
	private ByteBuffer frameBuffer;
	private WritableImage image;
	private PixelReader pr;
	
	public void start(Stage stage) {
		window = stage;
		
		initGL();
		
	}
	
	private void initGL() {
		pbuffer = new Pbuffer(GL_WINDOW_WIDTH, GL_WINDOW_HEIGHT, new PixelFormat, null);
	}
	
}