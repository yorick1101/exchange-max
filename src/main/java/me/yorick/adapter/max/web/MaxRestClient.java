package me.yorick.adapter.max.web;

import java.io.IOException;

import com.jsoniter.output.JsonStream;

import me.yorick.adapter.max.Side;
import me.yorick.adapter.max.message.OrderRequestBody;
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

	
}
