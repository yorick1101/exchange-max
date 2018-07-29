package me.yorick.adapter.max.web;

import okhttp3.HttpUrl;

public enum HttpUrls {

	ORDERS("orders"), ME("members/me"), DEPTH("depth");
	
	private HttpUrl.Builder urlBuilder;
	private String path;
	
	private HttpUrls(String url) {
		this.urlBuilder = new HttpUrl.Builder().scheme("https").host("max-api.maicoin.com")
				.addEncodedPathSegments("api/v2/")
				.addEncodedPathSegments(url);
		this.path = new StringBuilder().append("/api/v2/").append(url).toString();
	}
	
	public HttpUrl.Builder getUrlBuilder() {
		return urlBuilder;
	}
	
	public HttpUrl getUrl() {
		return urlBuilder.build();
	}

	public String getPath() {
		return path; 
	}
}
