package com.ashlimeianwarren.saaf;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Wheres my car travel activity.
 */
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
    private Location oldLocation;
    private Location currentLocation;
    float azimuthInRadians;
    float azimuthInDegress;
    private final float ALPHA = 0.25f;
    int[] pointers;
    public static boolean arrived = false;


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
        setContentView(R.layout.activity_wheres_my_car_travel);
        getSupportActionBar().hide();

        pointers = new int[4];
        pointers[0] = R.drawable.greenpointer;
        pointers[1] = R.drawable.orangepointer;
        pointers[2] = R.drawable.redpointer;
        pointers[3] = R.drawable.pointericon;

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
        pointerTwo.setImageResource(pointers[3]);
        distanceDisplay = (TextView) findViewById(R.id.distDisplay);
        destinationDisplay = (TextView) findViewById(R.id.destDisplay);
        destinationDisplay.setText(name);
    }


    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.
     *
     * <p>This is only called once, the first time the options menu is
     * displayed.  To update the menu every time it is displayed, see
     * {@link #onPrepareOptionsMenu}.
     *
     * <p>The default implementation populates the menu with standard system
     * menu items.  These are placed in the {@link Menu#CATEGORY_SYSTEM} group so that
     * they will be correctly ordered with application-defined menu items.
     * Deriving classes should always call through to the base implementation.
     *
     * <p>You can safely hold on to <var>menu</var> (and any items created
     * from it), making modifications to it as desired, until the next
     * time onCreateOptionsMenu() is called.
     *
     * <p>When you add items to the menu, you can implement the Activity's
     * {@link #onOptionsItemSelected} method to handle them there.
     *
     * @param menu The options menu in which you place your items.
     *
     * @return You must return true for the menu to be displayed;
     *         if you return false it will not be shown.
     *
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wheres_my_car_test, menu);
        return true;
    }

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.  This means
     * that in some cases the previous state may still be saved, not allowing
     * fragment transactions that modify the state.  To correctly interact
     * with fragments in their proper state, you should instead override
     * {@link #onResumeFragments()}.
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, magne, SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        sensorManager.unregisterListener(this, accel);
        sensorManager.unregisterListener(this, magne);
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     *
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     *
     * @return boolean Return false to allow normal menu processing to
     *         proceed, true to consume it here.
     *
     * @see #onCreateOptionsMenu
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
     * Called when sensor values have changed.
     * <p>See {@link android.hardware.SensorManager SensorManager}
     * for details on possible sensor types.
     * <p>See also {@link android.hardware.SensorEvent SensorEvent}.
     *
     * <p><b>NOTE:</b> The application doesn't own the
     * {@link android.hardware.SensorEvent event}
     * object passed as a parameter and therefore cannot hold on to it.
     * The object may be part of an internal pool and may be reused by
     * the framework.
     *
     * @param event the {@link android.hardware.SensorEvent SensorEvent}.
     */
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
        if (MainActivity.pm.getCurrentPosition() != null)
        {
            currentLocation = MainActivity.pm.getCurrentPosition();
            double dist = currentLocation.distanceTo(oldLocation);

            if(dist < 5)
            {
                //Intent intent = new Intent(WheresMyCarTravelActivity.this, WheresMyCarMainActivity.class);
                this.arrived = true;
                //startActivity(intent);
                finish();
            }

            float test = currentDegree + (float) MainActivity.pm.getBearingToLocation(oldLocation);
            test = (test + 360) % 360;

            if(test > 30 && test < 330)
            {
                pointerTwo.setImageResource(pointers[2]);
            }
            else if(test > 15 && test < 345)
            {
                pointerTwo.setImageResource(pointers[1]);
            }
            else
            {
                pointerTwo.setImageResource(pointers[0]);
            }

            distanceDisplay.setText(String.format("%d meters", (int)dist));

            RotateAnimation ra = new RotateAnimation(
                    currentDegree + (float) MainActivity.pm.getBearingToLocation(oldLocation),
                    (-azimuthInDegress + (float) MainActivity.pm.getBearingToLocation(oldLocation)),
                    Animation.RELATIVE_TO_SELF,
                    0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f);
            ra.setDuration(2000);

            ra.setFillAfter(true);

            pointerTwo.startAnimation(ra);
        }
    }

    /**
     * Called when the accuracy of the registered sensor has changed.
     *
     * <p>See the SENSOR_STATUS_* constants in
     * {@link android.hardware.SensorManager SensorManager} for details.
     *
     * @param accuracy The new accuracy of this sensor, one of
     *         {@code SensorManager.SENSOR_STATUS_*}
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        // Not used but required for the implementation.
    }

    /**
     * A low pass filter to remove the jitter caused by the accelerometer.
     *
     * @param input The input array.
     * @param output The output array.
     * @return The filtered output array.
     */
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
