package com.jaredjstewart;

import com.gemstone.gemfire.cache.Region;

import java.util.List;
import java.util.Random;


public class RandomStockPriceGenerator implements Runnable {
    private Region<String, String> region;
    private List<String> tickerSymbols;
    private Random random = new Random();

    public RandomStockPriceGenerator(Region<String, String> region, List<String> tickerSymbols) {
        this.region = region;
        this.tickerSymbols = tickerSymbols;
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
