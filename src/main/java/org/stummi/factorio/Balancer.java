package org.stummi.factorio;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.stummi.factorio.data.Factory;
import org.stummi.factorio.data.Item;
import org.stummi.factorio.data.ItemThroughput;
import org.stummi.factorio.data.Throughput;

@RequiredArgsConstructor
public class Balancer {
	private final Report report;
	private final Map<Item, Throughput> goals;
	private final Map<Factory, Double> factories;

	private static Map<Factory, Double> toMap(List<Factory> toBalance) {
		return toBalance.stream().collect(
				Collectors.toMap(f -> f, f -> 0D, (a, b) -> a));
	}

	public Balancer(Report report, Map<Item, Throughput> goals, List<Factory> toBalance) {
		this(report, goals, toMap(toBalance));
		goals.keySet().forEach(report::addItem);
	}

	
	public void balance() {
		while(balanceStep());
		
		report.setBalancedFactoriesCount(factories);
	}
	
	public boolean balanceStep() {
		boolean changedSomething = false;
		for(Entry<Factory, Double> e : factories.entrySet()) {
			Factory f = e.getKey();
			Double add = 0D;
			
			for(ItemThroughput ia : f.getProductThroughputs()) {
				Item i = ia.getItem();
				Throughput tpFact = ia.getThroughput();
				double goal = goals.getOrDefault(i, Throughput.NONE).amountPerSecond();
				double currentDiff = report.getThroughputs(i).getDiff().amountPerSecond() - goal;
				if(currentDiff >= goal) {
					// nothing to balance
					continue;
				}
				
				add = Math.max(add, -currentDiff / tpFact.amountPerSecond());
			}
			
			if(add == 0) {
				continue;
			}
			
			changedSomething = true;
			e.setValue(e.getValue() + add);
			report.addFactory(f, add);
		}
		
		return changedSomething;
	}
	
	

}
