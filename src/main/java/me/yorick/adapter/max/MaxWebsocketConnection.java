package me.yorick.adapter.max;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;

import me.yorick.adapter.max.message.ChallengeResponseMessage;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class MaxWebsocketConnection {

	private enum Status{ Authoriazed, Authorizing, New, UnAuthoriazed};
	private String wsUrl = "wss://max-ws.maicoin.com";
	private WebSocket ws;
	private final String accessKey;
	private final String secret;
	protected Status status  = Status.New;

	public MaxWebsocketConnection(final String accessKey, final String secret) {
		this.accessKey = accessKey;
		this.secret=secret;
	}

	public void start() {
		Request request = new Request.Builder()
				.url(wsUrl)
				.build();

		HttpUtils.getClient().newWebSocket(request, new WebSocketListener() {

			@Override
			public void onOpen(WebSocket webSocket, Response response) {
				ws = webSocket;
				System.out.println("client onOpen");
				System.out.println("client request header:" + response.request().headers());
				System.out.println("client response header:" + response.headers());
				System.out.println("client response:" + response);
				//开启消息定时发送
			}

			@Override
			public void onMessage(WebSocket webSocket, String text) {
				System.out.println("client onMessage");
				System.out.println("message:" + text);

				Any a =JsonIterator.deserialize(text);
				if(Status.Authoriazed == status) {

				}else if(Status.Authorizing == status){
					checkAuthentication(a);
				}else {
					try {
						authentication(a);
						status = Status.Authorizing;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}


			@Override
			public void onClosing(WebSocket webSocket, int code, String reason) {
				System.out.println("client onClosing");
				System.out.println("code:" + code + " reason:" + reason);
			}
			@Override
			public void onClosed(WebSocket webSocket, int code, String reason) {
				System.out.println("client onClosed");
				System.out.println("code:" + code + " reason:" + reason);
			}
			@Override
			public void onFailure(WebSocket webSocket, Throwable t, Response response) {
				System.out.println("client onFailure");
				System.out.println("throwable:" + t);
				System.out.println("response:" + response); 
			}
		});
	}


	private void authentication(Any any) throws Exception {
		String msg = any.get("msg").toString();
		System.out.println("msg:"+msg);
		StringBuilder payload = new StringBuilder();
		payload.append(accessKey).append(msg);
		String epayload = encrypt(payload.toString());
		ChallengeResponseMessage response = new ChallengeResponseMessage(accessKey);
		response.setAnswer(epayload);
		String s = JsonStream.serialize(response);
		System.out.println(s);
		ws.send(s);

	}

	private String encrypt(String payload) throws Exception {
		byte[] hmac = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, secret).hmac(payload);

		return Hex.encodeHexString(hmac);
	}

	private void checkAuthentication(Any a) {
		Any info = a.get("info");
		if(info!=null && info.toString().equals("authenticated")) {
			status = Status.Authoriazed;
		}else 
			status = Status.UnAuthoriazed;
	}
}
