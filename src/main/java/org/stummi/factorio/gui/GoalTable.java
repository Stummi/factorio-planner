package org.stummi.factorio.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.Getter;

import org.stummi.factorio.data.EntityLoader;
import org.stummi.factorio.data.Item;
import org.stummi.factorio.data.ItemThroughput;
import org.stummi.factorio.data.Throughput;

public class GoalTable extends TableView<GoalTable.Entry> implements Observable {
	@Getter
	class Entry {
		final ObjectProperty<Item> item = new SimpleObjectProperty<>();
		final ObjectProperty<Throughput> throughput = new SimpleObjectProperty<>(
				Throughput.NONE);

		public Entry() {
			InvalidationListener il = e -> fireInvalidate();
			item.addListener(il);
			throughput.addListener(il);
		}
		
		public ItemThroughput toItemThroughput() {
			return new ItemThroughput(item.get(), throughput.get());
		}
	}
	
	private final List<InvalidationListener> listeners = new ArrayList<>();

	public GoalTable(EntityLoader loader, JFXImageFactory imageFactory) {
		ObservableList<Item> items = FXCollections.observableArrayList(loader
				.getItems().values());
		Collections.sort(items, Comparator.comparing(Item::getName));

		TableColumn<GoalTable.Entry, Item> itemCol = new TableColumn<>("item");
		itemCol.setCellValueFactory(cda -> cda.getValue().getItem());
		itemCol.setCellFactory(tc -> new NameAndIconTableCell<>(imageFactory,
				items));

		TableColumn<GoalTable.Entry, Throughput> throughputCol = new TableColumn<>(
				"Prod / Min");
		throughputCol
				.setCellValueFactory(cda -> cda.getValue().getThroughput());
		throughputCol.setCellFactory(tc -> new AcceptOnExitTableCell<>(
				new ThroughputStringConverter()));

		getColumns().add(itemCol);
		getColumns().add(throughputCol);

		MenuItem addItem = new MenuItem("New");
		addItem.setOnAction(ae -> newItem());

		MenuItem delItem = new MenuItem("Remove Selected");
		delItem.setOnAction(ae -> deleteSelected());

		ContextMenu menu = new ContextMenu(addItem, delItem);
		setContextMenu(menu);

		getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}

	private void deleteSelected() {
		getItems().removeAll(getSelectionModel().getSelectedItems());
	}

	private void newItem() {
		getItems().add(new Entry());
	}

	public List<ItemThroughput> getGoals() {
		return getItems().stream().filter(
				e -> (e.item.get() != null && e.throughput.get()
						.amountPerMinute() > 0)).map(Entry::toItemThroughput)
						.collect(Collectors.toList());
	}

	@Override
	public void addListener(InvalidationListener listener) {	
		listeners.add(listener);
	}
	
	@Override
	public void removeListener(InvalidationListener listener) {
		listeners.remove(listener);
	}
	
	public void fireInvalidate() {
		listeners.forEach(li -> li.invalidated(this));
	}
}
