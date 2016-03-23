package org.stummi.factorio.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class PlannerApplication extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {

		FactoryTable table = new FactoryTable();
		table.setEditable(true);

		Button addButton = new Button("Add");
		Button delButton = new Button("Delete");
		Button reportButton = new Button("Report");

		addButton.setOnAction(me -> table.newItem());
		delButton.setOnAction(me -> table.deleteSelected());

		HBox hbox = new HBox(addButton, delButton, reportButton);
		BorderPane bp = new BorderPane(table);
		bp.setBottom(hbox);

		Scene scene = new Scene(bp);
		stage.setScene(scene);
		stage.show();
	}
}