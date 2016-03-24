package org.stummi.factorio;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.stummi.factorio.data.Factory;
import org.stummi.factorio.data.Item;
import org.stummi.factorio.data.ItemThroughput;
import org.stummi.factorio.data.Throughput;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class Report {
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

	/*
	 * void addReceipe(Receipe receipe) { addReceipe(receipe, 1); }
	 * 
	 * public void addReceipe(Receipe receipe, Integer count) { for
	 * (ProductAmount amount : receipe.getProducts()) {
	 * getThroughput(amount.getProduct()).add(receipe.getCycleTime(), 1d * count
	 * * amount.getAmount()); }
	 * 
	 * for (ProductAmount amount : receipe.getResources()) {
	 * getThroughput(amount.getProduct()).remove(receipe.getCycleTime(), 1d *
	 * count * amount.getAmount()); }
	 * 
	 * }
	 */

	private Throughputs getThroughputs(Item product) {
		return throughputs.computeIfAbsent(product, Throughputs::new);
	}

	public void print(PrintStream pout) {
		String head = String.format("%30s|%8s|%8s|%8s%n", "PRODUCT", "IN", "OUT", "DIFF");
		pout.print(head);
		pout.print(head.replaceAll(".", "-"));

		throughputs.values().stream().sorted(Comparator.comparingDouble(tp -> (tp.inPerSecond - tp.outPerSecond))).forEach(tp -> {
			double in = tp.inPerSecond * 60;
			double out = tp.outPerSecond * 60;
			pout.printf("%30s|%8.2f|%8.2f|%8.2f%n", tp.product.getName(), in, out, out - in);
		});
	}

	public void addFactory(Factory fact) {
		for (ItemThroughput throughput : fact.getResourceThroughputs()) {
			getThroughputs(throughput.getItem()).remove(throughput.getThroughput());
		}

		for (ItemThroughput throughput : fact.getProductThroughputs()) {
			getThroughputs(throughput.getItem()).add(throughput.getThroughput());
		}
	}
}
