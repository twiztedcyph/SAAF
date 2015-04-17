package com.ashlimeianwarren.saaf;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ashlimeianwarren.saaf.Beans.WhatHappenedToday.MediaNote;
import com.ashlimeianwarren.saaf.Beans.WhatHappenedToday.Subject;
import com.ashlimeianwarren.saaf.Beans.WhatHappenedToday.TextNote;
import com.ashlimeianwarren.saaf.Beans.WheresMyCar.PointOfInterest;
import com.ashlimeianwarren.saaf.Implementation.PositionManager;

import java.io.File;

/**
 * A class used to control a users interaction with the "Where's My Car" section of the app.
 * Responsible for allowing users to set up a new location for later navigation and for choosing
 * a pre saved location to navigate too.
 */

public class WheresMyCarMainActivity extends ActionBarActivity
{
    private PointOfInterest location;
    private double lon, lat;
    private Location currentLocation = null;
    private PointOfInterest[] locationArray;
    private ListView listView;
    private ListAdapter listAdapter;
    private AlertDialog.Builder alertDialogBuilder;
    private int clickedPosition;

    /**
     * Android Method, run when this Activity is created.
     *
     * @param savedInstanceState Allows for saving the state of of the application without
     *                           persisting data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheres_my_car_main);
        getSupportActionBar().hide();


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

                clickedPosition = position;
                AlertDialog.Builder alert = new AlertDialog.Builder(WheresMyCarMainActivity.this);
                alert.setTitle("Confirm Deletion");
                alert.setMessage("Are you sure you want to delete this location?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                int locationId = locationArray[clickedPosition].get_id();
                                PointOfInterest pointOfInterest = new PointOfInterest();
                                pointOfInterest.delete(locationId, WheresMyCarMainActivity.this);
                                refreshList();
                                dialog.dismiss();

                            }
                        }
                );

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });


                alert.show();
                refreshList();
                return true;


            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Location selectedLoc = new Location("selectedLoc");
                selectedLoc.setLatitude(locationArray[position].getLatitude());
                selectedLoc.setLongitude(locationArray[position].getLongitude());

                Intent intent = new Intent(WheresMyCarMainActivity.this, WheresMyCarTravelActivity.class);
                intent.putExtra("currentLocation", selectedLoc);
                intent.putExtra("locationName", locationArray[position].getInfo());
                startActivity(intent);
            }
        });
    }

    /**
     * Method for building an alert dialog which informs the user that their GPS is switched off
     * and asks if they would like to enable it.
     */
    private void buildAlertMessageNoGps()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id)
                    {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    public void onClick(final DialogInterface dialog,
                                        @SuppressWarnings("unused") final int id)
                    {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * A method which builds and alert to tell users that their device is still searching for a GPS
     * signal and advises them to wait until a GPS lock has been obtained.
     */
    private void gpsLoadingAlert()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your phone is still trying to get a location lock. Please wait.")
                .setCancelable(false)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener()
                {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog,
                                        @SuppressWarnings("unused") final int id)
                    {

                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Called when the App is resumed and allows users to interact with the app again.
     */
    @Override
    protected void onResume()
    {
        super.onResume();

        refreshList();

        if (WheresMyCarTravelActivity.arrived)
        {

            buildAlertMessageArrived("You have reached your destination.");
            WheresMyCarTravelActivity.arrived = false;
        }
    }

    /**
     * Called as part of the activity lifecycle when an activity is going into the background, but has not (yet) been killed.
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        System.out.println("OnPause");

    }

    /**
     * Called when the activity is no longer visible to the user.
     */
    @Override
    protected void onStop()
    {
        super.onStop();
        System.out.println("OnStop");
    }

    /**
     * Method used for controlling our custom list adapters
     * @param menu The options menu in which to place items
     * @return True to display the menu, false otherwise.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wheres_my_car_main, menu);
        return true;
    }

    /**
     * Method run when a menu item is selected.
     *
     * @param item The menu item that was selected
     * @return Return false to allow normal menu processing to proceed, true to consume it here.
     */
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

    /**
     * Method which runs when the save location button is clicked. The current location is obtained
     * and persisted to the database for later navigation.
     * @param view view The view that has been clicked
     */
    public void saveLocationClicked(View view)
    {
        LocationManager manager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            buildAlertMessageNoGps();

        }
        else if (MainActivity.pm.getCurrentPosition() == null)
        {
            gpsLoadingAlert();
        }
        else
        {
            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.wmc_location_dialog, null);
            alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setView(promptsView);

            final EditText userLocationInput = (EditText) promptsView.findViewById(R.id.locationNameInput);
            alertDialogBuilder
                    .setCancelable(false)
                    .setTitle("Enter Location Name:")
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {

                                    String locName = userLocationInput.getText().toString();
                                    System.out.println("LOCNAME: " + locName);
                                    lat = MainActivity.pm.getCurrentPosition().getLatitude();
                                    lon = MainActivity.pm.getCurrentPosition().getLongitude();

                                    location = new PointOfInterest(lon, lat, locName);
                                    location.persist(WheresMyCarMainActivity.this);
                                    refreshList();
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    dialog.cancel();
                                }
                            });
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
            refreshList();
        }
    }

    /**
     * A method which automatically updates a list displayed to a user so that new additions or
     * deletions are immediately shown to the user.
     */
    private void refreshList()
    {
        locationArray = new PointOfInterest().retrieve(this);
        listAdapter = new CustomLocationListAdapter(this, locationArray);
        listView = (ListView) findViewById(R.id.custom_location_list);
        listView.setAdapter(listAdapter);
    }

    private void buildAlertMessageArrived(String message)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    public void onClick(final DialogInterface dialog,
                                        @SuppressWarnings("unused") final int id)
                    {
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
