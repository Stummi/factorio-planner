package org.stummi.factorio.gui;

import java.util.Comparator;

import org.stummi.factorio.data.AssemblingMachine;
import org.stummi.factorio.data.EntityLoader;
import org.stummi.factorio.data.Factory;
import org.stummi.factorio.data.Receipe;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.Getter;

public class FactoryTable extends TableView<FactoryTable.Entry> {

	@Getter
	static class Entry {
		ObjectProperty<AssemblingMachine> type = new SimpleObjectProperty<>();
		ObjectProperty<Receipe> receipe = new SimpleObjectProperty<>();

		Entry(Factory fact) {
			type.set(fact.getType());
			receipe.set(fact.getReceipe());
		}

	}

	public FactoryTable(EntityLoader loader) {
		ImageFactory ifact = loader.getImageFactory();

		TableColumn<Entry, AssemblingMachine> typeCol = new TableColumn<>();
		setEditable(true);

		ObservableList<AssemblingMachine> factory = FXCollections.observableArrayList(loader.getAssemblingMachines().values());

		typeCol.setEditable(true);
		typeCol.setCellValueFactory(cd -> cd.getValue().getType());
		typeCol.setCellFactory(cv -> new NameAndIconTableCell<>(ifact, factory));
		typeCol.setComparator(Comparator.comparing(AssemblingMachine::getName));
		getColumns().add(typeCol);
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

}
