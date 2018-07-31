package me.yorick.adapter.max.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.yorick.adapter.max.type.MarketBookSnapshot;
import me.yorick.adapter.max.web.MaxRestClient;
import me.yorick.adapter.max.web.PirvateWebSocketConnection;

public class Engine {

	private Logger logger = LoggerFactory.getLogger(Engine.class);
	private MaxRestClient restClient;
	private PirvateWebSocketConnection wsConnection;
	private Map<String, AtomicBoolean> marketReceiver=new HashMap<>();
	private Map<String, MarketBookSnapshot> marektbooks=new HashMap<>();
	
	public Engine(String apiKey, String secret) {
		restClient = new MaxRestClient(apiKey, secret);
		wsConnection = new PirvateWebSocketConnection(apiKey, secret);
		
	}
	
	public void addMarket(String market) {
		try {
			MarketBookSnapshot snapshot = new MarketBookSnapshot();
			marektbooks.put(market, snapshot);
			AtomicBoolean interrupt = restClient.getDepth(market, snapshot);
			marketReceiver.put(market, interrupt);
		} catch (Exception e) {
			logger.error("failed to start market receiver",e);
		}
	}
	
}
