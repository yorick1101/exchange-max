package me.yorick.adapter.max.message;

public class EventMessage {

	private String event="pusher:subscribe";
	private ChannelMessage data;
	
	public EventMessage(String market) {
		data=new ChannelMessage(market);
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public ChannelMessage getData() {
		return data;
	}

	public void setData(ChannelMessage data) {
		this.data = data;
	}
	
	
}
