package org.stummi.factorio.data;

import lombok.Value;

@Value
public class ItemThroughput {
	Item item;
	Throughput throughput;

	public ItemThroughput multiply(double factor) {
		return new ItemThroughput(item, throughput.multiply(factor));
	}
}
