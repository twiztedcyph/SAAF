package com.ashlimeianwarren.saaf.Implementation;

import android.view.View.OnTouchListener;

import com.ashlimeianwarren.saaf.Framework.Input.TouchEvent;

import java.util.List;

/**
 * Interface for a TouchHandler.
 */
public interface TouchHandler extends OnTouchListener
{
    /**
     * Check if a touch is happening.
     *
     * @param pointer The pointer id.
     * @return True if a touch is currently happening. False if not.
     */
    public boolean isTouchDown(int pointer);

    /**
     * Get the X coordinate of the touch.
     *
     * @param pointer The pointer id.
     * @return The X coordinate of the touch.
     */
    public int getTouchX(int pointer);

    /**
     * Get the Y coordinate of the touch.
     *
     * @param pointer The pointer id.
     * @return The Y coordinate of the touch.
     */
    public int getTouchY(int pointer);

    /**
     * Get a list of TouchEvents.
     *
     * @return The list of TouchEvents.
     */
    public List<TouchEvent> getTouchEvents();
}
