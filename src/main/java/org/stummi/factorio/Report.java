package org.stummi.factorio;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;

public class Report {
	@RequiredArgsConstructor
	static class Throughput {
		private final Product procut;
		double inPerSecond;
		double outPerSecond;

		void add(int cycleTime, double amount) {
			inPerSecond += amount * 60D / cycleTime;
		}

		void remove(int cycleTime, double amount) {
			outPerSecond += amount * 60D / cycleTime;
		}
	}

	final Map<Product, Throughput> throughputs = new HashMap<>();

	void addReceipe(Receipe receipe) {
		addReceipe(receipe, 1);
	}

	public void addReceipe(Receipe receipe, Integer count) {
		for (ProductAmount amount : receipe.getProducts()) {
			getThroughput(amount.getProduct()).add(receipe.getCycleTime(),
					1d * count * amount.getAmount());
		}

		for (ProductAmount amount : receipe.getResources()) {
			getThroughput(amount.getProduct()).remove(receipe.getCycleTime(),
					1d * count * amount.getAmount());
		}

	}

	private Throughput getThroughput(Product product) {
		return throughputs.computeIfAbsent(product, Throughput::new);
	}

	public void print(PrintStream pout) {
		String head = String.format("%30s|%8s|%8s|%8s%n", "PRODUCT", "IN", "OUT", "DIFF");
		pout.print(head);
		pout.print(head.replaceAll(".", "-"));
		
		throughputs
				.values()
				.stream()
				.sorted(Comparator.comparingDouble(tp -> (tp.inPerSecond-tp.outPerSecond)))
				.forEach(
						tp -> {
							double in = tp.inPerSecond * 60;
							double out = tp.outPerSecond * 60;
							pout.printf("%30s|%8.2f|%8.2f|%8.2f%n",
									tp.procut.getName(), in, out, out - in);
						});
	}
}
