package com.ashlimeianwarren.saaf;

import android.app.Activity;
import android.graphics.Point;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.ashlimeianwarren.saaf.Framework.Input;
import com.ashlimeianwarren.saaf.Implementation.AndroidInput;
import com.ashlimeianwarren.saaf.Implementation.MultiTouchHandler;

import java.util.List;


public class WhatHappenedTodayMainActivity extends ActionBarActivity
{

    private MultiTouchHandler multiTouchHandler;
    Input input;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_happened_today_main);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        v = getWindow().getDecorView().findViewById(android.R.id.content);
        multiTouchHandler = new MultiTouchHandler(v, width, height);

        v.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                int action = MotionEventCompat.getActionMasked(event);

                switch (action)
                {
                    case (MotionEvent.ACTION_DOWN):
                        multiTouchHandler.onTouch(v, event);
                        System.out.println("action down " + event.getX() + " " + event.getY());
                        return true;
                    case (MotionEvent.ACTION_MOVE):
                        multiTouchHandler.onTouch(v, event);
                        System.out.println("action move" + event.getX() + " " + event.getY());
                        return true;
                    case (MotionEvent.ACTION_UP):
                        multiTouchHandler.onTouch(v, event);
                        System.out.println("action up" + event.getX() + " " + event.getY());
                        return true;
                    case (MotionEvent.ACTION_CANCEL):
                        multiTouchHandler.onTouch(v, event);
                        System.out.println("action cancel" + event.getX() + " " + event.getY());
                        return true;
                    case (MotionEvent.ACTION_OUTSIDE):

                        multiTouchHandler.onTouch(v, event);
                        System.out.println("action outside" + event.getX() + " " + event.getY());
                        return true;

                    default:
                        return v.onTouchEvent(event);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_what_happened_today_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
