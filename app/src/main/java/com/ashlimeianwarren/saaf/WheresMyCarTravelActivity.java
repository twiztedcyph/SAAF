package com.ashlimeianwarren.saaf;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import com.ashlimeianwarren.saaf.Implementation.PositionManager;


public class WheresMyCarTravelActivity extends ActionBarActivity implements SensorEventListener
{
    private ImageView pointerTwo;
    private SensorManager sensorManager;
    private Sensor accel;
    private Sensor magne;
    private TextView distanceDisplay, destinationDisplay;
    private float[] lastAccelerometer = new float[3];
    private float[] lastMagnetometer = new float[3];
    private float[] adjLastAccelerometer = new float[3];
    private float[] adjLastMagnetometer = new float[3];
    private boolean lastAccelerometerSet = false;
    private boolean lastMagnetometerSet = false;
    private float[] mR = new float[9];
    private float[] orientation = new float[3];
    private float currentDegree = 0f;
    private PositionManager pm;
    private Location oldLocation;
    private Location currentLocation;
    float azimuthInRadians;
    float azimuthInDegress;
    private final float ALPHA = 0.25f;
    int[] pointers;
    public static boolean arrived = false;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheres_my_car_travel);

        pointers = new int[3];
        pointers[0] = R.drawable.greenpointer;
        pointers[1] = R.drawable.orangepointer;
        pointers[2] = R.drawable.redpointer;

        pm = new PositionManager(this);

        Bundle extras = getIntent().getExtras();
        String name = "";
        if (extras != null)
        {
            oldLocation = (Location) extras.get("currentLocation");
            name = extras.getString("locationName");
        }
        else
        {
            finish();
        }

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magne = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        pointerTwo = (ImageView) findViewById(R.id.pointerIconPicTwo);
        pointerTwo.setImageResource(pointers[0]);
        distanceDisplay = (TextView) findViewById(R.id.distDisplay);
        destinationDisplay = (TextView) findViewById(R.id.destDisplay);
        destinationDisplay.setText(name);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wheres_my_car_test, menu);
        return true;
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        pm.start();
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, magne, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        pm.close();
        sensorManager.unregisterListener(this, accel);
        sensorManager.unregisterListener(this, magne);
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

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if (event.sensor == accel)
        {
            System.arraycopy(event.values, 0, lastAccelerometer, 0, event.values.length);
            adjLastAccelerometer = lowPass(event.values.clone(), adjLastAccelerometer);
            lastAccelerometerSet = true;
        }
        else if (event.sensor == magne)
        {
            System.arraycopy(event.values, 0, lastMagnetometer, 0, event.values.length);
            adjLastMagnetometer = lowPass(event.values.clone(), adjLastMagnetometer);
            lastMagnetometerSet = true;
        }
        if (lastAccelerometerSet && lastMagnetometerSet)
        {
            SensorManager.getRotationMatrix(mR, null, adjLastAccelerometer, adjLastMagnetometer);
            SensorManager.getOrientation(mR, orientation);
            azimuthInRadians = orientation[0];
            azimuthInDegress = (float) (Math.toDegrees(azimuthInRadians) + 360) % 360;
            currentDegree = (int) -azimuthInDegress;
        }
        if (pm.getCurrentPosition() != null)
        {
            currentLocation = pm.getCurrentPosition();
            double dist = currentLocation.distanceTo(oldLocation);

            if(dist < 5)
            {
                //Intent intent = new Intent(WheresMyCarTravelActivity.this, WheresMyCarMainActivity.class);
                this.arrived = true;
                //startActivity(intent);
                finish();
            }

            distanceDisplay.setText(String.format("%5d meters", (int)dist));

            RotateAnimation ra = new RotateAnimation(
                    currentDegree + (float) pm.getBearingToLocation(oldLocation),
                    (-azimuthInDegress + (float) pm.getBearingToLocation(oldLocation)),
                    Animation.RELATIVE_TO_SELF,
                    0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f);
            ra.setDuration(2000);

            ra.setFillAfter(true);

            pointerTwo.startAnimation(ra);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        // Not used but required for the implementation.
    }

    private float[] lowPass(float[] input, float[] output)
    {
        if (output == null)
        {
            return input;
        }
        for (int i = 0; i < input.length; i++)
        {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }
}
