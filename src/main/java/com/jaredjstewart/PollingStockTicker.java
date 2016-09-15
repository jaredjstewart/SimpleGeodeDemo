package com.jaredjstewart;

import com.gemstone.gemfire.cache.Region;

import java.util.Set;


public class PollingStockTicker implements Runnable {
    private Region<String, String> region;

    public PollingStockTicker(Region<String, String> region) {
        this.region = region;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            //TODO: This is a workaround to avoid calling .size() and .entrySet() due to [https://issues.apache.org/jira/browse/GEODE-1887]
            printPriceForEveryStock();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void printPriceForEveryStock() {
        Set<String> keySet = region.keySetOnServer();
        System.out.println("==============");

        for (String key : keySet) {
            System.out.format("Price for [%s] is %s\n", key, region.get(key));
        }
    }
}
