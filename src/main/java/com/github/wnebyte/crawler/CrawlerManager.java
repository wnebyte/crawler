package com.github.wnebyte.crawler;

import com.github.wnebyte.crawler.util.Files;
import com.github.wnebyte.crawler.util.Urls;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/*
needs to stay 'alive' for as long as there are active crawlers
 */
public class CrawlerManager implements Runnable {

    /*################################
    #       INSTANCE VARIABLES       #
    #################################*/

    // crawler thread-pool
 //   private final ExecutorService crawlerPool;

    private final ThreadPoolExecutor crawlerPool;

    // callback scheduler
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    // discovered urls "marked" for crawling
    private final Set<String> syncedAnchorTagUrls = Collections.synchronizedSet(new HashSet<>());

    private final AtomicInteger documentsMarked = new AtomicInteger(1);

    // # of downloaded resources (html)
    private final AtomicInteger documentsCrawled = new AtomicInteger(0);

    // # of downloaded resources (html) since the last callback
    private final AtomicInteger documentsCrawledRecently = new AtomicInteger(0);

    // # of downloaded bytes (html) since the last callback
    private final AtomicLong bytesDownloadedRecently = new AtomicLong(0L);

    // System.nanoTime() at object construction
    private final AtomicLong runtime = new AtomicLong(System.nanoTime());

    // param
    private final CrawlerContext context;

    /**
     * Constructs a new instance using the specified <code>context</code>.
     * @param context state to be shared between crawlers during subsequent crawls.
     */
    public CrawlerManager(final CrawlerContext context) {
        if (context == null) {
            throw new IllegalArgumentException(
                    "context must be non null"
            );
        }
        this.context = context;
      //  this.crawlerPool = Executors.newFixedThreadPool(context.getThreads());
        this.crawlerPool = new ThreadPoolExecutor(5, 10,
                0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(10000));
    }

    @Override
    public void run() {
        syncedAnchorTagUrls.add(context.getRootUrl());
        crawlerPool.submit(new Crawler(context.getRootUrl(), 0));

        if (context.getCallback() != null) {
            scheduleCallback();
        }

        while (true) {
            // documentsMarked is always 2 greater than documentsCrawled
            if (crawlerPool.getActiveCount() == 0) {
                stopCallback();
                System.out.println("done!");
                break;
            }
        }
    }

    private void scheduleCallback() {
        scheduler.scheduleAtFixedRate(() -> {
            context.getCallback().accept(new CrawlerProgressBuilder()
                    .setFrequency(5000)
                    .setDocumentsMarked(documentsMarked.get())
                    .setDocumentsCrawled(documentsCrawled.get())
                    .setDocumentsCrawledRecently(documentsCrawledRecently.getAndSet(0))
                    .setAvgBitrate(bytesDownloadedRecently.getAndSet(0L) * 8)
                    .setRuntime(System.nanoTime() - runtime.get())
                    .build());
        }, 5000, 5000, TimeUnit.MILLISECONDS);
    }

    public void stopCallback() {
        scheduler.shutdown();
    }

    /*
    executed on a crawler thread
     */
    private class Crawler extends AbstractHtmlCrawler implements Runnable {

        /*################################
        #       INSTANCE VARIABLES       #
        #################################*/

        // url to be crawled
        private final String url;

        // depth of this crawler
        private final int depth;

        // file where the resulting document (html) is to be stored
        private final File location;

        // html document representation
        private Document document;

        /**
         * Constructs a new instance using the specified <code>url</code> and <code>depth</code>.
         * @param url the url of the website to be crawled.
         * @param depth the depth of this crawler, where <code>0</code> is the depth of the root crawler.
         */
        private Crawler(final String url, final int depth) {
            this.url = url;
            this.depth = depth;
            this.location =
                    Files.mk(context.getHtmlDirectory(), context.getEncoder().encode(url), Files.Extension.HTML);
        }

        /**
         * Connects to the url.
         */
        @Override
        protected void connect() {
            try {
                document = Jsoup.connect(url)
                        .timeout(context.getConnectionTimeout())
                        .userAgent(context.getUserAgent())
                        .followRedirects(false)
                        .get();
            } catch (IOException ignored) { }
        }

        /**
         * Processes any anchorTags present in the document.
         */
        @Override
        protected void processAnchorTags() {
            if (document == null) { return; }
            // replace anchor tags with local res
            Elements anchorTags = document.select("a[href]");
            for (Element anchorTag : anchorTags) {
                String absUrl = anchorTag.absUrl("href");
                if (skipAnchorTag(absUrl)) {
                    anchorTag.attr("href", "#");
                    continue;
                }
                anchorTag.attr("href",
                        Files.toPath(context.getEncoder().encode(absUrl), Files.Extension.HTML));
                synchronized (syncedAnchorTagUrls) {
                    boolean success = syncedAnchorTagUrls.add(absUrl);
                    if (success) {
                        documentsMarked.incrementAndGet();
                        crawlerPool.submit(new Crawler(absUrl, depth + 1));
                    }
                }
            }
        }

        /**
         * Processes any stylesheets present in the document.
         */
        @Override
        protected void processStylesheets() {
            if (document == null) { return; }
            // replace stylesheets href with local path
            Elements stylesheets = document.select("link[rel=stylesheet]");
            stylesheets.remove();
        }

        /**
         * Processes any images present in the document.
         */
        @Override
        protected void processImages() {
            if (document == null) { return; }
            Elements images = document.select("picture img[srcSet], img[srcSet], img[data-srcSet]");
            images.remove();
            images = document.select("img[src~(?i)\\.(png|jpe?g)]");

        }

        /**
         * Processes any script tags present in the document.
         */
        @Override
        protected void processScripts() {
            if (document == null) { return; }
            // remove js files
            Elements scripts = document.select("script");
            scripts.remove();
        }

        /**
         * Writes the document (html) to the filesystem.
         */
        @Override
        protected void write() {
            if (document == null) { return; }
            try (FileWriter writer = new FileWriter(location)) {
                writer.write(document.html());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void log() {
            long size = Files.size(location);
            bytesDownloadedRecently.addAndGet(size);
            documentsCrawled.incrementAndGet();
            documentsCrawledRecently.incrementAndGet();
        }

        /**
         * Cleans up and releases any resources after a completed crawl.
         */
        @Override
        protected void cleanup() {
            document = null;
        }

        @Override
        public void run() {
            start();
        }

        /**
         * Returns whether the specified <code>absUrl</code> should <b>not</b> be crawled.
         */
        private boolean skipAnchorTag(final String absUrl) {
            return ((absUrl == null) || !(absUrl.contains(context.getDomainName())) || (absUrl.startsWith("#")) ||
                    (context.getLinkDepth() < depth + 1) || !(Urls.isHttpOrHttps(absUrl)));
        }
    }
}