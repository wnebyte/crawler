package com.github.wnebyte.crawler;

public class CrawlerProgressBuilder {

    private double s;

    private long avgBitrate;

    private int documentsCrawled;

    private int documentsMarked;

    private int documentsCrawledRecently;

    private long runtime;

    public CrawlerProgressBuilder setFrequency(final long millis) {
        this.s = millis / 1000.0;
        return this;
    }

    public CrawlerProgressBuilder setAvgBitrate(final long avgBitrate) {
        this.avgBitrate = avgBitrate;
        return this;
    }

    public CrawlerProgressBuilder setDocumentsCrawled(final int documentsCrawled) {
        this.documentsCrawled = documentsCrawled;
        return this;
    }

    public CrawlerProgressBuilder setDocumentsCrawledRecently(final int documentsCrawledRecently) {
        this.documentsCrawledRecently = documentsCrawledRecently;
        return this;
    }

    public CrawlerProgressBuilder setDocumentsMarked(final int documentsMarked)  {
        this.documentsMarked = documentsMarked;
        return this;
    }

    public CrawlerProgressBuilder setRuntime(final long runtime) {
        this.runtime = runtime;
        return this;
    }

    public CrawlerProgress build() {
        double urlsSince = documentsCrawledRecently / s;
        double urls = (runtime != 0) ?
                (documentsCrawled / (runtime * Math.pow(10, -9))) :
                0;
        return new CrawlerProgress(
                avgBitrate,
                documentsCrawled,
                documentsMarked,
                runtime,
                urls,
                urlsSince
        );
    }
}
