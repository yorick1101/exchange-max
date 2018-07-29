package me.yorick.adapter.max.message;

public class BaseRequestBody {

	private String path;
	private long nonce = System.currentTimeMillis();
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public long getNonce() {
		return nonce;
	}
	public void setNonce(long nonce) {
		this.nonce = nonce;
	}
	
	
}
