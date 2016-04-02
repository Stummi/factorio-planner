package org.stummi.factorio.data;

import java.util.Comparator;

public interface Named {
	String getName();
	
	
	static public <T extends Entity> Comparator<T> nameComparator() {
		return Comparator.comparing(Entity::getName);
	}

}
