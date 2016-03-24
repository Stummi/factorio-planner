package org.stummi.factorio.data;

import lombok.Value;

@Value
public class ItemAmount {
	Item item;
	double amount;

	public ItemThroughput perSeconds(double seconds) {
		return perTicks(Ticks.forSeconds(seconds));
	}

	public ItemThroughput perTicks(int ticks) {
		return perTicks(new Ticks(ticks));
	}

	public ItemThroughput perTicks(Ticks ticks) {
		return new ItemThroughput(item, new Throughput(amount, ticks));
	}
}
