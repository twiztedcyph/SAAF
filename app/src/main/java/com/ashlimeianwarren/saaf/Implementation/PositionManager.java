package com.ashlimeianwarren.saaf.Implementation;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.ashlimeianwarren.saaf.Beans.WheresMyCar.PointOfInterest;
import com.ashlimeianwarren.saaf.Framework.Positioning;

/**
 * Created by Cypher on 08/03/2015.
 */
public class PositionManager implements Positioning
{
    private LocationListener locationListener;

    private Location location;
    private Context context;
    private LocationManager locationManager;
    private PointOfInterest pointOfInterest;

    public PositionManager(Context context)
    {
        this.context = context;
    }

    public void start()
    {
        locationListener = new LocationListener()
        {
            @Override
            public void onLocationChanged(Location location)
            {
                PositionManager.this.location = location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras)
            {

            }

            @Override
            public void onProviderEnabled(String provider)
            {

            }

            @Override
            public void onProviderDisabled(String provider)
            {

            }
        };

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
    }

    @Override
    public Location getCurrentPosition()
    {
        return location;
    }

    @Override
    public double getDistToLocation(Location location)
    {
        return this.location.distanceTo(location);
    }

    @Override
    public double getBearingToLocation(Location location)
    {
        return this.location.bearingTo(location);
    }

    public void saveLocation(String poiText)
    {
        pointOfInterest = new PointOfInterest(location.getLongitude(),
                location.getLatitude(),
                poiText);

        pointOfInterest.persist(context);
    }

    public void close()
    {
        locationManager.removeUpdates(locationListener);
        locationManager = null;
    }

    private double rad2deg(double rad)
    {
        return (rad * 180 / Math.PI);
    }

    private double deg2rad(double deg)
    {
        return (deg * Math.PI / 180.0);
    }
}
