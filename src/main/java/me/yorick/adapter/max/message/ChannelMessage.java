package me.yorick.adapter.max.message;

public class ChannelMessage {

	private static String channel_template = "market-%s-global";
	private String channel;
	
	public ChannelMessage(String market) {
		channel = String.format(channel_template, market);
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	
}
