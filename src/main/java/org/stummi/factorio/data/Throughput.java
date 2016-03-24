package org.stummi.factorio.data;

import lombok.Value;

@Value
public class Throughput {
	double amount;
	Ticks ticks;

	public double amountPerSecond() {
		return amount / ticks.toSeconds();
	}

	public double amountPerSeconds(double seconds) {
		return amountPerSecond() * seconds;
	}

	public double amountPerMinute() {
		return amountPerSeconds(60);
	}

	public Throughput multiply(double factor) {
		return new Throughput(amount * factor, ticks);
	}
}
