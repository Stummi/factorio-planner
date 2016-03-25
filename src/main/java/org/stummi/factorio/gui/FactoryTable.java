package org.stummi.factorio.gui;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.Getter;

import org.stummi.factorio.Plan;
import org.stummi.factorio.Report;
import org.stummi.factorio.data.AssemblingMachine;
import org.stummi.factorio.data.Entity;
import org.stummi.factorio.data.EntityLoader;
import org.stummi.factorio.data.Factory;
import org.stummi.factorio.data.Recipe;

public class FactoryTable extends TableView<FactoryTable.Entry> implements Observable{

	@Getter
	class Entry {
		ObjectProperty<AssemblingMachine> type = new SimpleObjectProperty<>();
		ObjectProperty<Recipe> receipe = new SimpleObjectProperty<>();
		IntegerProperty count = new SimpleIntegerProperty(1);
		
		Entry(Factory fact) {
			type.set(fact.getType());
			receipe.set(fact.getReceipe());
			InvalidationListener li = l -> fireInvalidation(); 
			type.addListener(li);
			receipe.addListener(li);
			count.addListener(li);
		}

		public Factory toFactory() {
			Factory ret = new Factory(type.get(), receipe.get());
			ret.setCount(count.get());
			return ret;
		}

	}

	private List<InvalidationListener> listeners = new ArrayList<>();

	@SuppressWarnings("unchecked")
	public FactoryTable(EntityLoader loader, JFXImageFactory ifact) {
		setEditable(true);

		ObservableList<AssemblingMachine> factories = FXCollections.observableArrayList(loader.getAssemblingMachines().values());
		factories.sort(Entity.nameComparator());
		
		ObservableList<Recipe> recipes = FXCollections.observableArrayList(loader.getRecipes().values());
		recipes.sort(Entity.nameComparator());
		
		TableColumn<Entry, AssemblingMachine> typeCol = new TableColumn<>("type");
		typeCol.setEditable(true);
		typeCol.setCellValueFactory(cd -> cd.getValue().getType());
		typeCol.setCellFactory(cv -> new NameAndIconTableCell<>(ifact, factories));
		typeCol.setComparator(Entity.nameComparator());
		typeCol.setPrefWidth(70);

		TableColumn<Entry, Recipe> recipeCol = new TableColumn<>("receipe");
		recipeCol.setEditable(true);
		recipeCol.setCellValueFactory(cd -> cd.getValue().getReceipe());
		recipeCol.setCellFactory(cv -> new NameAndIconTableCell<>(ifact, recipes));
		recipeCol.setComparator(Entity.nameComparator());
		recipeCol.setPrefWidth(70);
		
		TableColumn<Entry, Number> countCol = new TableColumn<>("count");
		countCol.setEditable(true);
		countCol.setCellValueFactory(cd -> cd.getValue().getCount());
		countCol.setCellFactory(cd -> new SpinnerCell<Entry>());
		
		getColumns().addAll(typeCol, recipeCol, countCol);
		
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

	public void addItem(Factory factory) {
		getItems().add(new Entry(factory));
	}

	public void newItem() {
		addItem(new Factory());
	}

	public void deleteSelected() {
		getItems().removeAll(getSelectionModel().getSelectedItems());
	}

	public Report getReport() {
		Plan plan = new Plan();
		getItems().forEach(i -> plan.addFactory(i.toFactory()));
		return plan.report();
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
