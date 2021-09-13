package com.github.wnebyte.crawler.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

/**
 * This class declares utility methods for working with instances of the {@link URL} class.
 */
public class Urls {

    /**
     * This enum represents an Application-Layer Network Protocol.
     */
    public enum Protocol {
        /** http */
        HTTP,
        /** https */
        HTTPS
    }

    /**
     * Returns the domain name of the specified <code>url</code>.
     * @return the domain name if the specified <code>url</code> is non <code>null</code>,
     * otherwise an empty String.
     */
    public static String getDomainName(final URL url) {
        if (url == null) { return ""; }
        return getDomainName(url.toExternalForm());
    }

    /**
     * Returns the domain name of the specified <code>url</code>.
     * @return the domain name if the specified <code>url</code> is non <code>null</code>,
     * otherwise an empty String.
     */
    public static String getDomainName(final String url) {
        if (url == null) { return ""; }
        return url.split("\\.", 3)[1];
    }

    /**
     * Returns whether the specified <code>url</code> is malformed.
     * @return <code>true</code> if the specified <code>url</code> is malformed,
     * otherwise <code>false</code>.
     */
    public static boolean isMalformed(final String url) {
        try {
            new URL(url);
        } catch (MalformedURLException e) {
            return true;
        }
        return false;
    }

    /**
     * Returns whether the specified <code>url</code> is defined using one of the specified
     * <code>protocols</code>.
     * @param url the url.
     * @param protocols the protocols.
     * @return <code>true</code> if the specified <code>url</code> is not malformed and
     * is defined using one of the specified <code>protocols</code>,
     * otherwise <code>false</code>.
     */
    public static boolean isProtocol(final String url, final Protocol... protocols) {
        if ((url == null) || (protocols == null) || (protocols.length == 0)) { return false; }

        try {
            return isProtocol(new URL(url), protocols);
        } catch (MalformedURLException e) {
            return false;
        }
    }

    /**
     * Returns whether the specified <code>url</code> is defined using one of the specified
     * <code>protocols</code>.
     * @param url the url.
     * @param protocols the protocols.
     * @return <code>true</code> if the specified <code>url</code> is defined using one of
     * the specified <code>protocols</code>, otherwise <code>false</code>.
     */
    public static boolean isProtocol(final URL url, final Protocol... protocols) {
        if ((url == null) || (protocols == null) || (protocols.length == 0)) { return false; }

        for (Protocol protocol : protocols) {
            boolean is = isProtocol(url, protocol);
            if (is) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns whether the specified <code>url</code> is defined using the specified <code>protocol</code>.
     * @param url the url.
     * @param protocol the protocol.
     * @return <code>true</code> if the specified <code>url</code> is defined using the
     * specified <code>protocol</code>, otherwise <code>false</code>.
     */
    public static boolean isProtocol(final URL url, final Protocol protocol) {
        if ((url == null) || (protocol == null)) { return false; }
        return url.getProtocol().toLowerCase(Locale.ROOT)
                .equals(protocol.toString().toLowerCase(Locale.ROOT));
    }

    /**
     * Returns whether the specified <code>url</code> is defined using the specified <code>protocol</code>.
     * @param url the url.
     * @param protocol the protocol.
     * @return <code>true</code> if the specified <code>url</code> is not malformed and is
     * defined using the specified <code>protocol</code>, otherwise <code>false</code>.
     */
    public static boolean isProtocol(final String url, final Protocol protocol) {
        if ((url == null) || (protocol == null)) { return false; }
        try {
            return isProtocol(new URL(url), protocol);
        } catch (MalformedURLException e) {
            return false;
        }
    }

    /**
     * Returns whether the specified <code>url</code> is defined using one of
     * <code>HTTP</code> or <code>HTTPS</code>.
     * @param url the url.
     * @return <code>true</code> if the specified <code>url</code> is not malformed and is
     * defined using one of <code>HTTP</code> or <code>HTTPS</code>.
     */
    public static boolean isHttpOrHttps(final String url) {
        return isProtocol(url, Protocol.HTTP, Protocol.HTTPS);
    }

}