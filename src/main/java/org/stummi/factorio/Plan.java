package org.stummi.factorio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Value;

import org.stummi.factorio.data.Factory;
import org.stummi.factorio.data.Item;
import org.stummi.factorio.data.Recipe;
import org.stummi.factorio.data.Throughput;

public class Plan {
	final List<FactoryWithAmount> factories = new ArrayList<>();
	final List<Factory> factoriesToBalance = new ArrayList<>();
	
	final Map<Item, Throughput> goals = new HashMap<>();

	@Value
	static class FactoryWithAmount {
		Factory factory;
		double amount;
	}

	public void addReceipe(Recipe receipe, int count) {
		addFactory(new Factory(receipe), count);
	}

	public void addReceipe(Recipe receipe) {
		addFactory(new Factory(receipe), 1);
	}

	public void addFactory(Factory factory) {
		addFactory(factory, 1);
	}

	public void addFactory(Factory factory, double count) {
		factories.add(new FactoryWithAmount(factory, count));
	}

	public Report getUnbalancedReport() {
		Report unbalancedReport = new Report();
		for (FactoryWithAmount fact : factories) {
			unbalancedReport.addFactory(fact.getFactory(), fact.getAmount());
		}
		return unbalancedReport;
	}
	
	public Report getBalancedReport() {
		Report report = getUnbalancedReport();
		Balancer balancer = new Balancer(report, goals, factoriesToBalance);
		balancer.balance();
		
		return report;
	}

	public void addFactoryToBalance(Factory fact) {
		factoriesToBalance.add(fact);
	}

	public void addGoal(Item item, Throughput throughput) {
		Throughput curGoal = goals.getOrDefault(item, Throughput.perMinute(0));
		curGoal = curGoal.add(throughput);
		goals.put(item, curGoal);
	}
	
	
}
