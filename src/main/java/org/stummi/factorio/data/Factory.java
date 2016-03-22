package org.stummi.factorio.data;

import java.util.List;
import java.util.stream.Collectors;

import org.stummi.factorio.Receipe;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Factory {
	Receipe receipe = Receipe.NONE;
	FactoryType type = FactoryType.NONE;

	int count = 1;

	public Factory(FactoryType type, Receipe receipe) {
		this.type = type;
		this.receipe = receipe;
	}

	public Factory(FactoryType type) {
		this.type = type;
	}

	public Factory(Receipe receipe) {
		this.receipe = receipe;
	}

	public void add(int inc) {
		count += inc;
	}

	public void remove(int dec) {
		count -= dec;
		if (count < 0) {
			count = 0;
		}
	}

	public void setCount(int newCount) {
		count = newCount < 0 ? 0 : newCount;
	}

	public List<ProductThroughput> getResourceThroughputs() {
		return throughputs(receipe.getResources());
	}

	public List<ProductThroughput> getProductThroughputs() {
		return throughputs(receipe.getProducts());
	}

	private List<ProductThroughput> throughputs(List<ProductAmount> products) {
		return products.stream().map(res -> res.perTicks(receipe.getCycleTime()).multiply(count)).collect(Collectors.toList());
	}

}
