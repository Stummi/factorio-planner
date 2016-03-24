package org.stummi.factorio.data;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Factory {
	Receipe receipe = Receipe.NONE;
	AssemblingMachine type = AssemblingMachine.NONE;

	int count = 1;

	public Factory(AssemblingMachine type, Receipe receipe) {
		this.type = type;
		this.receipe = receipe;
	}

	public Factory(AssemblingMachine type) {
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

	public List<ItemThroughput> getResourceThroughputs() {
		return throughputs(receipe.getResources());
	}

	public List<ItemThroughput> getProductThroughputs() {
		return throughputs(receipe.getProducts());
	}

	private List<ItemThroughput> throughputs(List<ItemAmount> items) {
		return items.stream().map(res -> res.perTicks(receipe.getCycleTime()).multiply(count)).collect(Collectors.toList());
	}

}
