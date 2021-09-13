package com.github.wnebyte.crawler;

/**
 * This functional interface declares a method for encoding a value into a String.
 * @param <T> the Type of the value.
 */
public interface Encoder<T> {

    /**
     * Encodes the specified <code>value</code> into a String.
     * @param value to be encoded.
     * @return the encoded result.
     */
    String encode(final T value);
}