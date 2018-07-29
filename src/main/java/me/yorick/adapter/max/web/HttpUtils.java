package me.yorick.adapter.max.web;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

import okhttp3.OkHttpClient;

public class HttpUtils {

	private static OkHttpClient client = new OkHttpClient.Builder()
            .build();
	
	public static OkHttpClient getClient() {
		return client;
	}
	
	public static String encrypt(String secret, String payload) throws Exception {
		byte[] hmac = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, secret).hmac(payload);
		return Hex.encodeHexString(hmac);
	}
}
