package aatr.le.panel.right;

import aatr.le.panel.ToolWindow;
import aatr.le.panel.edit.EditPanel;
import aatr.le.world.World;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class RightPanelHeader {
	
	private VBox box;
	
	private ToolWindow app;
	private RightPanel rp;
	
	private FlowPane fpDimensions;
	private TextField fpdWidth;
	private TextField fpdHeight;
	private Button fpdChange;
	
	public RightPanelHeader(ToolWindow app, RightPanel rp) {
		this.app = app;
		this.rp = rp;
		
		box = new VBox();
		
		fpDimensions = new FlowPane();
		fpDimensions.setPadding(new Insets(5));
		
		box.getChildren().add(fpDimensions);
	}
	
	public void init() {
		
		//Text Field Width
		
		fpdWidth = new TextField(String.valueOf(app.getEditor().getWorld().getWidth()));
		
		fpdWidth.setPrefColumnCount(4);
		
		fpdWidth.textProperty().addListener((observableVal, oldVal, newVal) -> {
			if(!newVal.matches("\\d*") || fpdWidth.getText().length() > 4) {
				fpdWidth.setText(oldVal);
			} else if(newVal.length() > 0) {
				if(Integer.parseInt(newVal) < 1)
					fpdWidth.setText("1");
			}
		});
		
		fpdWidth.focusedProperty().addListener((observableVal, wasFocused, isFocused) -> {
			if(!isFocused) {
				if(fpdWidth.getText().length() < 1)
					fpdWidth.setText("0");
			}
		});
		
		//Text Field Height
		
		fpdHeight = new TextField(String.valueOf(app.getEditor().getWorld().getHeight()));
		
		fpdHeight.setPrefColumnCount(4);
		
		fpdHeight.textProperty().addListener((observableVal, oldVal, newVal) -> {
			if(!newVal.matches("\\d*") || fpdHeight.getText().length() > 4) {
				fpdHeight.setText(oldVal);
			} else if(newVal.length() > 0) {
				if(Integer.parseInt(newVal) < 1)
					fpdHeight.setText("1");
			}
		});
		
		fpdHeight.focusedProperty().addListener((observableVal, wasFocused, isFocused) -> {
			if(!isFocused) {
				if(fpdHeight.getText().length() < 1)
					fpdHeight.setText("1");
			}
		});
		
		//Button Change Width & Height
		
		fpdChange = new Button("Change");
		
		fpdChange.setOnAction(e -> {
			
			if(fpdWidth.getText().length() < 1)
				fpdWidth.setText("1");
			
			if(fpdHeight.getText().length() < 1)
				fpdWidth.setText("1");
			
			app.getEditor().getWorld().setDimensions(Integer.parseInt(fpdWidth.getText()), Integer.parseInt(fpdHeight.getText()));
			
			app.getFileHandler().setEdited();
			
		});
		
		//Adding Nodes to Width and Height Flow Pane
		
		fpDimensions.getChildren().clear();
		fpDimensions.getChildren().addAll(new Text("Width: "), fpdWidth, new Text("     Height: "), fpdHeight, new Text("     "), fpdChange);
	}
	
	public VBox getBox() {
		return box;
	}
	
}