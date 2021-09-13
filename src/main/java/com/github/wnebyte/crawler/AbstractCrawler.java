package com.github.wnebyte.crawler;

public abstract class AbstractCrawler {

    public final void start() {
        connect();
        work();
        write();
        log();
        cleanup();
    }

    protected abstract void connect();

    protected abstract void work();

    protected abstract void write();

    protected abstract void log();

    protected abstract void cleanup();
}