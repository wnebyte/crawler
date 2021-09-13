package com.github.wnebyte.crawler.util;

public class Objects {

    public static <T> T requireNonNullElseGet(final T value, final T get) {
        return (value != null) ? value : get;
    }
}
