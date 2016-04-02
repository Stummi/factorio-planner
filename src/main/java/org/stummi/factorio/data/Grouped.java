package org.stummi.factorio.data;

import java.util.List;

import lombok.Value;

@Value
public class Grouped<T extends GroupableEntity> {
	ItemGroup group;
	List<Subgrouped<T>> subgroups;
}
