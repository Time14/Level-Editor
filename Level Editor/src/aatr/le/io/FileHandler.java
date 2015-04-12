package aatr.le.io;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import aatr.le.debug.Debug;
import aatr.le.panel.ToolWindow;
import aatr.le.panel.window.YesNoBox;
import aatr.le.util.Util;
import aatr.le.world.Chunk;
import aatr.le.world.WorldData;
import aatr.le.world.WorldLoader;
import aatr.le.world.tile.Tile;
import aatr.le.world.tile.TileSet;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class FileHandler {
	
	private FileChooser fileChooser;
	private File currentFile;
	
	private boolean saved;
	
	private ToolWindow app;
	
	public FileHandler(ToolWindow app) {
		this.app = app;
		
		fileChooser = new FileChooser();
		
		fileChooser.getExtensionFilters().add(
				new ExtensionFilter("Map File", "*.map"));
		
		fileChooser
				.setInitialDirectory(new File(System.getProperty("user.dir")));
		
		fileChooser.setInitialFileName("untitled.map");
		
		saved = true;
		
		assignTitle();
	}
	
	public void open() {
		
		if (!saved)
			if (!askForSave())
				return;
		
		if (currentFile != null) {
			fileChooser.setInitialDirectory(new File(currentFile
					.getParentFile().getPath()));
		}
		
		fileChooser.setTitle("Open Map File");
		
		File file = fileChooser.showOpenDialog(app.getWindow());
		
		if (file == null)
			return;
		
		currentFile = file;
		
		app.getEditor().loadWorld(file.getAbsolutePath());
		
		app.getEditor().updateWorld();
		
		saved = true;
		
		assignTitle();
	}
	
	public void save() {
		if (currentFile == null)
			saveAs();
		
		if (currentFile == null)
			return;
		
		try (RandomAccessFile raf = new RandomAccessFile(currentFile, "rw")) {
			
			WorldData wd = app.getEditor().getWorld().getWorldData();
			
			FileChannel fc = raf.getChannel();
			
			fc.truncate(0);
			
			ByteBuffer headerData = Util.createByteBuffer(
					WorldLoader.BYTES_HEADER).order(ByteOrder.LITTLE_ENDIAN);
			
			headerData.putShort(wd.WIDTH);
			headerData.putShort(wd.HEIGHT);
			headerData.putShort(wd.TILESET_ID);
			headerData.putShort((short) wd.BORDER[0].getID());
			headerData.putShort((short) wd.BORDER[1].getID());
			headerData.putShort((short) wd.BORDER[2].getID());
			headerData.putShort((short) wd.BORDER[3].getID());
			
			headerData.flip();
			
			fc.write(headerData);
			
			for (int i = 0; i < wd.WIDTH; i++) {
				for (int j = 0; j < wd.HEIGHT; j++) {
					
					ArrayList<Short> chunkData = new ArrayList<>();
					
					short prevID = 0;
					short tileCount = 0;
					
					for (int k = 0; k < Math.pow(Chunk.GRID_DIMENSIONS, 2);) {
						
						short id;
						
						short count = 0;
						
						short startID = (short)Util.toArray(wd.CHUNKS[i][j].getTiles())[k + (count++)].getID();
						
						do {
							
							id = (short)Util.toArray(wd.CHUNKS[i][j].getTiles())[k + (count++)].getID();
							
						} while(startID == id && k + count != Math.pow(Chunk.GRID_DIMENSIONS, 2));
						
						if(k + count < 256)
							count--;
						
						k += count;
						
						if(count > 3) {
							ByteBuffer buffer = Util.createByteBuffer(3 * Short.SIZE).order(ByteOrder.LITTLE_ENDIAN);
							
							buffer.putShort((short)-1);
							buffer.putShort(count);
							buffer.putShort(startID);
							
							buffer.flip();
							
							fc.write(buffer);
						} else {
							ByteBuffer buffer = Util.createByteBuffer(count * Short.SIZE).order(ByteOrder.LITTLE_ENDIAN);
							
							for(int l = 0; l < count; l++)
								buffer.putShort(startID);
							
							buffer.flip();
							
							fc.write(buffer);
						}
						
						count = 0;
					}
				}
			}
			
			ByteBuffer buffer = Util.createByteBuffer((int) raf.length())
					.order(ByteOrder.LITTLE_ENDIAN);
			
			fc.position(0);
			fc.read(buffer);
			
			buffer.flip();
			
			for (int i = 0; i < buffer.capacity() / 2; i++) {
				Debug.log(buffer.getShort());
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		saved = true;
		
		assignTitle();
	}
	
	public void saveAs() {
		fileChooser.setTitle("Save Map File");
		
		File file = fileChooser.showSaveDialog(app.getWindow());
		
		if (file == null)
			return;
		
		currentFile = file;
		
		save();
	}
	
	public void setEdited() {
		saved = false;
		
		assignTitle();
	}
	
	public void setSaved(boolean saved) {
		this.saved = saved;
	}
	
	public void assignTitle() {
		String path = "untitled.map";
		if (currentFile != null)
			path = currentFile.getName();
		
		app.getWindow().setTitle(
				ToolWindow.TITLE + " - " + path + (saved ? "" : "*"));
	}
	
	public String getCurrentFileName() {
		String name = "untitled.map";
		
		if (currentFile != null)
			name = currentFile.getName();
		
		return name;
	}
	
	public boolean askForSave() {
		boolean wasAnswered;
		
		switch (YesNoBox.ask("Save Map File", "Save changes to \""
				+ getCurrentFileName() + "\"?", "Yes", "No")) {
		default:
			wasAnswered = false;
		case YesNoBox.ANSWER_YES:
			wasAnswered = true;
			save();
			if (!saved)
				return false;
			break;
		case YesNoBox.ANSWER_NO:
			wasAnswered = true;
			break;
		case YesNoBox.ANSWER_NONE:
			wasAnswered = false;
			break;
		}
		
		return wasAnswered;
	}
	
	public boolean isSaved() {
		return saved;
	}
}