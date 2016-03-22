package org.stummi.factorio;

import java.util.ArrayList;
import java.util.List;

import org.stummi.factorio.data.Factory;

public class Plan {
	final List<Factory> factories = new ArrayList<>();

	public void addReceipe(Receipe receipe) {
		addReceipe(receipe, 1);
	}

	public void addFactory(Factory factory) {
		factories.add(factory);
	}

	public void addReceipe(Receipe receipe, int count) {
		Factory fact = new Factory(receipe);
		fact.setCount(count);
		factories.add(fact);
	}

	public Report report() {
		Report r = new Report();
		for (Factory fact : factories) {
			r.addFactory(fact);
		}
		return r;
	}
}
