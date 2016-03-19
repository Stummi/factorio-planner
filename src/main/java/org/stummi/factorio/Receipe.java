package org.stummi.factorio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Value
public class Receipe {
	private final String name;
	private final int cycleTime;
	private final List<ProductAmount> products;
	private final List<ProductAmount> resources;

	public static Builder builder(String name, int cycleTime) {
		return new Builder(name, cycleTime);
	}

	public static Receipe singleProduct(int cycleTime, ProductAmount product, ProductAmount ... resources) {
		return new Builder(product.getProduct().getName(), cycleTime).product(product).resource(resources).build();
	}
	
	@RequiredArgsConstructor
	public static class Builder {
		final Map<Product, Double> resources = new HashMap<>();
		final Map<Product, Double> products = new HashMap<>();
		final String name;
		final int cycleTime;

		Builder resource(ProductAmount ... amounts) {
			for(ProductAmount amount : amounts) {
				resource(amount.getProduct(), amount.getAmount());
			}
			return this;
		}
		
		Builder resource(Product p, double amount) {
			resources.put(p, amount);
			return this;
		}

		Builder product(ProductAmount ... amounts) {
			for(ProductAmount amount : amounts) {
				product(amount.getProduct(), amount.getAmount());
			}
			return this;
		}

		Builder product(Product p, double amount) {
			products.put(p, amount);
			return this;
		}

		Receipe build() {
			return new Receipe(name, cycleTime, makeList(resources),
					makeList(products));
		}

		private List<ProductAmount> makeList(Map<Product, Double> resources2) {
			return resources2.entrySet().stream()
					.map(e -> new ProductAmount(e.getKey(), e.getValue()))
					.collect(Collectors.toList());
		}
	}
}
