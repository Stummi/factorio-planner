package org.stummi.factorio.data;

import lombok.Value;

@Value
public class Product {
	String name;
	
	public ProductAmount amount(double d) {
		return new ProductAmount(this, d);
	}
	
	public ProductAmount once() {
		return amount(1);
	}
	
	public ProductAmount twice() {
		return amount(2);
	}
}
