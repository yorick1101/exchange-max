package me.yorick.adapter.max;

import me.yorick.adapter.max.web.HomeMarketEventWebSocketConnection;
import me.yorick.adapter.max.web.MaxRestClient;

public class Application {

	public static void main(String[] args) throws Exception {
		//PirvateWebSocketConnection conn = new PirvateWebSocketConnection(null,null);
		//conn.start();
		HomeMarketEventWebSocketConnection pconn = new HomeMarketEventWebSocketConnection();
		pconn.addProduct("ethtwd");
		//pconn.start();
		
		
		MaxRestClient client = new MaxRestClient(null,null);
		//client.getOrders("ethtwd");
		client.postOrder("ethtwd", Side.buy, 13900, 0.1);
	}

}
