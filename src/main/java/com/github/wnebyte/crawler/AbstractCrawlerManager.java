package com.github.wnebyte.crawler;

public abstract class AbstractCrawlerManager {

    private void start() {
        scheduleCallback();
        startCrawler();
        await();
        downloadStylesheets();
        downloadImages();
        cleanup();
    }

    protected abstract void scheduleCallback();

    protected abstract void startCrawler();

    protected abstract void await();

    protected abstract void downloadStylesheets();

    protected abstract void downloadImages();

    protected abstract void cleanup();
}