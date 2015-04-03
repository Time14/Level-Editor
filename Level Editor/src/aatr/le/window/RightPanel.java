package aatr.le.window;

import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class RightPanel {
	
	private VBox box;
	
	private ToolWindow app;
	
	public RightPanel(ToolWindow app) {
		this.app = app;
		
		box = new VBox();
	}
	
	public Node getBox() {
		return box;
	}
	
}