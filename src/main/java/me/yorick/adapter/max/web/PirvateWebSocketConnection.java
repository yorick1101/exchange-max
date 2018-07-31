package me.yorick.adapter.max.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;

import me.yorick.adapter.max.Side;
import me.yorick.adapter.max.message.ChallengeResponseMessage;

public class PirvateWebSocketConnection extends WebSocketConnection{

	private Logger logger = LoggerFactory.getLogger(PirvateWebSocketConnection.class);
	private enum Status{ Authoriazed, Authorizing, New, UnAuthoriazed};
	private final String accessKey;
	private final String secret;
	protected Status status  = Status.New;

	public PirvateWebSocketConnection(final String accessKey, final String secret) {
		this.accessKey = accessKey;
		this.secret=secret;
	}


	private void authentication(Any any) throws Exception {
		String msg = any.get("msg").toString();
		StringBuilder payload = new StringBuilder();
		payload.append(accessKey).append(msg);
		String epayload = HttpUtils.encrypt(secret, payload.toString());
		ChallengeResponseMessage response = new ChallengeResponseMessage(accessKey);
		response.setAnswer(epayload);
		ws.send(JsonStream.serialize(response));

	}

	private void checkAuthentication(Any a) {
		Any info = a.get("info");
		if(info!=null && info.toString().equals("authenticated")) {
			status = Status.Authoriazed;
		}else 
			status = Status.UnAuthoriazed;
	}

	private void processFill(Any a) {
		Any trade = a.get("trade");
		String tradeId = trade.get("id").toString();
		double price = trade.get("price").toDouble();
		Side side = Side.valueOf(trade.get("side").toString());
		String market = trade.get("market").toString();
		String volume = trade.get("volume").toString();
		boolean isTaker = false;
		Any order = trade.get(side);
		if(order == null) {
			order = trade.get(side.switchSide());
			isTaker = true;
		}
		String orderId = order.get("id").toString();
		String orderSide = order.get("side").toString();
		String state = order.get("state").toString();
		double remaining_volume = order.get("remaining_volume").toDouble();
		
		logger.info("tradeId:{};market:{};side:{};price:{};volume:{};orderId:{};orderSide:{};state:{};remaining_volume:{}", tradeId, market, side, price, volume, orderId, orderSide, state, remaining_volume);
	}
	
	@Override
	void afterOpen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	void onMessage(String text) {
		Any a =JsonIterator.deserialize(text);
		if(Status.Authoriazed == status) {
			if(a.get("reason").toString().equals("trade")) {
				processFill(a);
			}
		}else if(Status.Authorizing == status){
			checkAuthentication(a);
		}else {
			try {
				authentication(a);
				status = Status.Authorizing;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	@Override
	Logger getLogger() {
		return logger;
	}
}
