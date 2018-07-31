package me.yorick.adapter.max.type;

import me.yorick.adapter.max.types.LevelInfo;
import me.yorick.adapter.max.types.MarketBook;

public class MarketBookSnapshot {
	
	private MarketBook current;
	private MarketBook update;

	public synchronized void update(LevelInfo[] asks, LevelInfo[] bids) {
		update.setAsks(asks);
		update.setBids(bids);
	}
	
	public synchronized void swap() {
		MarketBook temp = update;
		update = current;
		current = temp;
	}
	
	public MarketBook getBook() {
		return current;
	}

}
