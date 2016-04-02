package org.stummi.factorio.data;

import lombok.Value;

@Value
public class ItemSubgroup implements Named, Ordered {
	private final String name;
	private final Order order;
	private final ItemGroup group;
}
