package aatr.le.gfx.texture;

import java.util.HashMap;

public final class TextureLibrary {
	
	private static final HashMap<String, Texture> textures = new HashMap<>();
	
	
	public static final void registerTexture(String name, Texture texture) {
		textures.put(name, texture);
	}
	
	public static final Texture getTexture(String name) {
		return textures.get(name);
	}
	
	public static final void destroy() {
		for(Texture tex : textures.values())
			tex.destroy();
	}
}