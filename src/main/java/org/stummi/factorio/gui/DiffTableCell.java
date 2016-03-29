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
		
		

		Throughputs tp =  (Throughputs) getTableRow().getItem();
	
		double dval = val.doubleValue();

		setText(String.format("%.3f", dval));
		if(dval < 0e-5) {
			setTextFill(Color.RED);
		} else if (dval > 0e-5) {
			setTextFill(Color.GREEN);
		} else {
			setTextFill(Color.BLACK);
		}
		
	}
}
