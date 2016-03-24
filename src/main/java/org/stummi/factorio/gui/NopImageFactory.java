package org.stummi.factorio.gui;

import javafx.scene.image.Image;

public class NopImageFactory implements ImageFactory {

	@Override
	public Image getImage(String name) {
		return new Image("/questionmark.png");
	}

}
