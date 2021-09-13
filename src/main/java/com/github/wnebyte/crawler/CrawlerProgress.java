package com.github.wnebyte.crawler;

/**
 * This class represents the progress of a crawl.
 */
public class CrawlerProgress {

    private final int documentsCrawled;

    private final int documentsMarked;

    private final long avgBitRate;

    private final long runtime;

    private final double avgUrlsPerSecond;

    private final double currentUrlsPerSecond;

    public CrawlerProgress(
            final long avgBitRate,
            final int documentsCrawled,
            final int documentsMarked,
            final long runtime,
            final double avgUrlsPerSecond,
            final double currentUrlsPerSecond
            ) {
        this.avgBitRate = avgBitRate;
        this.documentsCrawled = documentsCrawled;
        this.documentsMarked = documentsMarked;
        this.runtime = runtime;
        this.avgUrlsPerSecond = avgUrlsPerSecond;
        this.currentUrlsPerSecond = currentUrlsPerSecond;
    }

    /**
     * @return returns the current byte rate since the last callback.
     */
    public long getAvgBitRate() {
        return avgBitRate;
    }

    /**
     * @return the number of urls that have been crawled.
     */
    public int getDocumentsCrawled() {
        return documentsCrawled;
    }

    /**
     * @return the number of urls that have been marked for crawling.
     */
    public int getDocumentsMarked() {
        return documentsMarked;
    }

    /**
     * @return the time in ns since the initialisation of the crawler.
     */
    public long getRuntime() {
        return runtime;
    }

    public int getRemainingCrawls() {
        return getDocumentsMarked() - getDocumentsCrawled();
    }

    public double getAvgUrlsPerSecond() {
        return avgUrlsPerSecond;
    }

    public double getCurrentUrlsPerSecond() {
        return currentUrlsPerSecond;
    }
}