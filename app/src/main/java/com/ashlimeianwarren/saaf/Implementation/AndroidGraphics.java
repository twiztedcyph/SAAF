package com.ashlimeianwarren.saaf.Implementation;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import com.ashlimeianwarren.saaf.Framework.Graphics;
import com.ashlimeianwarren.saaf.Framework.Image;
import java.io.IOException;
import java.io.InputStream;

/**
 * AndroidGraphics class.
 */
public class AndroidGraphics implements Graphics
{
    private AssetManager assets;
    private Bitmap frameBuffer;
    private Canvas canvas;
    private Paint paint;
    private Rect srcRect = new Rect();
    private Rect dstRect = new Rect();

    /**
     * Constructor for the AndroidGraphics class.
     *
     * @param assets Asset object for access to app assets.
     * @param frameBuffer Bitmap to be used as frame buffer.
     */
    public AndroidGraphics(AssetManager assets, Bitmap frameBuffer)
    {
        this.assets = assets;
        this.frameBuffer = frameBuffer;
        this.canvas = new Canvas(frameBuffer);
        this.paint = new Paint();
    }

    /**
     * Get an Image object from a file in a specific format.
     *
     * @param fileName The path to the image file.
     * @param format The format to be used.
     *               @see com.ashlimeianwarren.saaf.Framework.Graphics.ImageFormat
     * @return An Image object.
     */
    @Override
    public Image newImage(String fileName, ImageFormat format)
    {
        Config config = null;
        if (format == ImageFormat.RGB565)
        {
            config = Config.RGB_565;
        }
        else if (format == ImageFormat.ARGB4444)
        {
            config = Config.ARGB_4444;
        }
        else
        {
            config = Config.ARGB_8888;
        }

        Options options = new Options();
        options.inPreferredConfig = config;


        InputStream in = null;
        Bitmap bitmap = null;
        try
        {
            in = assets.open(fileName);
            bitmap = BitmapFactory.decodeStream(in, null, options);
            if (bitmap == null)
            {
                throw new RuntimeException("Couldn't load bitmap from asset '"
                        + fileName + "'");
            }
        } catch (IOException e)
        {
            throw new RuntimeException("Couldn't load bitmap from asset '"
                    + fileName + "'");
        } finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                } catch (IOException e)
                {
                }
            }
        }

        if (bitmap.getConfig() == Config.RGB_565)
        {
            format = ImageFormat.RGB565;
        }
        else if (bitmap.getConfig() == Config.ARGB_4444)
        {
            format = ImageFormat.ARGB4444;
        }
        else
        {
            format = ImageFormat.ARGB8888;
        }

        return new AndroidImage(bitmap, format);
    }

    /**
     * Fill (clear) the screen with a specific colour.
     *
     * @param color The colour to be used.
     */
    @Override
    public void clearScreen(int color)
    {
        //Int representation of colour separated into red, green and blue.
        canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8,
                (color & 0xff));
    }

    /**
     * Render a line.
     *
     * @param x Starting X coordinate for the line.
     * @param y Starting Y coordinate for the line.
     * @param x2 Finishing X coordinate for the line.
     * @param y2 Finishing Y coordinate for the line.
     * @param color The colour to be used for the line.
     */
    @Override
    public void drawLine(int x, int y, int x2, int y2, int color)
    {
        paint.setColor(color);
        canvas.drawLine(x, y, x2, y2, paint);
    }

    /**
     * Render a rectangle.
     *
     * @param x Starting X coordinate for the rectangle.
     * @param y Starting Y coordinate for the rectangle.
     * @param width The width of the rectangle.
     * @param height The height of the rectangle.
     * @param color The colour to be used for the rectangle.
     */
    @Override
    public void drawRect(int x, int y, int width, int height, int color)
    {
        paint.setColor(color);
        paint.setStyle(Style.FILL);
        canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
    }

    /**
     * Fill the entire current canvas clip with the specified ARGB color.
     *
     * @param a Alpha component (0..255).
     * @param r Red component (0..255).
     * @param g Green component (0..255).
     * @param b Blue component (0..255).
     */
    @Override
    public void drawARGB(int a, int r, int g, int b)
    {
        paint.setStyle(Style.FILL);
        canvas.drawARGB(a, r, g, b);
    }

    /**
     * Render text.
     *
     * @param text The text to be rendered.
     * @param x Starting X coordinate for the text.
     * @param y Starting Y coordinate for the text.
     * @param paint Style and color information.
     */
    @Override
    public void drawString(String text, int x, int y, Paint paint)
    {
        canvas.drawText(text, x, y, paint);


    }

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
    @Override
    public void drawImage(Image image, int x, int y, int srcX, int srcY,
                          int srcWidth, int srcHeight)
    {
        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth;
        srcRect.bottom = srcY + srcHeight;

        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + srcWidth;
        dstRect.bottom = y + srcHeight;

        canvas.drawBitmap(((AndroidImage) image).bitmap, srcRect, dstRect, null);
    }

    /**
     * Render an image.
     *
     * @param Image Image object to be rendered.
     * @param x Starting X coordinate for the image.
     * @param y Starting Y coordinate for the image.
     */
    @Override
    public void drawImage(Image Image, int x, int y)
    {
        canvas.drawBitmap(((AndroidImage) Image).bitmap, x, y, null);
    }

    /**
     * Get the width of the frame buffer.
     *
     * @return The width of the frame buffer.
     */
    @Override
    public int getWidth()
    {
        return frameBuffer.getWidth();
    }

    /**
     * Get the height of the frame buffer.
     *
     * @return The height of the frame buffer.
     */
    @Override
    public int getHeight()
    {
        return frameBuffer.getHeight();
    }
}
