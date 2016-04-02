package org.stummi.factorio.data;

import java.util.List;
import java.util.Map;

import org.stummi.factorio.luaconf.LuaEntityLoader;

/**
 * Interface for loading the game Entities.
 *
 * Currently the only implementation is {@link LuaEntityLoader} which loads the
 * entities from the game Data.
 */
public interface EntityLoader {

	/**
	 * Returns all {@link Item}s known mapped by their internal name
	 */
	Map<String, Item> getItems();

	/**
	 * Returns all {@link Recipe}s known mapped by their internal name
	 */
	Map<String, Recipe> getRecipes();

	/**
	 * Returns all {@link AssemblingMachine}s known mapped by their internal
	 * name
	 */
	Map<String, AssemblingMachine> getAssemblingMachines();

	List<Grouped<Item>> getGroupedItems();
	
	List<Grouped<Recipe>> getGroupedRecipes();
	
	/**
	 * Returns a {@link ResourceFactory} capable of loading the Icons named by
	 * the Enties returned from this instance
	 */
	ResourceFactory getResourceFactory();
	
	// convience methods
	default AssemblingMachine getAssemblingMachine(String name) {
		return getAssemblingMachines().get(name);
	}
	
	default Recipe getRecipe(String name) {
		return getRecipes().get(name);
	}
	
	default Item getItem(String name) {
		return getItems().get(name);
	}
	
	default Factory getFactory(String assemblingMachine, String recipe) {
		return new Factory(getAssemblingMachine(assemblingMachine), getRecipe(recipe));
	}
}
