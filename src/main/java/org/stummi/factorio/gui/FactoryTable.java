package org.stummi.factorio.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import lombok.Getter;

import org.stummi.factorio.Plan;
import org.stummi.factorio.Report;
import org.stummi.factorio.data.AssemblingMachine;
import org.stummi.factorio.data.Entity;
import org.stummi.factorio.data.EntityLoader;
import org.stummi.factorio.data.Factory;
import org.stummi.factorio.data.ItemThroughput;
import org.stummi.factorio.data.Recipe;

public class FactoryTable extends TableView<FactoryTable.Entry> implements
		Observable {

	@Getter
	class Entry {
		ObjectProperty<AssemblingMachine> type = new SimpleObjectProperty<>();
		ObjectProperty<Recipe> receipe = new SimpleObjectProperty<>();
		ObjectProperty<Double> balancedCount = new SimpleObjectProperty<>(0D);
		ObjectProperty<Double> userCount = new SimpleObjectProperty<>(1D);
		BooleanProperty autobalance = new SimpleBooleanProperty();

		Entry(Factory fact) {
			type.set(fact.getType());
			receipe.set(fact.getRecipe());
			InvalidationListener li = l -> fireInvalidation();
			type.addListener(li);
			receipe.addListener(li);
			userCount.addListener(li);
			autobalance.addListener(e -> refresh());
			autobalance.addListener(li);
		}

		public Factory toFactory() {
			Factory ret = new Factory(type.get(), receipe.get());
			return ret;
		}

		public ObjectProperty<Double> getCount() {
			return autobalance.get() ? balancedCount : userCount;
		}

		void addToPlan(Plan plan) {
			Factory fact = toFactory();
			if(autobalance.get()) {
				plan.addFactoryToBalance(fact);
			} else {
				plan.addFactory(fact, userCount.get());
			}
		}
	}

	private List<InvalidationListener> listeners = new ArrayList<>();

	@SuppressWarnings("unchecked")
	public FactoryTable(EntityLoader loader, JFXImageFactory ifact) {
		setEditable(true);
		

		ObservableList<AssemblingMachine> factories = FXCollections
				.observableArrayList(loader.getAssemblingMachines().values());
		factories.sort(Entity.nameComparator());

		ObservableList<Recipe> recipes = FXCollections
				.observableArrayList(loader.getRecipes().values());
		recipes.sort(Entity.nameComparator());

		TableColumn<Entry, AssemblingMachine> typeCol = new TableColumn<>(
				"type");
		typeCol.setEditable(true);
		typeCol.setCellValueFactory(cd -> cd.getValue().getType());
		typeCol.setCellFactory(cv -> new NameAndIconTableCell<>(ifact,
				factories));
		typeCol.setComparator(Entity.nameComparator());
		typeCol.setPrefWidth(70);

		TableColumn<Entry, Recipe> recipeCol = new TableColumn<>("receipe");
		recipeCol.setEditable(true);
		recipeCol.setCellValueFactory(cd -> cd.getValue().getReceipe());
		recipeCol.setCellFactory(cv -> new NameAndIconTableCell<>(ifact,
				recipes));
		recipeCol.setComparator(Entity.nameComparator());
		recipeCol.setPrefWidth(70);

		TableColumn<Entry, Double> countCol = new TableColumn<>("count");
		countCol.setEditable(true);
		countCol.setCellValueFactory(cd -> cd.getValue().getCount());
		countCol.setCellFactory(col -> new SpinnerCell<>());
		
		TableColumn<Entry, Boolean> autobalanceCol = new TableColumn<>("auto");
		autobalanceCol.setEditable(true);
		autobalanceCol.setCellValueFactory(cd -> cd.getValue().getAutobalance());
		autobalanceCol.setCellFactory(col -> new CheckBoxTableCell<>());
		
		getColumns().addAll(typeCol, recipeCol, countCol, autobalanceCol);
		
		InvalidationListener il = i -> fireInvalidation();
		getItems().addListener(il);

		MenuItem addItem = new MenuItem("New");
		addItem.setOnAction(ae -> newItem());

		MenuItem delItem = new MenuItem("Remove Selected");
		delItem.setOnAction(ae -> deleteSelected());

		ContextMenu menu = new ContextMenu(addItem, delItem);
		setContextMenu(menu);

		getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}

	public void addRecipe(Recipe recipe) {
		Entry entry = new Entry(Factory.NONE);
		entry.autobalance.set(true);
		entry.receipe.set(recipe);
		getItems().add(entry);
	}
	
	public void addItem(Factory factory) {
		getItems().add(new Entry(factory));
	}

	public void newItem() {
		getItems().add(new Entry(Factory.NONE));
	}

	public void deleteSelected() {
		getItems().removeAll(getSelectionModel().getSelectedItems());
	}

	public Report getReport(List<ItemThroughput> goals) {
		Plan plan = new Plan();
		plan.addGoals(goals);
		getItems().forEach(e -> e.addToPlan(plan));
		Report balancedReport = plan.getBalancedReport();
		
		Map<Factory, Double> balancedFactories = new HashMap<>(balancedReport.getBalancedFactories());
		for(Entry e : getItems()) {
			if(!e.autobalance.get()) {
				continue;
			}
			
			Double val = balancedFactories.remove(e.toFactory());
			e.balancedCount.set(val);
		}
		return balancedReport;
	}

	private void fireInvalidation() {
		listeners.forEach(l -> l.invalidated(this));
	}

	@Override
	public void addListener(InvalidationListener arg0) {
		listeners.add(arg0);
	}

	@Override
	public void removeListener(InvalidationListener arg0) {
		listeners.remove(arg0);
	}

}
