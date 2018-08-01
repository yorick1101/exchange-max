package me.yorick.adapter.max;

import me.yorick.adapter.max.engine.Engine;
import me.yorick.adapter.max.strategy.ArbitrageStrategyBuilder;

public class Application {
	//private static Logger logger = LoggerFactory.getLogger(Application.class);
	public static void main(String[] args) throws Exception {

		Engine engine = new Engine(null, null);
		ArbitrageStrategyBuilder builder = new ArbitrageStrategyBuilder(engine);
		builder.build();
	}

}
