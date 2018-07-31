package me.yorick.adapter.max.types;

public class LevelInfo {

	private double price;
	private double volume;
	
	public LevelInfo(double price , double volume) {
		this.price = price;
		this.volume = volume;
	}
	
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getVolume() {
		return volume;
	}
	public void setVolume(double volume) {
		this.volume = volume;
	}
	
	
}
