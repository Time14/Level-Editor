package aatr.le.window;

import java.awt.event.InputEvent;
import java.nio.ByteBuffer;


import org.lwjgl.util.vector.Vector2f;

import aatr.le.gfx.Renderer;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Box;

public class CenterPanel {
	
	public static final int PANEL_WIDTH = 600;
	public static final int PANEL_HEIGHT = 600;
	
	private ImageView vimage;
	private WritableImage wimage;
	private PixelWriter pwriter;
	
	private VBox box;
	
	private BorderPane bp;
	
	private ToolWindow app;
	
	public CenterPanel(ToolWindow app, BorderPane bp) {
		this.app = app;
		this.bp = bp;
		
		wimage = new WritableImage(PANEL_WIDTH, PANEL_HEIGHT);
		vimage = new ImageView(wimage);
		pwriter = wimage.getPixelWriter();
		
		box = new VBox(vimage);
		box.setAlignment(Pos.BOTTOM_CENTER);
		
		vimage.addEventHandler(MouseEvent.ANY, new EventHandler<MouseEvent>(){
			
			private double prevX = 0, prevY = 0;
			
			public void handle(MouseEvent e) {
				if(e.getEventType() == MouseEvent.MOUSE_PRESSED) {
					prevX = e.getX();
					prevY = e.getY();
					System.out.println("hej");
				} else if(e.getEventType() == MouseEvent.MOUSE_DRAGGED) {
					double currentX = e.getX();
					double currentY = e.getY();
					double dx = (currentX - prevX);
					double dy = (currentY - prevY);
					
					app.getRenderer().translation.x += dx;
					app.getRenderer().translation.y += dy;
					
					prevX = currentX;
					prevY = currentY;
				}
			}
		});
		
		vimage.setOnScroll(e -> {
			app.getRenderer().scale.x += ((float)e.getDeltaY()) / 1000;
			app.getRenderer().scale.y += ((float)e.getDeltaY()) / 1000;
			
			Vector2f delta = new Vector2f(-(float)(PANEL_WIDTH / 2 - e.getX()), -(float)(PANEL_HEIGHT / 2 - e.getY()));
			
			if(e.getDeltaY() < 0)
				delta = new Vector2f();
			
			app.getRenderer().translation.x -= (delta.x * Math.abs(delta.length()) / 100) * (app.getRenderer().scale.x / (Renderer.MAX_SCALE - Renderer.MIN_SCALE));
			app.getRenderer().translation.y -= (delta.y * Math.abs(delta.length()) / 100) * (app.getRenderer().scale.y / (Renderer.MAX_SCALE - Renderer.MIN_SCALE));
			
			if(app.getRenderer().scale.x < Renderer.MIN_SCALE)
				app.getRenderer().scale.x = Renderer.MIN_SCALE;
			
			if(app.getRenderer().scale.y < Renderer.MIN_SCALE)
				app.getRenderer().scale.y = Renderer.MIN_SCALE;
			
		});
		
	}
	
	public void sendPixels(ByteBuffer pixels) {
		pwriter.setPixels(0, 0, PANEL_WIDTH, PANEL_HEIGHT,
				javafx.scene.image.PixelFormat.getByteRgbInstance(), pixels,
				PANEL_WIDTH * Renderer.BYTES_PIXEL);
	}
	
	public Node getBox() {
		return box;
	}
	
}