package org.stummi.factorio.gui;

import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;

import org.stummi.factorio.Report.Throughputs;

public class DiffTableCell extends TableCell<Throughputs, Number> {
	@Override
	protected void updateItem(Number val, boolean empty) {
		super.updateItem(val, empty);
		if (val == null || empty) {
			setText(null);
			return;
		}

		double dval = val.doubleValue();
		setText(Double.toString(dval));
		if(dval < 0) {
			setTextFill(Color.RED);
		} else if (dval > 0) {
			setTextFill(Color.GREEN);
		} else {
			setTextFill(Color.BLACK);
		}
		
	}
}
