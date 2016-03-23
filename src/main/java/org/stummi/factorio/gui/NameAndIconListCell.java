package org.stummi.factorio.gui;

import org.stummi.factorio.data.NameAndIcon;

import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NameAndIconListCell<T extends NameAndIcon> extends ListCell<T> {
	private final ImageFactory factory;

	@Override
	protected void updateItem(T item, boolean isEmpty) {
		super.updateItem(item, isEmpty);
		if (item == null) {
			setGraphic(null);
			setText(null);
		} else {
			String iconName = item.getIconName();
			setGraphic(new ImageView(factory.getImage(iconName)));
			setText(item.getName());
		}
	}
}
