package org.stummi.factorio.data;

import java.util.Comparator;

public interface Entity {
	String getName();
	String getIconName();
	
	static public <T extends Entity> Comparator<T> nameComparator() {
		return Comparator.comparing(Entity::getName);
	}
}
