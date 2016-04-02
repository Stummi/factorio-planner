package org.stummi.factorio.data;

import lombok.Value;

@Value
public class ItemGroup implements Entity, Ordered {
	String name;
	Order order;
	String iconName;
}
