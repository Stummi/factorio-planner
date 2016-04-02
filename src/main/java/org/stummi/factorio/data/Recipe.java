package org.stummi.factorio.data;

import java.util.Arrays;
import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Singular;
import lombok.Value;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Value
@Builder
public class Recipe implements GroupableEntity {
	// NULL-Recipe, producing nothing for nothing (using 60 Ticks to avoid zero
	// divisions on calculating throughputs)
	public static final Recipe NONE = builder().name("None").cycleTime(60).category("crafting").build();

	private final String name;
	private final String iconName;
	private final int cycleTime;
	@Singular private final List<ItemAmount> products;
	@Singular private final List<ItemAmount> resources;
	private final String category;
	private final Order order;
	private final ItemSubgroup subgroup;

	public static Recipe singleProduct(int cycleTime, ItemAmount product, ItemAmount... resources) {
		return builder().name(product.getItem().getName()).cycleTime(cycleTime).product(product).resources(Arrays.asList(resources)).build();
	}
}
