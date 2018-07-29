package me.yorick.adapter.max.message;

public class SubscribeMessage {

	private String cmd="subscribe";
	private String channel="orderbook";
	private ParamMarketMessage params = new ParamMarketMessage();
	
	public SubscribeMessage(String product) {
		params.setMarket(product);
	}
	
	public String getCmd() {
		return cmd;
	}
	
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	
	public String getChannel() {
		return channel;
	}
	
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	public ParamMarketMessage getParams() {
		return params;
	}
	public void setParams(ParamMarketMessage params) {
		this.params = params;
	}
	
}
