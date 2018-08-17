package me.yorick.adapter.max.strategy;

import java.util.HashMap;

import org.slf4j.Logger;

import me.yorick.adapter.max.Side;
import me.yorick.adapter.max.engine.Engine;
import me.yorick.adapter.max.stragtegy.CompositionInfo;
import me.yorick.adapter.max.type.MarketBookSnapshot;
import me.yorick.adapter.max.types.MarketBook;

public class ArbitrageStrategy implements Strategy{

	private Logger logger;
	private static final double PROFIT = 1.1;
	private final Engine engine;
	private final EventListener<CompositionInfo> listener;
	private final HashMap<String, MarketBookSnapshot> snapshots;
	private String a;
	private String b;
	private String c;
	
	private CompositionInfo info1 = new CompositionInfo();
	private CompositionInfo info2 = new CompositionInfo();
	
	private final int id;
	private boolean hot = false;

	public ArbitrageStrategy(final int id, final Engine engine, final EventListener<CompositionInfo> listener, final TradingComposition composition, final HashMap<String, MarketBookSnapshot> snapshots, final Logger logger) {
		this.id = id;
		this.engine = engine;
		this.listener = listener;
		this.snapshots = snapshots;
		this.logger=logger;

		a = composition.getFirst().getId();
		b = composition.getSecond().getId();
		c = composition.getThird().getId();
		
		//for test 1
		info1.setBase(0, Side.buy, a);
		info1.setBase(1, Side.sell, b);
		info1.setBase(2, Side.sell, c);
		
		//for test 2
		info2.setBase(0, Side.buy, c);
		info2.setBase(1, Side.buy, b);
		info2.setBase(2, Side.sell, a);
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
		if(rate>PROFIT) {
			double aQty = aAskV;
			double bQty = aQty/bAsk;
			if(bAskV < bQty) {
				bQty = bAskV;
				aQty = bQty*bAsk;
			}
				
			double cQty;
			if(bQty <= cBidV) {
				cQty = bQty; 
			}else {
				//not enough for c
				cQty = cBidV;
				bQty = cQty;
				aQty = bQty*bAsk;
			}
			
			logger.info("Rate2:{} BUY{}:{}@{}@{} BUY{}:{}@{}@{} SELL{};{}@{}@{}", rate, a1,aAsk,aQty,aAskV, b1,bAsk,bQty,bAskV, c1, cBid,cQty, cBidV);
			info1.set(0, aAsk, aQty);
			info1.set(1, bAsk, bQty);
			info1.set(2, cBid, cQty);
			info1.setRate(rate);
			hot = true;
			listener.onEvent(id, info1);
		}else {
			if(hot==true) listener.removeEvent(id);
			hot= false;
		}
			
			
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
		if(rate>PROFIT) {
			double aQty = aAskV;
			double bQty = aQty;
			if(bBidV < bQty) {
				bQty = bBidV;
				aQty = bQty;
			}
			
			double cQty = bQty * bBid;
			if(cBidV < cQty) {
				cQty = cBidV;
				bQty = cQty/bBid;
				aQty = bQty;
			}
			logger.info("Rate1:{} BUY{}:{}@{}@{} SELL{}:{}@{}@{} SELL{};{}@{}@{}", rate, a1, aAsk, aQty, aAskV, b1, bBid, bQty, bBidV, c1, cBid, cQty, cBidV);	
			info2.set(0, aAsk, aQty);
			info2.set(1, bBid, bQty);
			info2.set(2, cBid, cQty);
			info2.setRate(rate);
			listener.onEvent(id, info2);
			hot = true;
		}else {
			if(hot==true) listener.removeEvent(id);
			hot= false;
		}
	}

}
