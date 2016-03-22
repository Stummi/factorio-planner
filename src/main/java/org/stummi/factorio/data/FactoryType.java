package org.stummi.factorio.data;

import lombok.Value;

@Value
public class FactoryType {
	public static final FactoryType NONE = new FactoryType("Dummy", 0);

	String name;
	int moduleSlots;

}
