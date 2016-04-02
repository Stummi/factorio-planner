package org.stummi.factorio.data;

import java.util.Collections;
import java.util.List;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Value
@Builder
public class AssemblingMachine implements Entity {
	public static final AssemblingMachine NONE = new AssemblingMachine("None",
			null, 0, 1D, Collections.emptyList(), 0);

	String name;
	String iconName;
	int moduleSlots;
	double craftingSpeed;
	@Singular("craftingCategory")
	List<String> craftingCategories;
	int ingredientCount;

	/**
	 * Checks if a Assembling Machine can produce this recipe, by checking if
	 * the recipes category is contained in the AssemblingMachines list of
	 * crafting Categories and if the number ingredients of this recipe does not
	 * exceed this machines ingredient count
	 */
	public boolean canProduce(Recipe recipe) {
		return craftingCategories.contains(recipe.getCategory())
				&& ingredientCount >= recipe.getResources().size();
	}

}
