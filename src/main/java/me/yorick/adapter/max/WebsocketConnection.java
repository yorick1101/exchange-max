package me.yorick.adapter.max;

import org.slf4j.Logger;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public abstract class WebsocketConnection {
	protected Logger logger = getLogger();
	protected WebSocket ws;
	
	private String wsUrl = "wss://max-ws.maicoin.com";
    
	public void start() {
		Request request = new Request.Builder()
				.url(wsUrl)
				.build();

		HttpUtils.getClient().newWebSocket(request, new WebSocketListener() {
			@Override
			public void onOpen(WebSocket webSocket, Response response) {
				logger.info("client onOpen");
				logger.info("client request header:" + response.request().headers());
				logger.info("client response header:" + response.headers());
				logger.info("client response:" + response);
				ws=webSocket;
			}

			@Override
			public void onMessage(WebSocket webSocket, String text) {
				logger.info("client onMessage");
				logger.info("message:{}", text);
			}


			@Override
			public void onClosing(WebSocket webSocket, int code, String reason) {
				logger.info("client onClosing, code:{} reason:{}" ,code , reason);
			}
			@Override
			public void onClosed(WebSocket webSocket, int code, String reason) {
				logger.info("client onClosed, code:{} reason:{}" ,code , reason);
			}
			@Override
			public void onFailure(WebSocket webSocket, Throwable t, Response response) {
				logger.info("client onFailure");
				logger.info("throwable:" + t);
				logger.info("response:" + response); 
			}
		});
	}
	
	abstract void afterOpen();
	abstract void onMessage(String text);
	abstract Logger getLogger();

}
