package org.stummi.factorio.gui;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import org.stummi.factorio.luaconf.GamePathFinder;
import org.stummi.factorio.luaconf.LuaEntityLoader;

public class PlannerApplication extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws IOException {
		File appDir = determineAppDir(stage);
		if (appDir == null) {
			// No Game directory could be determined and user canceled the
			// dialog.
			// We cannot do anything at this point
			return;
		}

		LuaEntityLoader loader = new LuaEntityLoader(appDir);
		JFXImageFactory factory = new JFXImageFactory(
				loader.getResourceFactory());

		GoalTable goalTable = new GoalTable(loader, factory);
		goalTable.setEditable(true);

		FactoryTable factoryTable = new FactoryTable(loader, factory);
		factoryTable.setEditable(true);

		ReportTable reportTable = new ReportTable(loader, factory);
		reportTable.addRecipeListener(factoryTable::addRecipe);
		goalTable.setPrefWidth(100);
		reportTable.setPrefWidth(100);
		factoryTable.setPrefWidth(100);

		InvalidationListener mkReport = me -> reportTable
				.setReport(factoryTable.getReport(goalTable.getGoals()));
		factoryTable.addListener(mkReport);
		goalTable.addListener(mkReport);

		SplitPane vbox = new SplitPane(tableTitle("Goals", goalTable), tableTitle("Factories", factoryTable), tableTitle("Result", reportTable));
		vbox.setDividerPositions(0.3, 0.7);
		BorderPane bp = new BorderPane(vbox);

		bp.setPrefSize(800, 600);
		Scene scene = new Scene(bp);
		scene.getStylesheets().add(
				PlannerApplication.class.getResource("/style.css")
						.toExternalForm());
		stage.setScene(scene);
		stage.show();
	}

	private static Node tableTitle(String title, Node table) {
		Label lblTitle = new Label(title);
		lblTitle.getStyleClass().add("label-head");
		lblTitle.setTextAlignment(TextAlignment.CENTER);
		
		BorderPane bp = new BorderPane(table, lblTitle, null, null, null);
		BorderPane.setAlignment(lblTitle, Pos.TOP_CENTER);
		return bp;
	}

	private File determineAppDir(Stage stage) {
		File file = GamePathFinder.findGameDir();
		if (file != null) {
			// We could find the game directory automatically
			return file;
		}

		// no success with automatically file finding. Lets bother the User
		// with it
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
		alert.setTitle("Factorio Game Directory");
		alert.setHeaderText("The Game directory could not be found on your system");
		alert.setContentText("This tool needs a Factorio installation on your system to work\n"
				+ "But it couldn't determine it automatically.\nDo you want to choose a directory now?");
		Optional<ButtonType> button = alert.showAndWait();
		if (!button.isPresent() || button.get() != ButtonType.YES) {
			return null;
		}

		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Please Select the Game directory");
		Alert wrongDirectory = new Alert(AlertType.WARNING);
		wrongDirectory.setTitle("Wrong Directory");
		wrongDirectory
				.setHeaderText("This does not seem to be a Factorio Directory");
		wrongDirectory
				.setContentText("Hint: The Installation Directory contains a data\ndirectory which contains a core and base directory");

		do {
			file = chooser.showDialog(stage);
			if (file == null) {
				// user canceled
				return null;
			} else if (!GamePathFinder.isSuitable(file)) {
				wrongDirectory.showAndWait();
				chooser.setInitialDirectory(file);
			} else {
				return file;
			}
		} while (true);
	}
}