package org.stummi.factorio;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.stummi.factorio.data.Factory;
import org.stummi.factorio.data.Item;
import org.stummi.factorio.data.ItemThroughput;
import org.stummi.factorio.data.Throughput;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class Report implements Cloneable {
	@RequiredArgsConstructor
	@Getter
	public static class Throughputs {
		private final Item product;
		double inPerSecond;
		double outPerSecond;

		void add(Throughput throughput) {
			outPerSecond += throughput.amountPerSecond();
		}

		void remove(Throughput throughput) {
			inPerSecond += throughput.amountPerSecond();
		}

		public double getDiffPerSecond() {
			return outPerSecond - inPerSecond;
		}
	}

	@Getter
	final Map<Item, Throughputs> throughputs = new HashMap<>();
	private Map<Factory, Double> balancedFactories;

	public Throughputs getThroughputs(Item product) {
		return throughputs.computeIfAbsent(product, Throughputs::new);
	}

	public void print(PrintStream pout) {
		pintThroughputs(pout);
		printBalancedFactories(pout);
	}

	private void printBalancedFactories(PrintStream pout) {
		int typeColLen = balancedFactories.keySet().stream()
				.map(f -> f.getType().getName().toString())
				.mapToInt(String::length).max().orElse(8);

		int recipeColLen = balancedFactories.keySet().stream()
				.map(f -> f.getRecipe().getName().toString())
				.mapToInt(String::length).max().orElse(8);

		String head = String.format("%"+typeColLen+"s | %"+recipeColLen+"s | %10s%n", "Machine", "Recipe",
				"Count");
		pout.print(head);
		pout.print(head.replaceAll(".", "-"));

		Comparator<? super Entry<Factory, Double>> comp = Comparator
				.comparing(e -> e.getKey().getRecipe().getName());
		balancedFactories
				.entrySet()
				.stream()
				.sorted(comp)
				.forEach(
						e -> {
							Factory f = e.getKey();
							Double amount = e.getValue();
							pout.printf("%"+typeColLen+"s | %"+recipeColLen+"s | %10.2f%n", f.getType()
									.getName(), f.getRecipe().getName(), amount);
						});

		pout.println();
		pout.println();
	}

	// Quick and Dirty formatting
	private void pintThroughputs(PrintStream pout) {
		int nameColLen = throughputs.keySet().stream().map(Item::getName)
				.mapToInt(String::length).max().orElse(8);
		String head = String.format("%" + nameColLen + "s |%8s | %8s | %8s%n",
				"PRODUCT", "IN", "OUT", "DIFF");
		pout.print(head);
		pout.print(head.replaceAll(".", "-"));

		throughputs
				.values()
				.stream()
				.sorted(Comparator
						.comparingDouble(tp -> (tp.inPerSecond - tp.outPerSecond)))
				.forEach(
						tp -> {
							double in = tp.inPerSecond * 60;
							double out = tp.outPerSecond * 60;
							pout.printf("%" + nameColLen
									+ "s | %8.2f | %8.2f | %8.2f%n",
									tp.product.getName(), in, out, out - in);
						});
		pout.println();
		pout.println();
	}

	public void addFactory(Factory fact, double count) {
		for (ItemThroughput throughput : fact.getResourceThroughputs()) {
			getThroughputs(throughput.getItem()).remove(
					throughput.getThroughput().multiply(count));
		}

		for (ItemThroughput throughput : fact.getProductThroughputs()) {
			getThroughputs(throughput.getItem()).add(
					throughput.getThroughput().multiply(count));
		}
	}

	public void setBalancedFactoriesCount(Map<Factory, Double> factories) {
		this.balancedFactories = factories;
	}

	@Override
	public Report clone() {
		Report ret = new Report();
		ret.throughputs.putAll(throughputs);
		return ret;
	}

	public Map<Factory, Double> getBalancedFactories() {
		return balancedFactories;
	}

}
