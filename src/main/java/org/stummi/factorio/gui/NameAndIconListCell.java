package org.stummi.factorio.gui;

import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;

import org.stummi.factorio.data.Entity;

public class NameAndIconListCell<T extends Entity> extends ListCell<T> {
	private final JFXImageFactory factory;
	private final ImageView imageView = new ImageView(JFXImageFactory.DEFAULT_IMAGE);

	public NameAndIconListCell(JFXImageFactory factory) {
		this.factory = factory;
		setGraphic(imageView);
	}
	
	@Override
	protected void updateItem(T item, boolean isEmpty) {
		super.updateItem(item, isEmpty);
		if (item == null) {
			imageView.setImage(JFXImageFactory.DEFAULT_IMAGE);
			setText(null);
		} else {
			String iconName = item.getIconName();
			imageView.setImage(factory.getImage(iconName));
			setText(item.getName());
		}
	}
}
