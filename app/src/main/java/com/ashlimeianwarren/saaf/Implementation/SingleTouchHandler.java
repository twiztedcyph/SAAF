package com.ashlimeianwarren.saaf.Implementation;

import android.view.MotionEvent;
import android.view.View;

import com.ashlimeianwarren.saaf.Framework.Input.TouchEvent;
import com.ashlimeianwarren.saaf.Framework.Pool;
import com.ashlimeianwarren.saaf.Framework.Pool.PoolObjectFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * SingleTouchHandler class.
 * <p/>
 * Used for older devices.
 */
public class SingleTouchHandler implements TouchHandler
{
    boolean isTouched;
    int touchX;
    int touchY;
    Pool<TouchEvent> touchEventPool;
    List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
    List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
    float scaleX;
    float scaleY;

    /**
     * Constructor for the SingleTouchHandler class.
     *
     * @param view   The current view.
     * @param scaleX X scaling factor.
     * @param scaleY Y scaling factor.
     */
    public SingleTouchHandler(View view, float scaleX, float scaleY)
    {
        PoolObjectFactory<TouchEvent> factory = new PoolObjectFactory<TouchEvent>()
        {
            @Override
            public TouchEvent createObject()
            {
                return new TouchEvent();
            }
        };
        touchEventPool = new Pool<TouchEvent>(factory, 100);
        view.setOnTouchListener(this);

        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    /**
     * Called when a touch event is dispatched to a view. This allows listeners to
     * get a chance to respond before the target view.
     *
     * @param v     The view the touch event has been dispatched to.
     * @param event The MotionEvent object containing full information about
     *              the event.
     * @return True if the listener has consumed the event, false otherwise.
     */
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        synchronized (this)
        {
            TouchEvent touchEvent = touchEventPool.newObject();
            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    touchEvent.type = TouchEvent.TOUCH_DOWN;
                    isTouched = true;
                    break;
                case MotionEvent.ACTION_MOVE:
                    touchEvent.type = TouchEvent.TOUCH_DRAGGED;
                    isTouched = true;
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    touchEvent.type = TouchEvent.TOUCH_UP;
                    isTouched = false;
                    break;
            }

            touchEvent.x = touchX = (int) (event.getX() * scaleX);
            touchEvent.y = touchY = (int) (event.getY() * scaleY);
            touchEventsBuffer.add(touchEvent);

            return true;
        }
    }

    /**
     * Check if a touch is happening.
     *
     * @param pointer The pointer id.
     * @return True if a touch is currently happening. False if not.
     */
    @Override
    public boolean isTouchDown(int pointer)
    {
        synchronized (this)
        {
            if (pointer == 0)
            {
                return isTouched;
            } else
            {
                return false;
            }
        }
    }

    /**
     * Get the X coordinate of the touch.
     *
     * @param pointer The pointer id.
     * @return The X coordinate of the touch.
     */
    @Override
    public int getTouchX(int pointer)
    {
        synchronized (this)
        {
            return touchX;
        }
    }

    /**
     * Get the Y coordinate of the touch.
     *
     * @param pointer The pointer id.
     * @return The Y coordinate of the touch.
     */
    @Override
    public int getTouchY(int pointer)
    {
        synchronized (this)
        {
            return touchY;
        }
    }

    /**
     * Get a list of TouchEvents.
     *
     * @return The list of TouchEvents.
     */
    @Override
    public List<TouchEvent> getTouchEvents()
    {
        synchronized (this)
        {
            int len = touchEvents.size();
            for (int i = 0; i < len; i++)
            {
                touchEventPool.free(touchEvents.get(i));
            }
            touchEvents.clear();
            touchEvents.addAll(touchEventsBuffer);
            touchEventsBuffer.clear();
            return touchEvents;
        }
    }
}
