package me.yorick.adapter.max.strategy;

import me.yorick.adapter.max.type.Product;

public class TradingComposition {

	private final Product first;
	private final Product second;
	private final Product third;
	private final Product[] all= new Product[3]; 
	
	public TradingComposition(Product first,Product second , Product third) {
		this.first=first; all[0]=first;
		this.second=second;all[1]=second;
		this.third=third;all[2]=third;
	}
	
	public Product getFirst() {
		return first;
	}
	
	public Product getSecond() {
		return second;
	}
	
	public Product getThird() {
		return third;
	}
	
	public Product[] getAll() {
		return all;
	}
	
}
