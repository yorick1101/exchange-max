package me.yorick.adapter.max.engine;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.yorick.adapter.max.stragtegy.CompositionInfo;
import me.yorick.adapter.max.type.MarketBookSnapshot;
import me.yorick.adapter.max.web.MaxRestClient;
import me.yorick.adapter.max.web.PirvateWebSocketConnection;

public class Engine {

	private final Logger logger = LoggerFactory.getLogger(Engine.class);
	private final MaxRestClient restClient;
	private final PirvateWebSocketConnection wsConnection;
	private final Map<String, AtomicBoolean> marketReceiver=new HashMap<>();
	private final Map<String, Map<Long, MarketBookSnapshot>> marektbooks=new HashMap<>();
	private final ProductManager productMgr;

	public Engine(String apiKey, String secret) throws IOException {
		this.productMgr = new ProductManager();
		this.restClient = new MaxRestClient(apiKey, secret);
		this.wsConnection = new PirvateWebSocketConnection(apiKey, secret);
		//this.wsConnection.start();
	}

	public ProductManager getProductManager(){
		return productMgr;
	}

	public MarketBookSnapshot addMarket(long modelId, String market) {
		Map<Long, MarketBookSnapshot> cache = marektbooks.computeIfAbsent(market, k->{
			try {
				Map<Long, MarketBookSnapshot> newcache = new HashMap<>();
				AtomicBoolean interrupt = restClient.getDepth(market, newcache);
				marketReceiver.put(market, interrupt);
				return newcache;
			}catch(Exception e) {logger.error("failed to start market receiver {};{}",modelId, market,e);}
			return null;
		});
		MarketBookSnapshot snapshot = cache.get(modelId); 
		if(snapshot==null) {
			snapshot = new MarketBookSnapshot();
			cache.put(modelId, snapshot);	
		}

		return snapshot;
	}

	public MaxRestClient getMaxRestClient() {
		return restClient;
	}

}
