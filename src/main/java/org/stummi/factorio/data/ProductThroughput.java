package org.stummi.factorio.data;

import lombok.Value;

@Value
public class ProductThroughput {
	Product product;
	Throughput throughput;

	public ProductThroughput multiply(int factor) {
		return new ProductThroughput(product, throughput.multiply(factor));
	}
}
