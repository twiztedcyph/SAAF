package com.ashlimeianwarren.saaf.Implementation;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.view.View;
import com.ashlimeianwarren.saaf.Framework.Input;
import java.util.List;

/**
 * {@inheritDoc}
 */
public class AndroidInput implements Input
{
    private TouchHandler touchHandler;

    /**
     * Constructor for the AndroidInput class.
     *
     * @param context The current context of the app.
     * @param view The current View.
     * @param scaleX X scaling factor.
     * @param scaleY Y scaling factor.
     */
    public AndroidInput(Context context, View view, float scaleX, float scaleY)
    {
        if (VERSION.SDK_INT < Build.VERSION_CODES.ECLAIR)
        {
            touchHandler = new SingleTouchHandler(view, scaleX, scaleY);
        }
        else
        {
            touchHandler = new MultiTouchHandler(view, scaleX, scaleY);
        }
    }


    @Override
    public boolean isTouchDown(int pointer)
    {
        return touchHandler.isTouchDown(pointer);
    }

    @Override
    public int getTouchX(int pointer)
    {
        return touchHandler.getTouchX(pointer);
    }

    @Override
    public int getTouchY(int pointer)
    {
        return touchHandler.getTouchY(pointer);
    }

    @Override
    public List<TouchEvent> getTouchEvents()
    {
        return touchHandler.getTouchEvents();
    }
}
