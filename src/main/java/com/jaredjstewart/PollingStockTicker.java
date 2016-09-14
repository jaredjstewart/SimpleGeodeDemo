package com.jaredjstewart;

import com.gemstone.gemfire.cache.Region;

import java.util.Collection;
import java.util.List;
import java.util.Random;


public class PollingStockTicker implements Runnable {
    private Region<String, String> region;
    private List<String> tickerSymbols;

    public PollingStockTicker(Region<String, String> region, List<String> tickerSymbols) {
        this.region = region;
        this.tickerSymbols = tickerSymbols;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            System.out.println("==============");
            for (String symbol : tickerSymbols) {
                System.out.format("Price for [%s] is %s\n", symbol, region.get(symbol));
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();  // set interrupt flag
            }
        }
    }
}
