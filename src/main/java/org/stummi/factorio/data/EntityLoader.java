package org.stummi.factorio.data;

import java.util.Map;

import org.stummi.factorio.gui.ImageFactory;

public interface EntityLoader {
	Map<String, Item> getItems();
	Map<String, Receipe> getReceipes();
	Map<String, AssemblingMachine> getAssemblingMachines();

	ImageFactory getImageFactory();
}
