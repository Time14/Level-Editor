package aatr.le.panel.right;

import java.nio.ByteBuffer;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import aatr.le.debug.Debug;
import aatr.le.editor.Editor;
import aatr.le.editor.EditorTile;
import aatr.le.panel.ToolWindow;
import aatr.le.panel.edit.EditPanel;
import aatr.le.world.World;

public class RightPanelTile {
	
	private VBox box;
	private ScrollPane tileArea;
	
	private ImageView tileSetVImage;
	private WritableImage tileSetWImage;
	private PixelWriter tileSetPWriter;
	
	private ImageView borderVImage;
	private WritableImage borderWImage;
	private PixelWriter borderPWriter;
	
	private ToolWindow app;
	private RightPanel rp;
	
	public RightPanelTile(ToolWindow app, RightPanel rp) {
		this.app = app;
		this.rp = rp;
		
		box = new VBox();
		
		box.setSpacing(10);
		
		tileArea = new ScrollPane();
		
		tileArea.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		tileArea.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		
		borderWImage = new WritableImage(EditorTile.BORDER_SIZE,
				EditorTile.BORDER_SIZE);
		borderVImage = new ImageView(borderWImage);
		borderPWriter = borderWImage.getPixelWriter();
		
		borderVImage.addEventHandler(MouseEvent.ANY, e -> {
			if (e.getEventType() == MouseEvent.MOUSE_PRESSED) {
				int tx = Math.floorDiv((int) e.getX(), EditorTile.BORDER_TILE_SIZE);
				int ty = Math.floorDiv((int) e.getY(), EditorTile.BORDER_TILE_SIZE);
				
				app.getEditor().getTileEditor().paintBorder(ty * 2 + tx);
			}
		});
		
		box.getChildren().add(new Text("Border Tiles:"));
		box.getChildren().add(borderVImage);
		box.getChildren().add(new Separator(Orientation.HORIZONTAL));
		box.getChildren().add(new Text("Tileset:"));
		
		box.getChildren().add(tileArea);
		
		box.setBorder(new Border(new BorderStroke(Color.GREEN, Color.GREEN,
				Color.GREEN, Color.GREEN, BorderStrokeStyle.SOLID,
				BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,
				BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
				BorderStroke.MEDIUM, new Insets(0))));
	}
	
	public void sendPixels(ByteBuffer pixels) {
		tileSetPWriter.setPixels(0, 0, app.getEditor().getTileEditor()
				.getTileSetWidth(), app.getEditor().getTileEditor()
				.getTileSetHeight(),
				javafx.scene.image.PixelFormat.getByteRgbInstance(), pixels,
				app.getEditor().getTileEditor().getTileSetWidth()
						* Editor.BYTES_PIXEL);
	}
	
	public void sendBorder(ByteBuffer pixels) {
		borderPWriter.setPixels(0, 0, EditorTile.BORDER_SIZE,
				EditorTile.BORDER_SIZE,
				javafx.scene.image.PixelFormat.getByteRgbInstance(), pixels,
				EditorTile.BORDER_SIZE * Editor.BYTES_PIXEL);
	}
	
	public void init() {
		tileSetWImage = new WritableImage(app.getEditor().getTileEditor()
				.getTileSetWidth(), app.getEditor().getTileEditor()
				.getTileSetHeight());
		tileSetVImage = new ImageView(tileSetWImage);
		tileSetPWriter = tileSetWImage.getPixelWriter();
		
		tileSetVImage.addEventHandler(MouseEvent.ANY, e -> {
			
			if (e.getEventType() == MouseEvent.MOUSE_PRESSED) {
				int tx = Math.floorDiv((int) e.getX(), World.GRID_SIZE);
				int ty = Math.floorDiv((int) e.getY(), World.GRID_SIZE);
				
				int tilesX = app.getEditor().getWorld().getTileSet()
						.getTilesX();
				app.getEditor().getTileEditor().selectTile(ty * tilesX + tx);
			}
			
		});
		
		tileArea.setContent(tileSetVImage);
		
		app.getEditor().getTileEditor().updateTileSet();
		
		app.getEditor().getTileEditor().updateBorder();
	}
	
	public VBox getBox() {
		return box;
	}
	
}