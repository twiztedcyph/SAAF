package com.ashlimeianwarren.saaf.Framework;

import android.graphics.Paint;

/**
 * Interface class for graphics.
 */
public interface Graphics
{
    /**
     * The available selection of image formats.
     */
    public static enum ImageFormat
    {
        ARGB8888, ARGB4444, RGB565
    }

    /**
     * Get an Image object from a file in a specific format.
     *
     * @param fileName The path to the image file.
     * @param format The format to be used.
     *               @see com.ashlimeianwarren.saaf.Framework.Graphics.ImageFormat
     * @return An Image object.
     */
    public Image newImage(String fileName, ImageFormat format);

    /**
     * Fill (clear) the screen with a specific colour.
     *
     * @param color The colour to be used.
     */
    public void clearScreen(int color);

    /**
     * Render a line.
     *
     * @param x Starting X coordinate for the line.
     * @param y Starting Y coordinate for the line.
     * @param x2 Finishing X coordinate for the line.
     * @param y2 Finishing Y coordinate for the line.
     * @param color The colour to be used for the line.
     */
    public void drawLine(int x, int y, int x2, int y2, int color);

    /**
     * Render a rectangle.
     *
     * @param x Starting X coordinate for the rectangle.
     * @param y Starting Y coordinate for the rectangle.
     * @param width The width of the rectangle.
     * @param height The height of the rectangle.
     * @param color The colour to be used for the rectangle.
     */
    public void drawRect(int x, int y, int width, int height, int color);

    /**
     * Render and scale an image.
     *
     * @param image Image object to be rendered
     * @param x Starting X coordinate for the image.
     * @param y Starting Y coordinate for the image.
     * @param srcX Image X coordinate.
     * @param srcY Image Y coordinate.
     * @param srcWidth The width of the image.
     * @param srcHeight The height of the image.
     */
    public void drawImage(Image image, int x, int y, int srcX, int srcY,
                          int srcWidth, int srcHeight);

    /**
     * Render an image.
     *
     * @param Image Image object to be rendered.
     * @param x Starting X coordinate for the image.
     * @param y Starting Y coordinate for the image.
     */
    public void drawImage(Image Image, int x, int y);

    /**
     * Render text.
     *
     * @param text The text to be rendered.
     * @param x Starting X coordinate for the text.
     * @param y Starting Y coordinate for the text.
     * @param paint Style and color information.
     */
    void drawString(String text, int x, int y, Paint paint);

    /**
     * Get the width of the frame buffer.
     *
     * @return The width of the frame buffer.
     */
    public int getWidth();

    /**
     * Get the height of the frame buffer.
     *
     * @return The height of the frame buffer.
     */
    public int getHeight();

    /**
     * Fill the entire current canvas clip with the specified ARGB color.
     *
     * @param i Alpha component (0..255).
     * @param j Red component (0..255).
     * @param k Green component (0..255).
     * @param l Blue component (0..255).
     */
    public void drawARGB(int i, int j, int k, int l);
}
