package aatr.le.panel.top;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import aatr.le.panel.ToolWindow;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class TopPanel {
	
	private MenuBar menuBar;
	private Menu menuFile;
	private ArrayList<MenuItem> fileItems;
	private Menu menuEdit;
	private ArrayList<MenuItem> editItems;
	private Menu menuHelp;
	private ArrayList<MenuItem> helpItems;
	
	private HBox box;
	
	private Stage window;
	
	private ToolWindow app;
	
	public TopPanel(ToolWindow app, Stage window) {
		this.app = app;
		this.window = window;
		
		box = new HBox();
		
		box.setBorder(new Border(new BorderStroke(Color.RED, Color.RED,
				Color.RED, Color.RED, BorderStrokeStyle.SOLID,
				BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,
				BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
				BorderStroke.MEDIUM, new Insets(0))));
		
		initMenuBar();
	}
	
	private void initMenuBar() {
		initFileMenu();
		initEditMenu();
		initHelpMenu();
		
		menuBar = new MenuBar(menuFile, menuEdit, menuHelp);
		menuBar.setPrefSize(ToolWindow.DEFAULT_WINDOW_WIDTH, 0);
		
		box.getChildren().add(menuBar);
	}
	
	private void initFileMenu() {
		menuFile = new Menu("File");
		fileItems = new ArrayList<>();
		
		//New File
		
		fileItems.add(new MenuItem("New"));
		fileItems.get(0)
				.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
		
		fileItems.get(0).setOnAction(e -> {
			if(!app.getFileHandler().isSaved())
				if(!app.getFileHandler().askForSave())
					return;
			
			app.getEditor().initEmptyWorld();
			
			app.getEditor().getTileEditor().init();
			app.getRightPanel().getTilePanel().init();
			
			app.getEditor().updateWorld();
			
			app.getFileHandler().setSaved(true);
			
			app.getWindow().setTitle(ToolWindow.TITLE + " - untitled.map");
		});
		
		//Open File
		
		fileItems.add(new MenuItem("Open File..."));
		fileItems.get(1)
				.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
		fileItems.add(new SeparatorMenuItem());
		
		fileItems.get(1).setOnAction(e -> {
			app.getFileHandler().open();
		});
		
		//Save File
		
		fileItems.add(new MenuItem("Save"));
		fileItems.get(3)
				.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
		
		fileItems.get(3).setOnAction(e -> {
			app.getFileHandler().save();
		});
		
		//Save File As
		
		fileItems.add(new MenuItem("Save As..."));
		fileItems.get(4).setAccelerator(
				KeyCombination.keyCombination("Ctrl+Shift+S"));
		
		fileItems.get(4).setOnAction(e -> {
			app.getFileHandler().saveAs();
		});
		
		fileItems.add(new SeparatorMenuItem());
		
		//Exit
		
		fileItems.add(new MenuItem("Exit"));
		fileItems.get(6)
				.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
		
		fileItems.get(6).setOnAction(
				e -> {
					app.getWindow().fireEvent(
							new WindowEvent(app.getWindow(),
									WindowEvent.WINDOW_CLOSE_REQUEST));
				});
		
		menuFile.getItems().addAll(fileItems);
	}
	
	private void initEditMenu() {
		menuEdit = new Menu("Edit");
		editItems = new ArrayList<>();
		editItems.add(new MenuItem("Undo"));
		editItems.get(0)
				.setAccelerator(KeyCombination.keyCombination("Ctrl+Z"));
		editItems.add(new MenuItem("Redo"));
		editItems.get(1)
				.setAccelerator(KeyCombination.keyCombination("Ctrl+Y"));
		editItems.add(new SeparatorMenuItem());
		editItems.add(new MenuItem("Cut"));
		editItems.get(3)
				.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
		editItems.add(new MenuItem("Copy"));
		editItems.get(4)
				.setAccelerator(KeyCombination.keyCombination("Ctrl+C"));
		editItems.add(new MenuItem("Paste"));
		editItems.get(5)
				.setAccelerator(KeyCombination.keyCombination("Ctrl+V"));
		
		menuEdit.getItems().addAll(editItems);
	}
	
	private void initHelpMenu() {
		menuHelp = new Menu("Help");
		helpItems = new ArrayList<>();
		helpItems.add(new MenuItem("Info"));
		helpItems.get(0).setAccelerator(KeyCombination.keyCombination("F9"));
		helpItems.add(new MenuItem("Help"));
		helpItems.get(1).setAccelerator(KeyCombination.keyCombination("F12"));
		helpItems.add(new SeparatorMenuItem());
		helpItems.add(new MenuItem("Settings..."));
		helpItems.get(3).setAccelerator(KeyCombination.keyCombination("F10"));
		
		menuHelp.getItems().addAll(helpItems);
	}
	
	public Node getBox() {
		return box;
	}
	
}