package aatr.le.panel.edit;

import java.awt.event.InputEvent;
import java.nio.ByteBuffer;

import org.lwjgl.util.vector.Vector2f;

import aatr.le.debug.Debug;
import aatr.le.editor.Editor;
import aatr.le.editor.Editor.EditMode;
import aatr.le.panel.ToolWindow;
import aatr.le.util.Util;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;

public class EditPanel {

	public static final int PANEL_WIDTH = 600;
	public static final int PANEL_HEIGHT = 600;

	private ImageView vimage;
	private WritableImage wimage;
	private PixelWriter pwriter;

	private VBox box;
	private TabPane tp;
	
	private Tab tabTile, tabProperties, tabEntity, tabEvent, tabHeader;
	
	private BorderPane bp;

	private ToolWindow app;
	
	private Editor editor;
	
	public EditPanel(ToolWindow app, BorderPane bp, Editor editor) {
		this.app = app;
		this.bp = bp;
		this.editor = editor;
		
		wimage = new WritableImage(PANEL_WIDTH, PANEL_HEIGHT);
		vimage = new ImageView(wimage);
		pwriter = wimage.getPixelWriter();
		
		box = new VBox();
		
		box.setBorder(new Border(new BorderStroke(Color.BLUE, Color.BLUE,
				Color.BLUE, Color.BLUE, BorderStrokeStyle.SOLID,
				BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,
				BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
				BorderStroke.MEDIUM, new Insets(0))));
		
		box.setMaxWidth(PANEL_WIDTH);
		
		tp = new TabPane();
		
		tp.setMaxWidth(PANEL_WIDTH);
		
		tabTile = new Tab("Tile");
		tabTile.setClosable(false);
		
		tabProperties = new Tab("Tile Properties");
		tabProperties.setClosable(false);
		
		tabEntity = new Tab("Entity");
		tabEntity.setClosable(false);
		
		tabEvent = new Tab("Event");
		tabEvent.setClosable(false);
		
		tabHeader = new Tab("Header");
		tabHeader.setClosable(false);
		
		tp.getTabs().addAll(tabTile, tabProperties, tabEntity, tabEvent, tabHeader);
		
		tabTile.setOnSelectionChanged(e -> {
			if(tabTile.isSelected()) {
				Editor.setEditMode(EditMode.TILE);
				
				app.getEditor().getTileEditor().init();
				app.getRightPanel().getTilePanel().init();
			} else {
				app.getEditor().getTileEditor().destroyTileSetRenderer();
			}
			editor.updateWorld();
			app.getRightPanel().update();
			
			//Setting up TileSet Image
		});
		
		tabProperties.setOnSelectionChanged(e -> {
			if(tabProperties.isSelected())
				Editor.setEditMode(EditMode.PROPERTIES);
			editor.updateWorld();
			app.getRightPanel().update();
		});
		
		tabEntity.setOnSelectionChanged(e -> {
			if(tabEntity.isSelected())
				Editor.setEditMode(EditMode.ENTITY);
			editor.updateWorld();
			app.getRightPanel().update();
		});
		
		tabEvent.setOnSelectionChanged(e -> {
			if(tabEvent.isSelected())
				Editor.setEditMode(EditMode.EVENT);
			editor.updateWorld();
			app.getRightPanel().update();
		});
		
		tabHeader.setOnSelectionChanged(e -> {
			if(tabHeader.isSelected())
				Editor.setEditMode(EditMode.HEADER);
			editor.updateWorld();
			app.getRightPanel().update();
		});
		
		box.setAlignment(Pos.BOTTOM_CENTER);
		
		box.getChildren().addAll(tp, vimage);
		
		box.setPadding(new Insets(0));
		
		vimage.addEventHandler(MouseEvent.ANY, e -> {
			
			editor.updateWorld();
			
			switch(e.getButton()) {
			case SECONDARY:
				if (e.getEventType() == MouseEvent.MOUSE_PRESSED)
					editor.setPrevCoords(e.getX(), e.getY());
				else if (e.getEventType() == MouseEvent.MOUSE_DRAGGED)
					editor.drag(e.getX(), e.getY());
				break;
			case PRIMARY:
				if(e.getEventType() == MouseEvent.MOUSE_DRAGGED || e.getEventType() == MouseEvent.MOUSE_PRESSED)
					if(Editor.getEditMode() == EditMode.TILE)
						editor.getTileEditor().paint(e.getX(), e.getY());
				break;
			default:
				break;
			}
			
		});
		
		vimage.setOnScroll(e -> {
			editor.zoom(e.getDeltaY(), e.getX(), e.getY());
			editor.updateWorld();
		});
	}

	public void sendPixels(ByteBuffer pixels) {
		pwriter.setPixels(0, 0, PANEL_WIDTH, PANEL_HEIGHT,
				javafx.scene.image.PixelFormat.getByteRgbInstance(), pixels,
				PANEL_WIDTH * Editor.BYTES_PIXEL);
	}

	public Node getBox() {
		return box;
	}

}