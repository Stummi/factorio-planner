package org.stummi.factorio.gui;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;

public class ImageFactory {
	private final Map<String, Image> cache = new HashMap<>();

	public Image getImage(String name) {
		if (name == null) {
			return null;
		}

		return new Image("/" + name + ".png");
	}

}
