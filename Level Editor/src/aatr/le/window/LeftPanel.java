package aatr.le.window;

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
	}
	
	public Node getBox() {
		return box;
	}
	
}