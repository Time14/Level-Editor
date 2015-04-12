package aatr.le.panel.left;

import aatr.le.panel.ToolWindow;
import aatr.le.panel.edit.EditPanel;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;

public class LeftPanel {
	
	private VBox box;
	
	private ToolWindow app;
	
	public LeftPanel(ToolWindow app) {
		this.app = app;
		
		box = new VBox();
		box.setMinWidth((ToolWindow.DEFAULT_WINDOW_WIDTH - EditPanel.PANEL_WIDTH) / 2);
		box.setMaxWidth((ToolWindow.DEFAULT_WINDOW_WIDTH - EditPanel.PANEL_WIDTH) / 2);
	}
	
	public Node getBox() {
		return box;
	}
	
}