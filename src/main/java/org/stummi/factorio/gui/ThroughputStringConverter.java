package org.stummi.factorio.gui;

import java.util.Locale;

import org.stummi.factorio.data.Throughput;

import javafx.util.StringConverter;

public class ThroughputStringConverter extends StringConverter<Throughput> {

	@Override
	public String toString(Throughput object) {
		return String.format(Locale.US, "%.3f", object.amountPerMinute());
	}

	@Override
	public Throughput fromString(String string) {
		return Throughput.perMinute(Double.parseDouble(string));
	}

}
