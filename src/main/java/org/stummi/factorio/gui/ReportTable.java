package org.stummi.factorio.gui;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import org.stummi.factorio.Report;
import org.stummi.factorio.Report.Throughputs;
import org.stummi.factorio.data.Item;

import com.sun.javafx.binding.DoubleConstant;
import com.sun.javafx.binding.ObjectConstant;

public class ReportTable extends TableView<Throughputs> {
	public ReportTable(ImageFactory factory) {
		TableColumn<Throughputs, Item> itemColumn = new TableColumn<>("Item");

		itemColumn
				.setCellFactory(cd -> new ImageViewTableCell<Throughputs, Item>(
						i -> factory.getImage(i.getIconName())));
		itemColumn.setCellValueFactory(cdf -> ObjectConstant.valueOf(cdf
				.getValue().getProduct()));
		itemColumn.setPrefWidth(48);
		
		TableColumn<Throughputs, Number> inColumn = new TableColumn<>("In/min");
		inColumn.setCellValueFactory(cdf -> DoubleConstant.valueOf(cdf.getValue().getInPerSecond() * 60));

		TableColumn<Throughputs, Number> outColumn = new TableColumn<>("Out/min");
		outColumn.setCellValueFactory(cdf -> DoubleConstant.valueOf(cdf.getValue().getOutPerSecond() * 60));

		TableColumn<Throughputs, Number> diffColumn = new TableColumn<>("Diff");
		diffColumn.setCellValueFactory(cdf -> DoubleConstant.valueOf(cdf.getValue().getDiffPerSecond() * 60));
		diffColumn.setCellFactory(k -> new DiffTableCell());
		
		getColumns().addAll(itemColumn, inColumn, outColumn, diffColumn);
	}

	public void setReport(Report report) {
		getItems().setAll(report.getThroughputs().values());
	}
}
