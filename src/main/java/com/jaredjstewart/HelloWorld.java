package com.jaredjstewart;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;
import com.gemstone.gemfire.cache.client.ClientRegionShortcut;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HelloWorld {

    public static void main(String[] args) throws Exception {
        ClientCache cache = new ClientCacheFactory()
                .addPoolLocator("localhost", 10334)
                .create();

        Region<String, String> region = cache
                .<String, String>createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY)
                .create("regionB");

        ExecutorService executorService = startExecutingSimulation(region);
        blockUntilUserInput();

        shutdownSimulation(executorService);

        cache.close();
    }

    private static void shutdownSimulation(ExecutorService executorService) {
        System.out.println("Shutting down the simulation...");
        executorService.shutdownNow();
    }

    private static void blockUntilUserInput() {
        System.out.println("Press enter to end the simulation...");
        Scanner scanner = new Scanner(System.in);
        scanner.hasNext();
    }

    private static ExecutorService startExecutingSimulation(Region<String, String> region) {
        System.out.println("Starting the stock price simulation...");
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(new RandomStockPriceGenerator(region));
        executorService.submit(new PollingStockTicker(region));
        return executorService;
    }
}
