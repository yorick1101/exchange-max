package me.yorick.adapter.max.web;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jsoniter.output.JsonStream;

import me.yorick.adapter.max.message.BaseRequestBody;
import okhttp3.MediaType;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;

public class RestClient {

	protected static final Logger logger = LoggerFactory.getLogger(RestClient.class);
	protected static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	private String key;
	private String secret;

	public RestClient(String key, String secret) {
		this.key = key;
		this.secret = secret;
	}

	protected Builder buildPrivateRequest(String body) throws Exception {
		String ebody = Base64.encodeBase64String(body.getBytes());
		Builder builder = new Builder();
		builder.addHeader("X-MAX-ACCESSKEY", key);
		builder.addHeader("X-MAX-PAYLOAD", ebody);
		builder.addHeader("X-MAX-SIGNATURE", HttpUtils.encrypt(secret, ebody));

		return builder;
	}

	protected Builder authorizedGetBuilder(String path) throws Exception {
		BaseRequestBody rbody = new BaseRequestBody();
		rbody.setNonce(System.currentTimeMillis());
		rbody.setPath(path);
		String body = JsonStream.serialize(rbody);
		return buildPrivateRequest(body).get();
	}
	
	protected Builder authorizedPostBuilder(String body) throws Exception {
		return buildPrivateRequest(body).post(RequestBody.create(JSON, body));
	}


}
