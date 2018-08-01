package me.yorick.adapter.max.strategy;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.yorick.adapter.max.type.MarketBookSnapshot;

public class StrategyWorker implements Runnable{

	private final Logger logger;
	private final Strategy strategy;
	private final Map<String, MarketBookSnapshot> snapshots;
	
	public StrategyWorker(long modelId, Strategy strategy, Map<String, MarketBookSnapshot> snapshots, Logger logger) {
		this.strategy = strategy;
		this.snapshots = snapshots;
		this.logger=logger;
		logger = LoggerFactory.getLogger("model-"+modelId);
	}

	private void swap() {
		for(MarketBookSnapshot snapshot : snapshots.values())
			snapshot.swap();
	}
	
	@Override
	public void run() {
		while(!Thread.currentThread().isInterrupted()) {
			try {
				swap();
				strategy.update();
			} catch (Exception e) {
				logger.error("failed to execute update",e);
			}
		}
	}
	
	
}
