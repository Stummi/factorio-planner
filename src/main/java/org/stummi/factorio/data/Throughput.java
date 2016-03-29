package org.stummi.factorio.data;

import lombok.Value;

@Value
public class Throughput {
	public static final Throughput NONE = Throughput.perMinute(0);
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

	public static Throughput perMinute(double amount) {
		return new Throughput(amount, Ticks.forSeconds(60));
	}

	/**
	 * Returns a Throughput which is a sum of this and the other Throughput
	 * 
	 * If both Throughputs are based on the same amount of ticks, the new
	 * one will also be based on this tick amount, else the result will be based
	 * on 60 ticks
	 */
	public Throughput add(Throughput other) {
		if (this.ticks.equals(other.ticks)) {
			return new Throughput(this.amount + other.amount, ticks);
		} else {
			return Throughput.perMinute(amountPerMinute() + other.amount);
		}
	}
}
