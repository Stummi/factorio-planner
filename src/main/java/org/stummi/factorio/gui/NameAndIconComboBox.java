package org.stummi.factorio.gui;

import org.stummi.factorio.data.NameAndIcon;

import javafx.scene.control.ComboBox;

public class NameAndIconComboBox<T extends NameAndIcon> extends ComboBox<T> {
	private final ImageFactory imageFactory;

	public NameAndIconComboBox(ImageFactory factory) {
		this.imageFactory = factory;
		setPrefSize(50, 50);
		setButtonCell(new ImageViewListCell<>(t -> factory.getImage(t.getIconName())));
		setCellFactory(data -> new NameAndIconListCell<>(imageFactory));

	}

}
