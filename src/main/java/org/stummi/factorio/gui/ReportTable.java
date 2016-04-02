package org.stummi.factorio.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import org.stummi.factorio.Report;
import org.stummi.factorio.Report.Throughputs;
import org.stummi.factorio.data.EntityLoader;
import org.stummi.factorio.data.Item;
import org.stummi.factorio.data.Recipe;

import com.sun.javafx.binding.DoubleConstant;
import com.sun.javafx.binding.ObjectConstant;

public class ReportTable extends TableView<Throughputs> {
	private EntityLoader loader;
	private final List<Consumer<Recipe>> recipeListeners = new ArrayList<>();

	@SuppressWarnings("unchecked")
	public ReportTable(EntityLoader loader, JFXImageFactory factory) {
		this.loader = loader;
		TableColumn<Throughputs, Item> itemColumn = new TableColumn<>("Item");

		itemColumn
				.setCellFactory(cd -> new ImageViewTableCell<Throughputs, Item>(
						i -> factory.getImage(i.getIconName())));
		itemColumn.setCellValueFactory(cdf -> ObjectConstant.valueOf(cdf
				.getValue().getItem()));
		itemColumn.setPrefWidth(48);

		TableColumn<Throughputs, Number> inColumn = new TableColumn<>("In/min");
		inColumn.setCellValueFactory(cdf -> DoubleConstant.valueOf(cdf
				.getValue().getConsumption().amountPerMinute()));

		TableColumn<Throughputs, Number> outColumn = new TableColumn<>(
				"Out/min");
		outColumn.setCellValueFactory(cdf -> DoubleConstant.valueOf(cdf
				.getValue().getProduction().amountPerMinute()));

		TableColumn<Throughputs, Number> diffColumn = new TableColumn<>("Diff");
		diffColumn.setCellValueFactory(cdf -> DoubleConstant.valueOf(cdf
				.getValue().getDiff().amountPerMinute()));
		diffColumn.setCellFactory(k -> new DiffTableCell());

		TableColumn<Throughputs, Recipe> addRecipeColumn = new TableColumn<>(
				"Add");
		addRecipeColumn.setCellValueFactory(cdf -> new SimpleObjectProperty<>(
				getRecipe(cdf.getValue())));
		addRecipeColumn.setCellFactory(tc -> new ButtonTableCell<>("Add", (ae,
				r) -> addRecipe(r)));

		getColumns().addAll(itemColumn, inColumn, outColumn, diffColumn,
				addRecipeColumn);

	}

	private void addRecipe(Recipe r) {
		recipeListeners.forEach(l -> l.accept(r));
	}

	private Recipe getRecipe(Throughputs throughputs) {
		Item item = throughputs.getItem();
		if (throughputs.getProduction().amountPerMinute() > 0) {
			return null;
		}

		Recipe found = null;
		for (Recipe r : loader.getRecipes().values()) {
			if (r.getProducts().size() != 1) {
				continue;
			}

			if (r.getProducts().get(0).getItem() == item) {
				if (found != null) {
					return null;
				}
				found = r;
			}
		}

		return found;
	}

	public void setReport(Report report) {
		getItems().setAll(report.getThroughputs().values());
		sort();
	}

	public void addRecipeListener(Consumer<Recipe> listener) {
		recipeListeners.add(listener);
	}
}
