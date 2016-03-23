package org.stummi.factorio.data;

import lombok.Value;

@Value
public class FactoryType implements NameAndIcon {
	public static final FactoryType NONE = new FactoryType("None", "questionmark", 0);

	String name;
	String iconName;
	int moduleSlots;

}
