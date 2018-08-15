package me.yorick.adapter.max.stragtegy;

import me.yorick.adapter.max.Side;

public class CompositionInfo {

	private TradeInfo[] levels = new TradeInfo[3];
	private double rate;
	
	public CompositionInfo() {
		for(int i =0;i<levels.length;i++) {
			levels[i] = new TradeInfo();
		}
	}
	public void set(final int index, final double price, final double qty) {
		levels[index].set(price, qty);
	}
	
	public double geRate() {
		return rate;
	}
	
	public void setRate(double rate) {
		this.rate = rate;
	}

	public TradeInfo[] getLevels() {
		return levels;
	}
	
	public void setBase(final int index, final Side side, final String product) {
		levels[index].setBase(side, product);
	}
		
	public static class TradeInfo{
		private Side side;
		private String product;
		private double price;
		private double qty;
		
		public void set(final double price, final double qty) {
			this.price = price;
			this.qty = qty;
		}
		
		public void setBase(final Side side, final String product) {
			this.side = side;
			this.product = product;
		}

		public Side getSide() {
			return side;
		}
		
		public String getProduct() {
			return product;
		}
		
		public double getPrice() {
			return price;
		}
		
		public double getQty() {
			return qty;
		}
		
	}

	
}

