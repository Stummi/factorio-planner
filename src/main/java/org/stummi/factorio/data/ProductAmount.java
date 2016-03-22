package org.stummi.factorio.data;

import lombok.Value;

@Value
public class ProductAmount {
	Product product;
	double amount;

	public ProductThroughput perSeconds(double seconds) {
		return perTicks(Ticks.forSeconds(seconds));
	}

	public ProductThroughput perTicks(int ticks) {
		return perTicks(new Ticks(ticks));
	}

	public ProductThroughput perTicks(Ticks ticks) {
		return new ProductThroughput(product, new Throughput(amount, ticks));
	}
}
