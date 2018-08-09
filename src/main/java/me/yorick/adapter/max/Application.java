package me.yorick.adapter.max;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.yorick.adapter.max.engine.Engine;
import me.yorick.adapter.max.strategy.ArbitrageStrategy;
import me.yorick.adapter.max.strategy.StrategyWorker;
import me.yorick.adapter.max.strategy.TradingComposition;
import me.yorick.adapter.max.type.MarketBookSnapshot;
import me.yorick.adapter.max.type.Product;
import me.yorick.adapter.max.web.MaxRestClient;

public class Application {
	private static Logger logger = LoggerFactory.getLogger(Application.class);
	public static void main(String[] args) throws Exception {

		/*

		ArbitrageStrategyBuilder builder = new ArbitrageStrategyBuilder(engine);
		builder.build();
		 */


		/*
		MaxRestClient client = new MaxRestClient("mxQiVDUa3jDpobKLWsGY6Mu2GWNsDLQEk8AaCruT", "0PFWjjBHZfxRtxjPU9JzVJELr8EG9EfPD9lpkEsp");

		while(true) {
			String orderId = client.postOrder("cccxeth", Side.buy, 0.00001080, 30000);
			//String orderId = client.postOrder("cccxeth", Side.sell, 0.00001100, 47.9);
			client.cancel(orderId);
			Thread.sleep(3000);
		}
		 */
		s(0.00001298);

	}



	private static void s(double price) throws IOException {
		int index = 1;
		Engine engine = new Engine(null, null);
		Map<String, Product> prodMgr = engine.getProductManager().getProducts();
		TradingComposition tradingComposition = new TradingComposition(prodMgr.get("cccxeth"), prodMgr.get("cccxtwd"), prodMgr.get("ethtwd"));

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
