package aatr.le.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

public final class Loader {
	
	public static final String loadSource(String path) {
		try {
			File file = new File(path);
			StringBuilder sb = new StringBuilder();
			
			if(file.exists()) {
				
				Scanner s = new Scanner(file);
			
				while(s.hasNextLine()) {
					
					sb.append(s.nextLine() + "\n");
				}
				
				s.close();
				
				return sb.toString();
			}
			
			System.err.println("\"404 Error\", no file found at \"" + path + "\"");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "How the hell did you even get here?!";
	}
	
	public static final BufferedImage loadImage(String path) throws IOException {
		return ImageIO.read(new File(path));
	}
}