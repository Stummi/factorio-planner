package org.stummi.factorio.data;

import lombok.Value;

@Value
public class Order implements Comparable<Order> {
	String rawString;

	@Override
	public int compareTo(Order o) {
		// TODO: Some actual logic here
		return rawString.compareTo(o.rawString);
	}
}
