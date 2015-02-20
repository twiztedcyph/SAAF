package com.ashlimeianwarren.saaf.Framework;

import android.location.Location;

/**
 * Created by Twiz on 20/02/2015.
 */
public interface Positioning
{
    public Location getCurrentPosition();

    public double getDistToLocation(Location location);

    public  double getBearingToLocation(Location location);
}
