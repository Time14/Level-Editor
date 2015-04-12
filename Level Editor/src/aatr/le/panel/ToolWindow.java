package aatr.le.panel;

import java.nio.ByteBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Drawable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.Pbuffer;
import org.lwjgl.opengl.PixelFormat;

import aatr.le.editor.Editor;
import aatr.le.io.FileHandler;
import aatr.le.panel.edit.EditPanel;
import aatr.le.panel.left.LeftPanel;
import aatr.le.panel.right.RightPanel;
import aatr.le.panel.top.TopPanel;
import javafx.application.Application;
import javafx.geometry.Insets;
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
	
	public static final String TITLE = "Level Editor v.1.0";
	
	// Main Window Dimensions
	public static final int DEFAULT_WINDOW_WIDTH = 1280;
	public static final int DEFAULT_WINDOW_HEIGHT = 720;
	
	// Panels
	private LeftPanel lpanel;
	private RightPanel rpanel;
	private TopPanel tpanel;
	private EditPanel epanel;

	private Stage window;
	private Scene scene;

	private BorderPane bp;

	private FileHandler fh;
	
	private Editor editor;

	public void start(Stage stage) {
		window = stage;

		initialize();
	}

	public synchronized void sendPixels(ByteBuffer pixels) {
		epanel.sendPixels(pixels);
	}

	private void initialize() {
		
		bp = new BorderPane();
		
		editor = new Editor(this);
		
		lpanel = new LeftPanel(this);
		bp.setLeft(lpanel.getBox());
		rpanel = new RightPanel(this, bp);
		bp.setRight(rpanel.getBox());
		tpanel = new TopPanel(this, window);
		bp.setTop(tpanel.getBox());
		epanel = new EditPanel(this, bp, editor);
		bp.setCenter(epanel.getBox());
		
		editor.init(epanel);
		
		editor.getTileEditor().init();
		rpanel.getTilePanel().init();
		
		scene = new Scene(bp, DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
		
		fh = new FileHandler(this);
		
		window.setScene(scene);

		window.setOnCloseRequest(e -> {
			if(!fh.isSaved())
				if (!fh.askForSave())
					e.consume();
		});
		
		window.show();
	}

	public synchronized Stage getWindow() {
		return window;
	}

	public synchronized FileHandler getFileHandler() {
		return fh;
	}
	
	public synchronized EditPanel getEditPanel() {
		return epanel;
	}
	
	public synchronized RightPanel getRightPanel() {
		return rpanel;
	}
	
	public Editor getEditor() {
		return editor;
	}
	
	public void stop() {
		window.close();
	}
}