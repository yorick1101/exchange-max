package me.yorick.adapter.max.message;

public class OrderRequestBody extends BaseRequestBody {

	private String market;
	private String ord_type="limit";
	private String side;
	private String price;
	private String volume;
	private String stop_price;
	public String getMarket() {
		return market;
	}
	public void setMarket(String market) {
		this.market = market;
	}
	public String getOrd_type() {
		return ord_type;
	}
	public void setOrd_type(String ord_type) {
		this.ord_type = ord_type;
	}
	public String getSide() {
		return side;
	}
	public void setSide(String side) {
		this.side = side;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public String getStop_price() {
		return stop_price;
	}
	public void setStop_price(String stop_price) {
		this.stop_price = stop_price;
	}
	
	
	
}
