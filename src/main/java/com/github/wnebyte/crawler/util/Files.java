package com.github.wnebyte.crawler.util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

/**
 * This class declares utility methods for working with instances of the {@link File} class.
 */
public class Files {

    /**
     * This enum represents a File Extension.
     */
    public enum Extension {
        /** .html */
        HTML,
        /** .png */
        PNG,
        /** .jpg */
        JPG,
        /** .jpeg */
        JPEG,
        /** .css */
        CSS
    }

    /**
     * Constructs a new <code>File</code> using the format:
     * <code>{@literal <}path{@literal >}{@linkplain File#separator}{@literal <}fileName{@literal >}</code>
     * @param path the path to the file's parent directory.
     * @param fileName the name of the file.
     * @return the file if the specified <code>path</code> and <code>fileName</code> was non <code>null</code>,
     * otherwise <code>null</code>.
     */
    public static File mk(final String path, final String fileName) {
        if ((path == null) || (fileName == null)) { return null; }
        String fp = path.concat(File.separator).concat(fileName);
        return new File(fp);
    }

    /**
     * Constructs a new <code>File</code> using the format:
     * <code>{@literal <}path{@literal >}{@linkplain File#separator}{@literal <}fileName{@literal >}.{@literal <}extension{@literal >}</code>
     * @param path the path to the file's parent directory.
     * @param fileName the name of the file.
     * @param extension the extension type of the file.
     * @return the file if the specified <code>path</code> and <code>fileName</code> was non <code>null</code>,
     * otherwise <code>null</code>.
     */
    public static File mk(final String path, final String fileName, final Extension extension) {
        if ((path == null) || (fileName == null)) { return null; }
        String filePath = path.concat(File.separator)
                .concat(fileName)
                .concat(".")
                .concat(extension.toString().toLowerCase(Locale.ROOT));
        return new File(filePath);
    }

    /**
     * Returns whether the specified <code>path</code> coincides with a directory.
     * @param path of the potential directory.
     * @return <code>true</code> if the specified <code>path</code> is non <code>null</code> and coincides
     * with a directory, otherwise <code>false</code>.
     */
    public static boolean isDirectory(final String path) {
        if (path == null) { return false; }
        File file = new File(path);
        return file.isDirectory();
    }

    public static long size(final File file) {
        if (file == null) { return 0L; }
        return file.length();
    }

    public static long size(final String path) {
        return size(new File(path));
    }

    public static String toPath(final String fileName, final Extension extension) {
        if ((fileName == null) || (extension == null)) { return null; }
        return fileName.concat(".").concat(extension.toString().toLowerCase(Locale.ROOT));
    }
}