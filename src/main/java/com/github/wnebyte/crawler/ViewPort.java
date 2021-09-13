package com.github.wnebyte.crawler;

import java.awt.*;

/**
 * This singleton class can be used to get the width and height of the primary display.
 */
public class ViewPort {

    // singleton instance
    private static ViewPort instance;

    // the width of the users viewport
    private final double width;

    // the height of the users viewport
    private final double height;

    /**
     * Constructs a new instance using the specified <code>width</code> and <code>height</code>.
     * @param width to be used.
     * @param height to be used.
     */
    private ViewPort(final double width, final double height) {
        this.width = width;
        this.height = height;
    }

    /**
     * @return the width of the viewport.
     */
    public double getWidth() {
        return width;
    }

    /**
     * @return the height of the viewport.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Constructs a new instance if one has not already been constructed.
     * @return the singleton instance.
     */
    public static ViewPort getInstance() {
        if (instance == null) {
            Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
            instance = new ViewPort(dimension.getWidth(), dimension.getHeight());
        }
        return instance;
    }
}