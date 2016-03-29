package org.stummi.factorio.gui;

import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;
import javafx.util.converter.DoubleStringConverter;

import org.stummi.factorio.gui.FactoryTable.Entry;

@SuppressWarnings("unchecked")
public class SpinnerCell<S> extends TableCell<Entry, Double> {


	Spinner<Double> spinner = new Spinner<>(1D, 100D, 1D, 1D);
	Label textField = new Label();

	public SpinnerCell() {
		spinner.getValueFactory().converterProperty().set(new DoubleStringConverter());
		spinner.valueProperty().addListener(
				(obs, oldValue, newValue) -> {
					ObservableValue<Double> value = getTableColumn()
							.getCellObservableValue(getIndex());
					if (value instanceof WritableValue) {
						((WritableValue<Double>) value)
								.setValue(newValue);
					}
				});
	}

	@Override
	protected void updateItem(Double item, boolean empty) {
		super.updateItem(item, empty);
		
		if (empty) {
			setText(null);
			setGraphic(null);
		} else {
			TableRow<Entry> row = getTableRow();
			boolean autobalance = false;
			if(row.getItem() != null) {
				autobalance = row.getItem().autobalance.get();
			}
			
			setEditable(!autobalance);

			
			if (isEditing()) {
				setText(null);
				setGraphic(null);
			} else {
				Double val = getItem();
				if(autobalance) {
					textField.setText(val == null ? "" : String.format("%.2f", getItem()));
					setGraphic(textField);
				} else {
					spinner.getValueFactory().setValue(val);
					setGraphic(spinner);
				}
				setText(null);
			}
		}
	};
}