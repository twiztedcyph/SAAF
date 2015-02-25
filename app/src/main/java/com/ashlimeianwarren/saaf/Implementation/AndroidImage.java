package com.ashlimeianwarren.saaf.Implementation;

import android.graphics.Bitmap;

import com.ashlimeianwarren.saaf.Framework.Graphics;
import com.ashlimeianwarren.saaf.Framework.Graphics.ImageFormat;
import com.ashlimeianwarren.saaf.Framework.Image;

/**
 * AndroidImage class.
 */
public class AndroidImage implements Image
{
    Bitmap bitmap;
    Graphics.ImageFormat format;

    /**
     * Constructor for the AndroidImage class.
     *
     * @param bitmap Imange bitmap to be used.
     * @param format Image format to be used.
     * @see com.ashlimeianwarren.saaf.Framework.Graphics.ImageFormat
     */
    public AndroidImage(Bitmap bitmap, ImageFormat format)
    {
        this.bitmap = bitmap;
        this.format = format;
    }

    /**
     * Get the width of the image.
     *
     * @return The width of the image.
     */
    @Override
    public int getWidth()
    {
        return bitmap.getWidth();
    }

    /**
     * Get the height of the image.
     *
     * @return The height of the image.
     */
    @Override
    public int getHeight()
    {
        return bitmap.getHeight();
    }

    /**
     * Get the format used for the image.
     *
     * @return The format used for the image.
     */
    @Override
    public ImageFormat getFormat()
    {
        return format;
    }

    /**
     * Free the object associated with this bitmap, and clear the reference to the pixel data.
     */
    @Override
    public void dispose()
    {
        bitmap.recycle();
    }
}
