package com.github.wnebyte.crawler;

public abstract class AbstractHtmlCrawler extends AbstractCrawler {

    @Override
    protected final void work() {
        processAnchorTags();
        processStylesheets();
        processImages();
        processScripts();
    }

    protected abstract void processAnchorTags();

    protected abstract void processStylesheets();

    protected abstract void processImages();

    protected abstract void processScripts();
}