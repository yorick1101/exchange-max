package me.yorick.adapter.max;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.yorick.adapter.max.engine.Engine;
import me.yorick.adapter.max.strategy.ArbitrageStrategyBuilder;

public class Application {
	private static Logger logger = LoggerFactory.getLogger(Application.class);
	public static void main(String[] args) throws Exception {

		Engine engine = new Engine(System.getenv("key"), System.getenv("secret"));
		ArbitrageStrategyBuilder builder = new ArbitrageStrategyBuilder(engine);
		builder.build();

	}


	

	

}
