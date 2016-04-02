package org.stummi.factorio.gui;

import java.util.function.BiConsumer;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

public class ButtonTableCell<S, T> extends TableCell<S, T> {
	final Button cellButton = new Button("Add");

	public ButtonTableCell(String title, BiConsumer<ActionEvent, T> handler) {
		cellButton.setOnAction(ae -> handler.accept(ae, getItem()));
	}

	// Display button if the row is not empty
	@Override
	protected void updateItem(T t, boolean empty) {
		super.updateItem(t, empty);
		if (empty || t == null) {
			setGraphic(null);
		} else {
			setGraphic(cellButton);
		}
	}
}