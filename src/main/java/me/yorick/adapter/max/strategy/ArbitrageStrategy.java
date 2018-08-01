package me.yorick.adapter.max.strategy;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.yorick.adapter.max.engine.Engine;
import me.yorick.adapter.max.type.MarketBookSnapshot;
import me.yorick.adapter.max.type.Product;
import me.yorick.adapter.max.types.MarketBook;

public class ArbitrageStrategy implements Strategy{

	private Logger logger;
	private final Engine engine;
	private final HashMap<String, MarketBookSnapshot> snapshots;
	private final TradingComposition composition;
	private String a;
	private String b;
	private String c;

	public ArbitrageStrategy(final Engine engine, final TradingComposition composition, final HashMap<String, MarketBookSnapshot> snapshots, final Logger logger) {
		this.engine = engine;
		this.snapshots = snapshots;
		this.composition=composition;
		this.logger=logger;

		a = composition.getFirst().getId();
		b = composition.getSecond().getId();
		c = composition.getThird().getId();
	}

	@Override
	public void update() throws Exception {
		test(a, b, c);
		test(c, b, a);
	}

	private void test(String a1, String b1 , String c1) {
		MarketBook abook= snapshots.get(a1).getBook();
		MarketBook bbook= snapshots.get(b1).getBook();
		MarketBook cbook= snapshots.get(c1).getBook();

		double aAsk = abook.getAsks()[0].getPrice();
		double bBid = bbook.getBids()[0].getPrice();
		double cAsk = cbook.getAsks()[0].getPrice();

		double rate = bBid*cAsk/aAsk;
		if(rate>1)
			logger.info("Rate:{} {}:{} {}:{} {};{}", rate, a1,aAsk, b1,bBid, c1, cAsk);


	}

}
