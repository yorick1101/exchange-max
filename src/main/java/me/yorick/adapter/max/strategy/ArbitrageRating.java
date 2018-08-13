package me.yorick.adapter.max.strategy;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import me.yorick.adapter.max.engine.Engine;
import me.yorick.adapter.max.type.MarketBookSnapshot;
import me.yorick.adapter.max.type.Product;

public class ArbitrageRating {

	private TradingComposition tradingComposition;
	
	public ArbitrageRating(final TradingComposition tradingComposition) {
		this.tradingComposition= tradingComposition;
	}
	
	public void start(double price) throws IOException {
		int index = 1;
		Engine engine = new Engine(null, null);
		
		Map<String, Product> prodMgr = engine.getProductManager().getProducts();
		HashMap<String, MarketBookSnapshot> snapshots = new HashMap<>();
		for(Product product : tradingComposition.getAll()) {
			snapshots.put(product.getId(), engine.addMarket(index, product.getId()));
		}

		Executors.newSingleThreadExecutor().execute(new Runnable() {

			private MarketBookSnapshot a = snapshots.get(tradingComposition.getFirst().getId());
			private MarketBookSnapshot b = snapshots.get(tradingComposition.getSecond().getId());
			private MarketBookSnapshot c = snapshots.get(tradingComposition.getThird().getId());
			
			@Override
			public void run() {
				while(true) {
					a.swap();b.swap();c.swap();
					if(b.getBook().getBids()!=null && c.getBook().getAsks()!=null) {
						double rate = b.getBook().getBids()[0].getPrice()/(price*c.getBook().getAsks()[0].getPrice());
						System.out.println(rate);
					}
				}
			}

		});
	}
}
