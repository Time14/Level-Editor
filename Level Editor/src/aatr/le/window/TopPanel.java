package aatr.le.window;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

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
		fileItems.add(new MenuItem("New"));
		fileItems.get(0).setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
		fileItems.add(new MenuItem("Open File..."));
		fileItems.get(1).setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
		fileItems.add(new SeparatorMenuItem());
		fileItems.add(new MenuItem("Save"));
		fileItems.get(3).setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
		fileItems.add(new MenuItem("Save As..."));
		fileItems.get(4).setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+S"));
		fileItems.add(new SeparatorMenuItem());
		fileItems.add(new MenuItem("Exit"));
		fileItems.get(6).setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
		
		menuFile.getItems().addAll(fileItems);
	}
	
	private void initEditMenu() {
		menuEdit = new Menu("Edit");
		editItems = new ArrayList<>();
		editItems.add(new MenuItem("Undo"));
		editItems.get(0).setAccelerator(KeyCombination.keyCombination("Ctrl+Z"));
		editItems.add(new MenuItem("Redo"));
		editItems.get(1).setAccelerator(KeyCombination.keyCombination("Ctrl+Y"));
		editItems.add(new SeparatorMenuItem());
		editItems.add(new MenuItem("Cut"));
		editItems.get(3).setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
		editItems.add(new MenuItem("Copy"));
		editItems.get(4).setAccelerator(KeyCombination.keyCombination("Ctrl+C"));
		editItems.add(new MenuItem("Paste"));
		editItems.get(5).setAccelerator(KeyCombination.keyCombination("Ctrl+V"));
		
		menuEdit.getItems().addAll(editItems);
	}
	
	private void initHelpMenu() {
		menuHelp = new Menu("Help");
		helpItems = new ArrayList<>();
		helpItems.add(new MenuItem("Info"));
		helpItems.get(0).setAccelerator(KeyCombination.keyCombination(""));
		helpItems.add(new MenuItem("Help"));
		helpItems.add(new SeparatorMenuItem());
		helpItems.add(new MenuItem("Settings..."));
		
		menuHelp.getItems().addAll(helpItems);
	}
	
	public Node getBox() {
		return box;
	}
	
}