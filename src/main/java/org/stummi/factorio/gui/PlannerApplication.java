package org.stummi.factorio.gui;

import java.io.File;
import java.io.IOException;

import org.stummi.factorio.luaconf.LuaEntityLoader;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PlannerApplication extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws IOException {
		LuaEntityLoader loader = new LuaEntityLoader(new File(System.getenv("HOME"), ".steam/steam/SteamApps/common/Factorio/"));

		FactoryTable factoryTable = new FactoryTable(loader);
		factoryTable.setEditable(true);
		
		ReportTable reportTable = new ReportTable(loader.getImageFactory());

		Button addButton = new Button("Add");
		Button delButton = new Button("Delete");
		Button reportButton = new Button("Report");

		addButton.setOnAction(me -> factoryTable.newItem());
		delButton.setOnAction(me -> factoryTable.deleteSelected());
		reportButton.setOnAction(me -> reportTable.setReport(factoryTable.getReport()));
		HBox hbox = new HBox(addButton, delButton, reportButton);
		HBox vbox = new HBox(factoryTable, reportTable);
		BorderPane bp = new BorderPane(vbox);
		bp.setTop(hbox);

		Scene scene = new Scene(bp);
	    scene.getStylesheets().add(PlannerApplication.class.getResource("/style.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
	}
}