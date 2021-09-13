package com.github.wnebyte.crawler.test;

import com.github.wnebyte.crawler.CrawlerContextBuilder;
import com.github.wnebyte.crawler.CrawlerManager;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Test {

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void main(String[] args) throws InterruptedException {
        CrawlerManager crawlerManager = new CrawlerManager(new CrawlerContextBuilder()
                .setRootUrl("https://www.expressen.se")
                .setLinkDepth(1)
                .setThreads(5)
                .setHtmlDirectory("res/assets/html")
                .setEncoder(value -> String.valueOf(value.hashCode()))
                .setCallback(progress -> System.out.printf(
                        "<%d/%d>, <%.1fMB/s> <%.0f avg urls/s>%n",
                        progress.getDocumentsCrawled(),
                        progress.getDocumentsMarked(),
                        progress.getAvgBitRate() * Math.pow(10, -6),
                        progress.getCurrentUrlsPerSecond()
                ))
                .setDownloadImages(true)
                .setDownloadStylesheets(true)
                .build());
        executor.submit(crawlerManager);
        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.MINUTES);
        System.out.println("terminated");
    }
}