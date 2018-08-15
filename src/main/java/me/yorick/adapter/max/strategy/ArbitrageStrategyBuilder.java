package me.yorick.adapter.max.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.yorick.adapter.max.engine.Engine;
import me.yorick.adapter.max.stragtegy.CompositionInfo;
import me.yorick.adapter.max.type.MarketBookSnapshot;
import me.yorick.adapter.max.type.Product;

public class ArbitrageStrategyBuilder {
	
	private final List<TradingComposition> tradingCompositions;
	private final Engine engine;
	private final ExecutorService executor;
	private final EventListener<CompositionInfo> listener;
	
	public ArbitrageStrategyBuilder(final Engine engine, final EventListener<CompositionInfo> listener) {
		this.engine = engine;
		this.listener = listener;
		tradingCompositions = findPairs();
		executor = Executors.newFixedThreadPool(tradingCompositions.size());
	}
	
	public void build() {
		int index = 1;
		HashMap<String, MarketBookSnapshot> snapshots = new HashMap<>();
		List<Strategy> strategies = new ArrayList<>();
		for(TradingComposition tradingComposition : tradingCompositions) {
			for(Product product : tradingComposition.getAll()) {
				snapshots.put(product.getId(), engine.addMarket(index, product.getId()));
			}
			Logger logger = LoggerFactory.getLogger("model-"+index);
			ArbitrageStrategy strategy = new ArbitrageStrategy(index, engine, listener, tradingComposition, snapshots, logger);
			strategies.add(strategy);
			index++;
		}
		StrategyWorker worker = new StrategyWorker(strategies, snapshots);
		executor.execute(worker);
		
	}


	private List<TradingComposition> findPairs() {
		Map<String, Product> idToProduct = engine.getProductManager().getProducts();
		List<TradingComposition> compositions = new ArrayList<>();
		for(Entry<String, List<Product>> entry: engine.getProductManager().getBaseCurrencyProducts().entrySet()) {
			List<Product> products = entry.getValue();
			for(int i =0;i< products.size()-1;i++) {
				for(int j =1;j< products.size();j++) {
					Product first = products.get(i);
					Product second = products.get(j);
					if(first.getQuote_unit().equals("twd") || second.getQuote_unit().equals("twd")) {
						Product third = findProduct(idToProduct, first.getQuote_unit(),second.getQuote_unit());
						if(third!=null) {
							//logger.info("composition: {} {} {}", first.getId(), second.getId(), third.getId());
							if(first.getQuote_unit().equals("twd"))
								compositions.add(new TradingComposition(first, second, third));
							else
								compositions.add(new TradingComposition(second, first, third));
						}
					}
				}
			}

		}
		return compositions;
	}

	private Product findProduct(Map<String, Product> idToProduct, String c1, String c2) {
		String tryId = new StringBuilder().append(c1).append(c2).toString();
		Product third = idToProduct.get(tryId);
		if(third==null) {
			tryId = new StringBuilder().append(c2).append(c1).toString();
			third = idToProduct.get(tryId);
		}
		return third;
	}

}
