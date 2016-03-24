package org.stummi.factorio.gui;

import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class SpinnerTableCellFactory<S> implements
		Callback<TableColumn<S, Number>, TableCell<S, Number>> {

	@Override
	public TableCell<S, Number> call(TableColumn<S, Number> param) {

		return new TableCell<S, Number>() {

			Spinner<Number> spinner = new Spinner<>(1, 100, 1, 1);

			{
				spinner.valueProperty().addListener(
						(obs, oldValue, newValue) -> {
							ObservableValue<Number> value = getTableColumn()
									.getCellObservableValue(getIndex());
							if (value instanceof WritableValue) {
								((WritableValue<Number>) value)
										.setValue(newValue);
							}
						});
			}

			protected void updateItem(Number item, boolean empty) {
				super.updateItem(item, empty);

				if (empty) {
					setText(null);
					setGraphic(null);
				} else {
					if (isEditing()) {
						setText(null);
						setGraphic(null);
					} else {
						spinner.getValueFactory().setValue(getItem());
						setText(null);
						setGraphic(spinner);
					}
				}
			};
		};
	}
}