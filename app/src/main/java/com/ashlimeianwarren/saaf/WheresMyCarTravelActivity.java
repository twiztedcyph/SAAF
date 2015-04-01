package com.ashlimeianwarren.saaf;

import android.hardware.GeomagneticField;
import android.hardware.SensorManager;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.hardware.SensorEventListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;

import com.ashlimeianwarren.saaf.Implementation.PositionManager;


public class WheresMyCarTravelActivity extends ActionBarActivity implements SensorEventListener
{

    private double carLon = 0;
    private double carLat = 0;
    private double distance = 0;
    private double currentLon = 0;
    private double currentLat = 0;
    private double bearing = 0;
    private PositionManager pm;
    private Location oldLocation = null;
    private Location currentLocation = null;
    private float mCurrentDegree = 230f;
    private double roll;
    private double pitch;
    private double azimuth;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];


    private ImageView mPointer;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheres_my_car_travel);

        pm = new PositionManager(this);
        //Getting the location object passed from the previous activity
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            oldLocation = (Location) extras.get("currentLocation");

        }

        TextView distanceDisplay = (TextView) findViewById(R.id.textView2);
        carLon = oldLocation.getLongitude();
        carLat = oldLocation.getLatitude();
        distanceDisplay.setText(carLat + "\n" + carLon);
        //Initialising the sensors
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mPointer = (ImageView) findViewById(R.id.pointer);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        pm.start();
        //Register the sensor listeners when app is paused
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        System.out.println("OnPause");
        pm.close();
        //Unregister the sensor listeners when app is paused
        mSensorManager.unregisterListener(this, mAccelerometer);
        mSensorManager.unregisterListener(this, mMagnetometer);

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
        getMenuInflater().inflate(R.menu.menu_wheres_my_car_travel, menu);
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

    public void getDistance(View view)
    {
        //Gets the current longitude and latitude of current position
        currentLat = pm.getCurrentPosition().getLatitude();
        currentLon = pm.getCurrentPosition().getLongitude();
        //Gets the current location object
        currentLocation = pm.getCurrentPosition();
        //Calculates distance between old and current location
        distance = pm.getDistToLocation(oldLocation);
        TextView distanceDisplay = (TextView) findViewById(R.id.textView2);
        //bearing = pm.getBearingToLocation(oldLocation);
        distanceDisplay.setText("Bearing = " + bearing);
    }

    public void getArrowAngle(View view)
    {
        TextView angleDisplay = (TextView) findViewById(R.id.textView3);
        //Gets the current location
        currentLocation = pm.getCurrentPosition();
        //Calculates the bearing to the destination location from the current location
        bearing = currentLocation.bearingTo(oldLocation);
        //Converting the degrees into 0-360
        if (bearing < 0)
        {
            bearing += 360;
        }
        //Displaying data
        angleDisplay.setText("Angle = " + bearing);
        TextView distanceDisplay = (TextView) findViewById(R.id.textView2);
        distanceDisplay.setText("Azimuth = " + azimuth + "\n" +
                "Bearing = " + bearing + "\n");
    }


    @Override
    public void onSensorChanged(SensorEvent event)
    {
        //Break out if no location found
        if (currentLocation == null)
        {
            return;
        }

        //Checking which sensors have changed
        if (event.sensor == mAccelerometer)
        {
            //Copy the values from the sensor into accelorometer array
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            //Sets the indicator to true if sensor has been changed
            mLastAccelerometerSet = true;
            //Same tests for the magnetometer
        }
        else if (event.sensor == mMagnetometer)
        {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;

        }
        //If both sensors have been changed
        if (mLastAccelerometerSet && mLastMagnetometerSet)
        {

            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mR, mOrientation);
            //We only need the azimuth value so we obtain this from the 1st position from the orientation
            //array
            float azimuthInRadians = mOrientation[0];
            //Convert the azimuth into the degrees
            float azimuthInDegress = (float) (Math.toDegrees(azimuthInRadians) + 360) % 360;
            //Creating a geomagnetic field from the current location
            GeomagneticField geoField = new GeomagneticField(
                    (float) currentLocation.getLatitude(),
                    (float) currentLocation.getLongitude(),
                    (float) currentLocation.getAltitude(),
                    System.currentTimeMillis()

            );
            //True north is calculated using declination which is subtracted from the azimuth value
            azimuthInDegress -= geoField.getDeclination();
            //Updating the current location
            currentLocation = pm.getCurrentPosition();
            //Updating the bearing
            float bearing = currentLocation.bearingTo(oldLocation);
            //Converting bearing into 0-360 degrees
            if (bearing < 0)
            {
                bearing += 360;
            }
            //Calculating the direction the phone is heading by subtracting the bearing from the
            //azimuth value
            float direction = azimuthInDegress - bearing;
            //Converting the direction into 0-360 degrees
            if (direction < 0)
            {
                direction += 360;
            }
            //Rotating the image
            RotateAnimation ra = new RotateAnimation(
                    mCurrentDegree,
                    -direction,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f);
            //Duration of animation in ms
            ra.setDuration(250);

            ra.setFillAfter(true);
            //mPointer is the imageview of the activity, start the animation
            mPointer.startAnimation(ra);
            //Setting the currentdegrees to the minus value of the direction the phone has moved
            mCurrentDegree = -direction;

        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }


}
