package com.jaredjstewart;

import com.gemstone.gemfire.cache.Region;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;


public class PollingStockTicker implements Runnable {
    private Region<String, String> region;
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public PollingStockTicker(Region<String, String> region) {
        this.region = region;
    }

    @Override
    public void run() {
            printPriceForEveryStock();
    }

    private void printPriceForEveryStock() {
        //TODO: This is a workaround to avoid calling .size() and .entrySet() due to [https://issues.apache.org/jira/browse/GEODE-1887]
        Set<String> keySet = region.keySetOnServer();
        System.out.println("==============");
        System.out.format("Current stock prices as of %s\n", getCurrentTimestamp());
        for (String key : keySet) {
            System.out.format("[%s] %s\n", key, region.get(key));
        }
    }


    private String getCurrentTimestamp() {
        return timeFormatter.format(LocalDateTime.now());
    }


}
