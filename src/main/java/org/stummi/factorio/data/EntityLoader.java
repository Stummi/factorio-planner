package org.stummi.factorio.data;

import java.util.Map;

public interface EntityLoader {
	Map<String, Item> getItems();
	Map<String, Recipe> getRecipes();
	Map<String, AssemblingMachine> getAssemblingMachines();

	ResourceFactory getResourceFactory();
}
