package com.ashlimeianwarren.saaf.Implementation;

import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashlimeianwarren.saaf.R;

import java.io.IOException;
import java.io.InputStream;

public class WhatsThisImageDisplayActivity extends ActionBarActivity
{
    private TextView imageDescription;
    private ImageView imageDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_this_image_display);

        imageDescription = (TextView) findViewById(R.id.imageDescDisplay);
        imageDisplay = (ImageView) findViewById(R.id.mainImageDisplay);

        Bundle extras = getIntent().getExtras();

        String desc = extras.getString("imageDesc");
        String name = extras.getString("imageName");

        // get input stream
        InputStream ims = null;
        try
        {
            ims = getAssets().open(name);
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            imageDisplay.setImageDrawable(d);
            imageDescription.setText(desc);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        // load image as Drawable

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_whats_this_image_display, menu);
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
