package org.stummi.factorio.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Value
public class Recipe implements Entity {
	// NULL-Recipe, producing nothing for nothing (using 60 Ticks to avoid zero
	// divisions on calculating throughputs)
	public static final Recipe NONE = builder("None", 60).build();

	private final String name;
	private final String iconName;
	private final int cycleTime;
	private final List<ItemAmount> products;
	private final List<ItemAmount> resources;

	public static Builder builder(String name, int cycleTime) {
		return new Builder(name, cycleTime);
	}

	public static Recipe singleProduct(int cycleTime, ItemAmount product, ItemAmount... resources) {
		return new Builder(product.getItem().getName(), cycleTime).product(product).resource(resources).build();
	}

	@RequiredArgsConstructor
	public static class Builder {
		final Map<Item, Double> resources = new HashMap<>();
		final Map<Item, Double> products = new HashMap<>();
		final String name;
		final int cycleTime;
		String iconName;

		public Builder icon(String iconName) {
			this.iconName = iconName;
			return this;
		}

		public Builder resource(ItemAmount... amounts) {
			for (ItemAmount amount : amounts) {
				resource(amount.getItem(), amount.getAmount());
			}
			return this;
		}

		public Builder resource(Item p, double amount) {
			resources.put(p, amount);
			return this;
		}

		public Builder product(ItemAmount... amounts) {
			for (ItemAmount amount : amounts) {
				product(amount.getItem(), amount.getAmount());
			}
			return this;
		}

		public Builder product(Item p, double amount) {
			products.put(p, amount);
			return this;
		}

		public Recipe build() {
			return new Recipe(name, iconName, cycleTime, makeList(products), makeList(resources));
		}

		private static List<ItemAmount> makeList(Map<Item, Double> resources2) {
			return resources2.entrySet().stream().map(e -> new ItemAmount(e.getKey(), e.getValue())).collect(Collectors.toList());
		}
	}
}
