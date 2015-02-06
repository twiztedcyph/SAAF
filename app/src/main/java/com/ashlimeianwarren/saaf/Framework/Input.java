package com.ashlimeianwarren.saaf.Framework;

import java.util.List;

/**
 * Interface class for Input.
 */
public interface Input
{

    /**
     * This class describes a touch event.
     */
    public static class TouchEvent
    {
        public static final int TOUCH_DOWN = 0;
        public static final int TOUCH_UP = 1;
        public static final int TOUCH_DRAGGED = 2;
        public static final int TOUCH_HOLD = 3;

        public int type;
        public int x, y;
        public int pointer;
    }

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
