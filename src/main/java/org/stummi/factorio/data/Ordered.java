package org.stummi.factorio.data;

import java.util.Comparator;

public interface Ordered  {
	Order getOrder();

	static Comparator<? super Ordered> orderedComparator() {
		return Comparator.comparing(Ordered::getOrder);
	}

}
