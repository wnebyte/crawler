package com.github.wnebyte.crawler;

import java.io.File;
import java.util.Base64;
import java.util.function.Consumer;

public class CrawlerContextBuilder {

    private String rootUrl;

    private int linkDepth;

    private String htmlDirectory;

    private String cssDirectory = "../css";

    private String imageDirectory = "../images";

    private Encoder<String> encoder = new Encoder<String>() {
        @Override
        public String encode(String value) {
            return Base64.getUrlEncoder().encodeToString(value.getBytes());
        }
    };

    private Consumer<CrawlerProgress> callback;

    private String userAgent = "Mozilla";

    private int connectionTimeout = 15000;

    private int nThreads = 3;

    private boolean downloadImages = true;

    private boolean downloadStylesheets = true;

    /**
     * Sets the first url that should be crawled.
     * @param rootUrl the first url to be crawled.
     * @return this.
     */
    public CrawlerContextBuilder setRootUrl(final String rootUrl) {
        this.rootUrl = rootUrl;
        return this;
    }

    /**
     * Sets the link depth.
     * @param linkDepth the depth that the crawler should stop at.
     * @return this.
     */
    public CrawlerContextBuilder setLinkDepth(final int linkDepth) {
        this.linkDepth = linkDepth;
        return this;
    }

    public CrawlerContextBuilder setHtmlDirectory(final String htmlDirectory) {
        this.htmlDirectory = htmlDirectory;
        return this;
    }

    public CrawlerContextBuilder setCssDirectory(final String cssDirectory) {
        this.cssDirectory = cssDirectory;
        return this;
    }

    public CrawlerContextBuilder setImageDirectory(final String imageDirectory) {
        this.imageDirectory = imageDirectory;
        return this;
    }

    public CrawlerContextBuilder setEncoder(final Encoder<String> encoder) {
        if (encoder != null) {
            this.encoder = encoder;
        }
        return this;
    }

    public CrawlerContextBuilder setUserAgent(final String userAgent) {
        if (userAgent != null) {
            this.userAgent = userAgent;
        }
        return this;
    }

    public CrawlerContextBuilder setCallback(final Consumer<CrawlerProgress> callback) {
        this.callback = callback;
        return this;
    }

    public CrawlerContextBuilder setThreads(final int nThreads) {
        if (1 <= nThreads) {
            this.nThreads = nThreads;
        }
        return this;
    }

    /**
     * Sets the <code>connectionTimeout</code>.
     * @param connectionTimeout in ms.
     * @return this.
     */
    public CrawlerContextBuilder setConnectionTimeout(final int connectionTimeout) {
        if (0 <= connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
        }
        return this;
    }

    public CrawlerContextBuilder setDownloadImages(final boolean value) {
        this.downloadImages = value;
        return this;
    }

    public CrawlerContextBuilder setDownloadStylesheets(final boolean value) {
        this.downloadStylesheets = value;
        return this;
    }

    public CrawlerContext build() {
        if ((rootUrl == null) || (rootUrl.equals(""))) {
            throw new IllegalArgumentException(
                    "rootUrl must not be null or empty"
            );
        }
        if (linkDepth <= 0) {
            throw new IllegalArgumentException(
                    "linkDepth must be greater than 0"
            );
        }
        if ((htmlDirectory == null) || !(new File(htmlDirectory).isDirectory())) {
            throw new IllegalArgumentException(
                    "directory must be a directory on the filesystem"
            );
        }
        return new CrawlerContext(
                rootUrl,
                linkDepth,
                htmlDirectory,
                cssDirectory,
                imageDirectory,
                encoder,
                userAgent,
                connectionTimeout,
                callback,
                nThreads
                );
    }
}
