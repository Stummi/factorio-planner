package org.stummi.factorio.data;

import lombok.Value;

@Value
public class AssemblingMachine implements Entity {
	public static final AssemblingMachine NONE = new AssemblingMachine("None", null, 0, 1D);

	String name;
	String iconName;
	int moduleSlots;
	double craftingSpeed;

}
