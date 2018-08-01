package me.yorick.adapter.max.types;

public class MarketBook {

	private LevelInfo[] asks;
	private LevelInfo[] bids;
	
	public boolean isAsksValid() {
		return asks!=null && asks.length>0;
	}
	
	public boolean isBidsValid() {
		return bids!=null && bids.length>0;
	}
	
	public LevelInfo[] getAsks() {
		return asks;
	}
	public void setAsks(LevelInfo[] asks) {
		this.asks = asks;
	}
	public LevelInfo[] getBids() {
		return bids;
	}
	public void setBids(LevelInfo[] bids) {
		this.bids = bids;
	}
	
}
