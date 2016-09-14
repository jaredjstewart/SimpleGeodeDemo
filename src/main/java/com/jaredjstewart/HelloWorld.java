package com.jaredjstewart;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;
import com.gemstone.gemfire.cache.client.ClientRegionShortcut;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HelloWorld {
    private static final List<String> symbols = Arrays.asList("APL", "GOOG", "DELL");

    public static void main(String[] args) throws Exception {
        ClientCache cache = new ClientCacheFactory()
                .addPoolLocator("localhost", 10334)
                .create();

        Region<String, String> region = cache
                .<String, String>createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY)
                .create("regionB");


        Scanner scanner = new Scanner(System.in);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(new RandomStockPriceGenerator(region, symbols));
        executorService.submit(new PollingStockTicker(region, symbols));


        scanner.hasNext();

        executorService.shutdownNow();

//        region.put("1", "Hello");
//        region.put("2", "World");

        Set<String> keySet = region.keySetOnServer(); //TODO: This is a workaround to avoid calling .size() and .entrySet() due to [https://issues.apache.org/jira/browse/GEODE-1887]

        System.out.format("Our cache currently contains %d values.\n", keySet.size());

        for (String key : keySet) {
            System.out.format("key = %s, value = %s\n", key, region.get(key));
        }

        cache.close();
    }
}
