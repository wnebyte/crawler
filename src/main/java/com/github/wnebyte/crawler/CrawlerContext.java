package com.github.wnebyte.crawler;

import com.github.wnebyte.crawler.util.Urls;
import java.util.function.Consumer;

/**
 * This class encapsulates shared state needed to crawl and archive a set of urls.
 */
public class CrawlerContext {

    private static final ViewPort viewPort = ViewPort.getInstance();

    private final String rootUrl;

    private final String domainName;

    private final int linkDepth;

    private final String htmlDirectory;

    private final String cssDirectory;

    private final String imageDirectory;

    private final Encoder<String> encoder;

    private final String userAgent;

    private final int connectionTimeout;

    private final Consumer<CrawlerProgress> callback;

    private final int nThreads;

    public CrawlerContext(
            final String rootUrl,
            final int linkDepth,
            final String htmlDirectory,
            final String cssDirectory,
            final String imageDirectory,
            final Encoder<String> encoder,
            final String userAgent,
            final int connectionTimeout,
            final Consumer<CrawlerProgress> callback,
            final int nThreads

    ) {
        if (Urls.isMalformed(rootUrl)) {
            throw new IllegalArgumentException(
                    "the specified rootUrl is not a url"
            );
        }
        if (!Urls.isHttpOrHttps(rootUrl)) {
            throw new IllegalArgumentException(
                    "rootUrl must use either http or https protocol"
            );
        }
        if (linkDepth < 0) {
            throw new IllegalArgumentException(
                    "the specified linkDepth must be greater than -1"
            );
        }
        this.rootUrl = rootUrl;
        this.domainName = Urls.getDomainName(rootUrl);
        this.linkDepth = linkDepth;
        this.htmlDirectory = htmlDirectory;
        this.cssDirectory = cssDirectory;
        this.imageDirectory = imageDirectory;
        this.encoder = encoder;
        this.userAgent = userAgent;
        this.connectionTimeout = connectionTimeout;
        this.callback = callback;
        this.nThreads = nThreads;
    }

    public String getRootUrl() {
        return rootUrl;
    }

    public int getLinkDepth() {
        return linkDepth;
    }

    public String getHtmlDirectory() {
        return htmlDirectory;
    }

    public Encoder<String> getEncoder() {
        return encoder;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public ViewPort getViewPort() {
        return viewPort;
    }

    public Consumer<CrawlerProgress> getCallback() {
        return callback;
    }

    public String getDomainName() {
        return domainName;
    }

    public int getThreads() {
        return nThreads;
    }

    public String getCssDirectory() {
        return cssDirectory;
    }

    public String getImageDirectory() {
        return imageDirectory;
    }
}