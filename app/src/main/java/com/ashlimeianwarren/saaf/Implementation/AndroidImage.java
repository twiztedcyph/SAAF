package com.ashlimeianwarren.saaf.Implementation;

import android.graphics.Bitmap;
import com.ashlimeianwarren.saaf.Framework.Graphics;
import com.ashlimeianwarren.saaf.Framework.Graphics.ImageFormat;
import com.ashlimeianwarren.saaf.Framework.Image;

public class AndroidImage implements Image
{
    Bitmap bitmap;
    Graphics.ImageFormat format;

    public AndroidImage(Bitmap bitmap, ImageFormat format)
    {
        this.bitmap = bitmap;
        this.format = format;
    }

    @Override
    public int getWidth()
    {
        return bitmap.getWidth();
    }

    @Override
    public int getHeight()
    {
        return bitmap.getHeight();
    }

    @Override
    public ImageFormat getFormat()
    {
        return format;
    }

    @Override
    public void dispose()
    {
        bitmap.recycle();
    }
}
