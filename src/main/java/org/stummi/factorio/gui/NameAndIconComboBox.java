package org.stummi.factorio.gui;

import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;

import org.stummi.factorio.data.Entity;

public class NameAndIconComboBox<T extends Entity> extends ComboBox<T> {
	private final JFXImageFactory imageFactory;

	public NameAndIconComboBox(JFXImageFactory factory) {
		this.imageFactory = factory;
		setPrefSize(50, 50);
		setButtonCell(new ImageViewListCell<>(t -> factory.getImage(t.getIconName())));
		setCellFactory(data -> new NameAndIconListCell<>(imageFactory));
		setPadding(Insets.EMPTY);
		
	}

}
