package org.stummi.factorio;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Plan {
	final Map<Receipe, Integer> receipes = new HashMap<>();
	
	public void addReceipe(Receipe receipe) {
		addReceipe(receipe, 1);
	}

	public void addReceipe(Receipe receipe, int count) {
		receipes.put(receipe, getReceipeCount(receipe) + count);
		
	}

	private int getReceipeCount(Receipe receipe) {
		return receipes.getOrDefault(receipe, 0);
	}
	
	public Report report() {
		Report r = new Report();
		for(Entry<Receipe, Integer> e : receipes.entrySet()) {
			r.addReceipe(e.getKey(), e.getValue());
		}
		return r;
	}
}
