package org.stummi.factorio.data;

import java.util.List;

import lombok.Value;

@Value
public class Subgrouped<T extends GroupableEntity> {
	ItemSubgroup subgroup;
	List<T> entities;
}
