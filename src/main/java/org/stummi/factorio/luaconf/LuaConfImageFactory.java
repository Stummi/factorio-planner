package org.stummi.factorio.luaconf;

import java.io.File;

import org.stummi.factorio.gui.ImageFactory;

import javafx.scene.image.Image;

public class LuaConfImageFactory implements ImageFactory {

	private File baseDir;

	public LuaConfImageFactory(File baseDir) {
		this.baseDir = baseDir;
	}

	@Override
	public Image getImage(String name) {
		if (name.startsWith("__base__")) {
			String fileName = name.replace("__base__", baseDir.getAbsolutePath());
			return new Image(new File(fileName).toURI().toString());
		} else {
			return new Image("/" + name + ".png");
		}
	}

}
