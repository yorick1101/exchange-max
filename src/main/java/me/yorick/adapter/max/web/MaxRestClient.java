package me.yorick.adapter.max.web;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;

import me.yorick.adapter.max.Side;
import me.yorick.adapter.max.message.OrderRequestBody;
import me.yorick.adapter.max.type.MarketBookSnapshot;
import me.yorick.adapter.max.types.LevelInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

public class MaxRestClient extends RestClient {

	public MaxRestClient(String key, String secret) {
		super(key, secret);
	}

	public void getMe() throws Exception {
		HttpUrls eurl = HttpUrls.ME;
		Request request = authorizedGetBuilder(eurl.getPath()).url(eurl.getUrl()).build();
		
		HttpUtils.getClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onFailure(Call call, IOException e) {
				logger.error("can not send {}", call.request().url().toString(), e);
			}

			@Override
			public void onResponse(Call arg0, Response response) throws IOException {
				logger.info("me: {}", response.body().string());
			}
			
		});

	}
	
	public void getOrders(String market) throws Exception {
	
		HttpUrls eurl = HttpUrls.ORDERS;
		HttpUrl url = eurl.getUrlBuilder().addQueryParameter("market", market).build();
		logger.info(url.toString());
		Request request = authorizedGetBuilder(eurl.getPath()).url(url).build();
		
		HttpUtils.getClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onFailure(Call call, IOException e) {
				logger.error("can not send {}", call.request().url().toString(), e);
			}

			@Override
			public void onResponse(Call arg0, Response response) throws IOException {
				logger.info("orders({}): {}",market, response.body().string());
			}
			
		});
	}
	
	public void postOrder(String market, Side side, double price, double volume) throws Exception {
		HttpUrls eurl = HttpUrls.ORDERS;
		OrderRequestBody body = new OrderRequestBody();
		body.setMarket(market);
		body.setPath(eurl.getPath());
		body.setPrice(Double.toString(price));
		body.setSide(side.name());
		body.setVolume(Double.toString(volume));
		String strBody = JsonStream.serialize(body);
		
		Request request = authorizedPostBuilder(strBody).url(eurl.getUrl()).build();
		
		Response response = HttpUtils.getClient().newCall(request).execute();
		if(response.isSuccessful()) {
			logger.info(response.body().string());
		}else {
			logger.error("failed to create order {},{}",response.code(), response.body().string());
		}
	}

	public AtomicBoolean getDepth(String market, Map<Long, MarketBookSnapshot> snapshots) throws Exception {
		HttpUrls eurl = HttpUrls.DEPTH;
		HttpUrl url = eurl.getUrlBuilder().addQueryParameter("market", market).addQueryParameter("limit", "1").build();
		Request request = new Request.Builder().url(url).build();
		AtomicBoolean interrupt = new AtomicBoolean(false);
		
		HttpUtils.getClient().newCall(request).enqueue(new Callback() {

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if(response.isSuccessful()) {
					String market = response.body().string();
					//logger.info(market);
					Any root = JsonIterator.deserialize(market);
					LevelInfo[] asks=root.get("asks").asList().stream().map(a -> {String[] strs = a.as(String[].class); return new LevelInfo(Double.valueOf(strs[0]), Double.valueOf(strs[1])); }).toArray(size -> new LevelInfo[size]);
					LevelInfo[] bids=root.get("bids").asList().stream().map(a -> {String[] strs = a.as(String[].class); return new LevelInfo(Double.valueOf(strs[0]), Double.valueOf(strs[1])); }).toArray(size -> new LevelInfo[size]);
					
					//logger.info("asks/bids {}/{}",asks[0].getPrice(), bids[0].getPrice());
					for(MarketBookSnapshot snapshot : snapshots.values())
						snapshot.update(asks, bids);
					if(!interrupt.get())
						HttpUtils.getClient().newCall(request).enqueue(this);
				}else {
					logger.error("failed to get depth {},{}",response.code(), response.body().string());
				}		
			}
			
		});
		return interrupt;
	}
	
}
