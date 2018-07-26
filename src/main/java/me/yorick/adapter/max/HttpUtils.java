package me.yorick.adapter.max;

import okhttp3.OkHttpClient;

public class HttpUtils {

	private static OkHttpClient client = new OkHttpClient.Builder()
            .build();
	
	public static OkHttpClient getClient() {
		return client;
	}
}
