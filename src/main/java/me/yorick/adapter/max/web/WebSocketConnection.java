package me.yorick.adapter.max.web;

import org.slf4j.Logger;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public abstract class WebSocketConnection {
	
	protected WebSocket ws;
	
	private String wsUrl = "wss://max-ws.maicoin.com";
	
	protected void setWsUrl(String url) {
		this.wsUrl=url;
	}
	
    
	public void start() {
		Request request = new Request.Builder()
				.url(wsUrl)
				.build();

		HttpUtils.getClient().newWebSocket(request, new WebSocketListener() {
			@Override
			public void onOpen(WebSocket webSocket, Response response) {
				getLogger().info("client onOpen");
				getLogger().info("client request header:" + response.request().headers());
				getLogger().info("client response header:" + response.headers());
				getLogger().info("client response:" + response);
				ws=webSocket;
			}

			@Override
			public void onMessage(WebSocket webSocket, String text) {
				getLogger().info("client onMessage");
				getLogger().info("message:{}", text);
				WebSocketConnection.this.onMessage(text);
			}


			@Override
			public void onClosing(WebSocket webSocket, int code, String reason) {
				getLogger().info("client onClosing, code:{} reason:{}" ,code , reason);
			}
			@Override
			public void onClosed(WebSocket webSocket, int code, String reason) {
				getLogger().info("client onClosed, code:{} reason:{}" ,code , reason);
			}
			@Override
			public void onFailure(WebSocket webSocket, Throwable t, Response response) {
				getLogger().info("client onFailure");
				getLogger().info("throwable:" + t);
				getLogger().info("response:" + response); 
			}
		});
	}
	
	abstract void afterOpen();
	abstract void onMessage(String text);
	abstract Logger getLogger();

}
