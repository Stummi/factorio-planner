package org.stummi.factorio.gui;

import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

public class ImageViewListCell<T> extends ListCell<T> {
	private final ImageView imageView = new ImageView();
	private final Callback<T, Image> imagemapper;

	public ImageViewListCell(Callback<T, Image> mapper) {
		this.imagemapper = mapper;
		setGraphic(imageView);
	}

	@Override
	protected void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);
		if (empty || item == null) {
			imageView.setImage(null);
		} else {
			imageView.setImage(imagemapper.call(item));
		}
	}

}
