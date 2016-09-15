package com.jaredjstewart;

import com.gemstone.gemfire.cache.Region;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class RandomStockPriceGenerator implements Runnable {
    private final List<String> tickerSymbols = Arrays.asList("AAPL", "AMZN", "DELL", "GOOG");
    private final Region<String, String> region;
    private final Random random = new Random();

    public RandomStockPriceGenerator(Region<String, String> region) {
        this.region = region;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {

            String tickerSymbol = getRandomTickerSymbol();
            region.put(tickerSymbol, getRandomPrice());
        }
    }

    private String getRandomPrice() {
        return String.valueOf(random.nextInt(1000));
    }

    private String getRandomTickerSymbol() {
        int index = random.nextInt(tickerSymbols.size());
        return tickerSymbols.get(index);
    }
}
