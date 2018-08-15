package me.yorick.adapter.max;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.yorick.adapter.max.engine.Engine;
import me.yorick.adapter.max.gui.RootBoard;
import me.yorick.adapter.max.stragtegy.CompositionInfo;
import me.yorick.adapter.max.strategy.ArbitrageStrategyBuilder;
import me.yorick.adapter.max.strategy.EventListener;

public class Application {
	private static Logger logger = LoggerFactory.getLogger(Application.class);
	public static void main(String[] args) throws Exception {

		run();
		//testGUI();
	}

	private static void run() throws IOException {
		RootBoard board= new RootBoard();
		Engine engine = new Engine(System.getenv("key"), System.getenv("secret"));

		EventListener<CompositionInfo> listener = new EventListener<CompositionInfo>() {

			Map<Integer, CompositionInfo> chances = new ConcurrentHashMap<>();

			@Override
			public void onEvent(int id, CompositionInfo event) {
				chances.putIfAbsent(id, event);
				board.updateChance(id, event);
			}

			@Override
			public void removeEvent(int id) {
				if(chances.remove(id) != null) {
					board.remvoeChance(id);
				}

			}

		};

		ArbitrageStrategyBuilder builder = new ArbitrageStrategyBuilder(engine, listener);
		builder.build();

		board.setVisible(true);
	}


	private static void testGUI() {
		RootBoard board= new RootBoard();

		EventListener<CompositionInfo> listener = new EventListener<CompositionInfo>() {

			Map<Integer, CompositionInfo> chances = new ConcurrentHashMap<>();

			@Override
			public void onEvent(int id, CompositionInfo event) {
				chances.putIfAbsent(id, event);
				board.updateChance(id, event);
			}

			@Override
			public void removeEvent(int id) {
				if(chances.remove(id) != null) {
					board.remvoeChance(id);
				}

			}

		};

		listener.onEvent(42, create());


		new Thread() {
			@Override 
			public void run() {
				CompositionInfo info = create();
				while(true) {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					info.setRate(Math.random());
					listener.onEvent(32, info);
				}
			}

		}.start();




		board.setVisible(true);
	}
	private static CompositionInfo create() {
		CompositionInfo info = new CompositionInfo();
		info.setBase(0, Side.buy, "ethbtc");
		info.setBase(1, Side.buy, "ethltc");
		info.setBase(2, Side.sell, "ltcbtc");

		info.set(0, 1, 1.1);
		info.set(1, 2, 2.2);
		info.set(2, 3, 3.3);
		return info;
	}


}
