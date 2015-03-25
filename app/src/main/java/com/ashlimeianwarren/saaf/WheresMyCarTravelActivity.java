package com.ashlimeianwarren.saaf;

import android.content.Intent;
import android.hardware.GeomagneticField;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.hardware.SensorEventListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import com.ashlimeianwarren.saaf.Beans.WheresMyCar.PointOfInterest;
import com.ashlimeianwarren.saaf.Implementation.PositionManager;


public class WheresMyCarTravelActivity extends ActionBarActivity implements SensorEventListener{

    private double carLon = 0;
    private double carLat = 0;
    private double distance = 0;
    private double currentLon = 0;
    private double currentLat = 0;
    private double bearing = 0;
    private PositionManager pm;
    private Location oldLocation = null;
    private Location currentLocation = null;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheres_my_car_travel);
        senSensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
        pm = new PositionManager(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            oldLocation = (Location)extras.get("currentLocation");

        }

        TextView distanceDisplay = (TextView) findViewById(R.id.textView2);
        carLon = oldLocation.getLongitude();
        carLat = oldLocation.getLatitude();
        distanceDisplay.setText(carLat + "\n" + carLon);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        pm.start();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        System.out.println("OnPause");
        pm.close();
        senSensorManager.unregisterListener(this);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        System.out.println("OnStop");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wheres_my_car_travel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getDistance(View view)
    {
        currentLat = pm.getCurrentPosition().getLatitude();
        currentLon = pm.getCurrentPosition().getLongitude();
        currentLocation = pm.getCurrentPosition();
        distance = pm.getDistToLocation(oldLocation);
        TextView distanceDisplay = (TextView) findViewById(R.id.textView2);
        bearing = pm.getBearingToLocation(oldLocation);
        distanceDisplay.setText("Distance = "+distance+"\n"+
                            "Bearing = "+bearing);
    }

    public void getArrowAngle(View view)
    {
        TextView angleDisplay = (TextView) findViewById(R.id.textView3);

        double heading = 0;
        //float baseAzimuth = azimuth;

        GeomagneticField geoField = new GeomagneticField( Double
                .valueOf( currentLocation.getLatitude() ).floatValue(), Double
                .valueOf( currentLocation.getLongitude() ).floatValue(),
                Double.valueOf( currentLocation.getAltitude() ).floatValue(),
                System.currentTimeMillis() );
        heading += geoField.getDeclination();

        heading = bearing - (bearing + heading);
        Math.round(-heading / 360 + 180);
        angleDisplay.setText("Angle = "+heading);

    }


    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}
