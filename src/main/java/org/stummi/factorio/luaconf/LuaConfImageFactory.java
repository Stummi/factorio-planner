package org.stummi.factorio.luaconf;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.stummi.factorio.gui.ImageFactory;

import javafx.scene.image.Image;

public class LuaConfImageFactory implements ImageFactory {
	private static final Image DEFAULT_IMAGE = new Image("/questionmark.png");

	private final Map<String, Image> cache = new HashMap<>();
	private final File baseDir;

	public LuaConfImageFactory(File baseDir) {
		this.baseDir = baseDir;
	}

	@Override
	public Image getImage(String name) {
		if (name == null) {
			return DEFAULT_IMAGE;
		}

		Image ret = cache.get(name);
		if (ret == null) {
			try {
				String fileName = name.replace("__base__",
						baseDir.getAbsolutePath());
				ret = new Image(new File(fileName).toURI().toString());
			} catch (Exception e) {
				System.err.println("Could not load image: " + name);
				ret = DEFAULT_IMAGE;
			}
			cache.put(name, ret);
		}
		return ret;
	}

}
