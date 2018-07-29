package me.yorick.adapter.max.web;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;

import me.yorick.adapter.max.message.EventMessage;

public class HomeMarketEventWebSocketConnection extends WebSocketConnection{

	private Logger logger = LoggerFactory.getLogger(HomeMarketEventWebSocketConnection.class);
	
	private Set<String> products = new HashSet<>();
	private AtomicBoolean connected = new AtomicBoolean(false);
	
	public void addProduct(String product) {
		products.add(product);
	}
	
	public HomeMarketEventWebSocketConnection() {
		super.setWsUrl("wss://io.maicoin.com:8080/app/jb10W3zIxR98blDAg8BWUalZzLa6U5T5?protocol=7&client=js&version=4.2.1&flash=false");
	}
	
	@Override
	void afterOpen() {
	
	}

	@Override
	void onMessage(String text) {
		System.out.println(text);
		String event = JsonIterator.deserialize(text).get("event").toString();
		System.out.println(event);
		if(!connected.get()&&"pusher:connection_established".equals(event)) {
			logger.info("connected, subscribe products");
			connected.set(true);
			for(String product: products) {
				EventMessage msg = new EventMessage(product);
				ws.send(JsonStream.serialize(msg));
			}
		}
    }

	@Override
	Logger getLogger() {
		return logger;
	}

}
