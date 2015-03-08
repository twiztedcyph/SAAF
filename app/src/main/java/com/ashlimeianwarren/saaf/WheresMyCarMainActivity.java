package com.ashlimeianwarren.saaf;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ashlimeianwarren.saaf.Implementation.PositionManager;


public class WheresMyCarMainActivity extends ActionBarActivity
{
    private PositionManager pm;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheres_my_car_main);
        pm = new PositionManager(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        pm.start();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        System.out.println("OnPause");
        pm.close();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        System.out.println("OnStop");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wheres_my_car_main, menu);
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

    public void showLocation(View view)
    {
        TextView locDisp = (TextView) findViewById(R.id.wmc_maintext);
        double lat = pm.getCurrentPosition().getLatitude();
        double lon = pm.getCurrentPosition().getLongitude();

        locDisp.setText(lat + "\n" + lon);
    }
}
