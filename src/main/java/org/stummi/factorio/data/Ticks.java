package org.stummi.factorio.data;

import lombok.Value;

@Value
public class Ticks {
	public static final int TICKS_PER_SECOND = 60;
	public static final Ticks SECOND = new Ticks(TICKS_PER_SECOND);
	public static final Ticks HSECOND = new Ticks(TICKS_PER_SECOND / 2);

	int ticks;

	public Ticks plus(Ticks t) {
		return new Ticks(ticks + t.ticks);
	}

	public Ticks minus(Ticks t) {
		return new Ticks(ticks - t.ticks);
	}

	public static Ticks forSeconds(double seconds) {
		return new Ticks((int) (seconds * TICKS_PER_SECOND));
	}

	public double toSeconds() {
		return (double) ticks / TICKS_PER_SECOND;
	}
}
