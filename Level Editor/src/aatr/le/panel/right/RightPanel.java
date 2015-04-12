package aatr.le.panel.right;

import java.awt.Paint;

import aatr.le.editor.Editor;
import aatr.le.panel.ToolWindow;
import aatr.le.panel.edit.EditPanel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class RightPanel {

	private VBox box;

	private ToolWindow app;
	private BorderPane bp;
	
	private RightPanelTile rpTile;
	private RightPanelHeader rpHeader;
	
	public RightPanel(ToolWindow app, BorderPane bp) {
		this.app = app;
		this.bp = bp;

		rpTile = new RightPanelTile(app, this);
		rpHeader = new RightPanelHeader(app, this);
		
		update();
	}

	public void update() {
		switch (Editor.getEditMode()) {
		case TILE:
			box = rpTile.getBox();
			break;
		case HEADER:
			box = rpHeader.getBox();
			rpHeader.init();
			break;
		default:
			box = new VBox();
			((VBox)box).setMinWidth((ToolWindow.DEFAULT_WINDOW_WIDTH - EditPanel.PANEL_WIDTH) / 2);
			break;
		}
		
		box.setMinWidth((ToolWindow.DEFAULT_WINDOW_WIDTH - EditPanel.PANEL_WIDTH) / 2);
		box.setMaxWidth((ToolWindow.DEFAULT_WINDOW_WIDTH - EditPanel.PANEL_WIDTH) / 2);

		box.setPadding(new Insets(20));
		
		bp.setRight(box);
	}
	
	public RightPanelTile getTilePanel() {
		return rpTile;
	}

	public VBox getBox() {
		return box;
	}
}