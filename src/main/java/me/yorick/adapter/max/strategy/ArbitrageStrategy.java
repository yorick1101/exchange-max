package me.yorick.adapter.max.strategy;

import java.util.HashMap;

import org.slf4j.Logger;

import me.yorick.adapter.max.engine.Engine;
import me.yorick.adapter.max.type.MarketBookSnapshot;
import me.yorick.adapter.max.types.MarketBook;

public class ArbitrageStrategy implements Strategy{

	private Logger logger;
	private final Engine engine;
	private final HashMap<String, MarketBookSnapshot> snapshots;
	private String a;
	private String b;
	private String c;

	public ArbitrageStrategy(final Engine engine, final TradingComposition composition, final HashMap<String, MarketBookSnapshot> snapshots, final Logger logger) {
		this.engine = engine;
		this.snapshots = snapshots;
		this.logger=logger;

		a = composition.getFirst().getId();
		b = composition.getSecond().getId();
		c = composition.getThird().getId();
	}

	@Override
	public void update() throws Exception {
		test1(a, b, c);
		test2(c, b, a);
	}

	
	private void test2(String a1, String b1 , String c1) {
		MarketBook abook= snapshots.get(a1).getBook();
		MarketBook bbook= snapshots.get(b1).getBook();
		MarketBook cbook= snapshots.get(c1).getBook();

		if(!abook.isAsksValid() || !bbook.isAsksValid()||!cbook.isBidsValid())
			return;
		double aAsk = abook.getAsks()[0].getPrice();
		double aAskV = abook.getAsks()[0].getVolume();
		double bAsk = bbook.getAsks()[0].getPrice();
		double bAskV = bbook.getAsks()[0].getVolume();
		double cBid = cbook.getBids()[0].getPrice();
		double cBidV = cbook.getBids()[0].getVolume();

		double rate = cBid/(aAsk*bAsk);
		if(rate>1)
			logger.info("Rate:{} BUY{}:{}@{} BUY{}:{}@{} SELL{};{}@{}", rate, a1,aAsk,aAskV, b1,bAsk,bAskV, c1, cBid,cBidV);

	}
	
	private void test1(String a1, String b1 , String c1) {
		MarketBook abook= snapshots.get(a1).getBook();
		MarketBook bbook= snapshots.get(b1).getBook();
		MarketBook cbook= snapshots.get(c1).getBook();

		if(!abook.isAsksValid() || !bbook.isBidsValid()||!cbook.isBidsValid())
			return;
		double aAsk = abook.getAsks()[0].getPrice();
		double aAskV = abook.getAsks()[0].getVolume();
		double bBid = bbook.getBids()[0].getPrice();
		double bBidV = bbook.getBids()[0].getVolume();
		double cBid = cbook.getBids()[0].getPrice();
		double cBidV = cbook.getBids()[0].getVolume();

		double rate = (bBid*cBid)/aAsk;
		if(rate>1)
			logger.info("Rate:{} BUY{}:{}@{} SELL{}:{}@{} SELL{};{}@{}", rate, a1,aAsk,aAskV, b1,bBid,bBidV, c1, cBid,cBidV);


	}

}
