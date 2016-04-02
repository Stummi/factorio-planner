package org.stummi.factorio.data;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
/**
 * An Item denotes anything that can be produced or consumed by Factories
 */
public class Item implements GroupableEntity {
	String name;
	String iconName;
	Order order;
	ItemSubgroup subgroup;

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
