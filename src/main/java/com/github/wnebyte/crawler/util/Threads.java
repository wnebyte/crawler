package com.github.wnebyte.crawler.util;

public class Threads {

    public static int grow(final int nThreads, final int urls) {
        if (range(urls, 0, 5)) {
            return 5 * nThreads;
        }
        if (range(urls, 5, 10)) {
            return 2 * nThreads;
        }
        if (range(urls, 10, 15)) {
            return nThreads + 2;
        }
        if (range(urls, 15, 20)) {
            return nThreads + 1;
        }
        return nThreads;
    }

    private static boolean range(final int n, final int lowerBounds, final int upperBounds) {
        return (lowerBounds <= n) && (n < upperBounds);
    }
}
