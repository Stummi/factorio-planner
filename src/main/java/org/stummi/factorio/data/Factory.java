package org.stummi.factorio.data;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Factory {
	public static final Factory NONE = new Factory(AssemblingMachine.NONE, Recipe.NONE);
	AssemblingMachine type;
	Recipe recipe;

	public Factory(AssemblingMachine type) {
		this(type, Recipe.NONE);
	}

	public Factory(Recipe recipe) {
		this(AssemblingMachine.NONE, recipe);
	}

	public List<ItemThroughput> getResourceThroughputs() {
		return throughputs(recipe.getResources());
	}

	public List<ItemThroughput> getProductThroughputs() {
		return throughputs(recipe.getProducts());
	}

	private List<ItemThroughput> throughputs(List<ItemAmount> items) {
		return items
				.stream()
				.map(res -> res.perTicks(recipe.getCycleTime())
						.multiply(type.getCraftingSpeed()))
				.collect(Collectors.toList());
	}

}
