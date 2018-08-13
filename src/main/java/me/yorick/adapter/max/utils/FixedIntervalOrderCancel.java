package me.yorick.adapter.max.utils;

import me.yorick.adapter.max.Side;
import me.yorick.adapter.max.engine.Engine;

public class FixedIntervalOrderCancel {

	private final Engine engine;
	private long interval;

	public FixedIntervalOrderCancel(Engine engine) {
		this.engine = engine;

	}
	
	public void setInterval(long interval) {
		this.interval=interval;
	}

	public void start(String market, Side side, double price, double qty) throws Exception {

		while(true) {
			String orderId = engine.getMaxRestClient().postOrder(market, side, price, qty);
			engine.getMaxRestClient().cancel(orderId);
			Thread.sleep(interval);
		}
	}
}
