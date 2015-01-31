package com.ashlimeianwarren.saaf.Framework;

/**
 * Interface class for AndroidImage.
 */
public interface Image
{
    /**
     * Get the width of the image.
     *
     * @return The width of the image.
     */
    public int getWidth();

    /**
     * Get the height of the image.
     *
     * @return The height of the image.
     */
    public int getHeight();

    /**
     * Get the format used for the image.
     *
     * @return The format used for the image.
     */
    public Graphics.ImageFormat getFormat();

    /**
     * Free the object associated with this bitmap, and clear the reference to the pixel data.
     */
    public void dispose();
}
