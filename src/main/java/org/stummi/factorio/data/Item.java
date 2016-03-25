package org.stummi.factorio.data;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
/**
 * An Item denotes anything that can be produced or consumed by Factories
 */
public class Item implements Entity {
	String name;
	String iconName;

	public Item(String name) {
		this(name, null);
	}

	public ItemAmount amount(double d) {
		return new ItemAmount(this, d);
	}

	public ItemAmount once() {
		return amount(1);
	}

	public ItemAmount twice() {
		return amount(2);
	}

}
