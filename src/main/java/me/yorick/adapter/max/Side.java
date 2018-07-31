package me.yorick.adapter.max;

public enum Side {
	sell(){

		public Side switchSide() {
			return buy;
		}
	},
	buy(){
		public Side switchSide() {
			return sell;
		}
	};


	public abstract Side switchSide();


}
