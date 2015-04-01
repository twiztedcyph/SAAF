package com.ashlimeianwarren.saaf;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ashlimeianwarren.saaf.Beans.WhatHappenedToday.MediaNote;
import com.ashlimeianwarren.saaf.Beans.WhatHappenedToday.TextNote;
import com.ashlimeianwarren.saaf.Beans.WheresMyCar.PointOfInterest;
import com.ashlimeianwarren.saaf.Implementation.PositionManager;

import java.io.File;


public class WheresMyCarMainActivity extends ActionBarActivity
{
    private PositionManager pm;
    private PointOfInterest location;
    private double lon, lat;
    private Location currentLocation = null;
    private PointOfInterest[] locationArray;
    private ListView listView;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheres_my_car_main);
        pm = new PositionManager(this);
        LocationManager manager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            buildAlertMessageNoGps();
        }
        refreshList();
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                int locationId = locationArray[position].get_id();
                PointOfInterest pointOfInterest = new PointOfInterest();
                pointOfInterest.delete(locationId, WheresMyCarMainActivity.this);

                refreshList();
                return false;
            }
        });


    }

    private void buildAlertMessageNoGps()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setNegativeButton("Yes", new DialogInterface.OnClickListener()
                {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id)
                    {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setPositiveButton("No", new DialogInterface.OnClickListener()
                {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id)
                    {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void gpsLoadingAlert()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your phone is still trying to get a location lock. Please wait.")
                .setCancelable(false)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener()
                {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id)
                    {

                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        refreshList();
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

    public void saveLocationClicked(View view)
    {
        LocationManager manager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            buildAlertMessageNoGps();

        }
        else if (pm.getCurrentPosition() == null)
        {
            gpsLoadingAlert();
        }
        else
        {


            // TextView locDisp = (TextView) findViewById(R.id.wmc_maintext);
            lat = pm.getCurrentPosition().getLatitude();
            lon = pm.getCurrentPosition().getLongitude();
            location = new PointOfInterest(lon, lat, "Car Location");
            location.persist(this);
            refreshList();

            // locDisp.setText("Position Saved");


        }
    }

    private void refreshList()
    {

        locationArray = new PointOfInterest().retrieve(this);
        listAdapter = new CustomLocationListAdapter(this, locationArray);
        listView = (ListView) findViewById(R.id.custom_location_list);
        listView.setAdapter(listAdapter);


    }
}
