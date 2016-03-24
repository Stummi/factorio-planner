package org.stummi.factorio.data;

import lombok.Value;

@Value
public class AssemblingMachine implements Entity {
	public static final AssemblingMachine NONE = new AssemblingMachine("None", "questionmark", 0);

	String name;
	String iconName;
	int moduleSlots;

}
