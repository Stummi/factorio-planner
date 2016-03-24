package org.stummi.factorio.gui;

import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;

import org.stummi.factorio.data.Entity;

public class NameAndIconComboBox<T extends Entity> extends ComboBox<T> {
	private final ImageFactory imageFactory;

	public NameAndIconComboBox(ImageFactory factory) {
		this.imageFactory = factory;
		setPrefSize(50, 50);
		setButtonCell(new ImageViewListCell<>(t -> factory.getImage(t.getIconName())));
		setCellFactory(data -> new NameAndIconListCell<>(imageFactory));
		setPadding(Insets.EMPTY);
		
	}

}
