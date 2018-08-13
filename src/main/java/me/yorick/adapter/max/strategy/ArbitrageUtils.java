package me.yorick.adapter.max.strategy;

import java.io.IOException;

public class ArbitrageUtils {

	private static ArbitrageRating createRatingBoard(TradingComposition composition) throws IOException {
		return new ArbitrageRating(composition);
	}
}
