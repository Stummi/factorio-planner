package org.stummi.factorio.data;

public interface GroupableEntity extends Entity, Ordered {
	ItemSubgroup getSubgroup();
}
