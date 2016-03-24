package org.stummi.factorio.gui;

import java.io.File;
import java.io.IOException;

import org.stummi.factorio.luaconf.LuaEntityLoader;

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
	public void start(Stage stage) throws IOException {
		LuaEntityLoader loader = new LuaEntityLoader(new File(System.getenv("HOME"), ".steam/steam/steamapps/common/Factorio/"));

		FactoryTable table = new FactoryTable(loader);
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