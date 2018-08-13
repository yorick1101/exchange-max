package me.yorick.adapter.max.strategy;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.yorick.adapter.max.type.MarketBookSnapshot;

public class StrategyWorker implements Runnable{

	private final Logger logger;
	private final List<Strategy> strategies;
	private final Map<String, MarketBookSnapshot> snapshots;

	public StrategyWorker(List<Strategy> strategies, Map<String, MarketBookSnapshot> snapshots) {
		this.strategies = strategies;
		this.snapshots = snapshots;
		logger = LoggerFactory.getLogger("StrategyWorker");
	}

	private void swap() {
		for(MarketBookSnapshot snapshot : snapshots.values())
			snapshot.swap();
	}

	@Override
	public void run() {
		while(!Thread.currentThread().isInterrupted()) {
			swap();
			strategies.stream().forEach(s -> {
				try {
					s.update();
				} catch (Exception e) {
					logger.error("failed to execute update for model", e);
				}
			});
		}
	}


}
