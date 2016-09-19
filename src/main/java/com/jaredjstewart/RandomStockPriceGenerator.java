package com.jaredjstewart;

import com.gemstone.gemfire.cache.Region;
import org.apache.commons.lang.time.StopWatch;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class RandomStockPriceGenerator implements Runnable {
    private static final long TWO_SECONDS = 2000L;
    private final List<String> tickerSymbols = Arrays.asList("AAPL", "AMZN", "DELL", "GOOG");
    private final Region<String, BigDecimal> region;
    private final Random random = new Random();

    public RandomStockPriceGenerator(Region<String, BigDecimal> region) {
        this.region = region;
    }

    @Override
    public void run() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int putCounter = 0;
        while (stopWatch.getTime() < TWO_SECONDS ) {
            String tickerSymbol = getRandomTickerSymbol();
            region.put(tickerSymbol, getRandomPrice());
            putCounter++;
        }
        stopWatch.stop();
        System.out.println("==============");
        System.out.println("RandomStockPriceGenerator made " + putCounter + " updates in the last two seconds");
    }

    private BigDecimal getRandomPrice() {
        int priceInCents =  random.nextInt(100000);
        BigDecimal stockPrice = BigDecimal.valueOf((double) priceInCents / 100);

        return stockPrice;
    }

    private String getRandomTickerSymbol() {
        int index = random.nextInt(tickerSymbols.size());
        return tickerSymbols.get(index);
    }
}
