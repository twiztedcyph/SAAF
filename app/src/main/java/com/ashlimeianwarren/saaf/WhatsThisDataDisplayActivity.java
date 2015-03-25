package com.ashlimeianwarren.saaf;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashlimeianwarren.saaf.Beans.WhatsThis.Data;
import com.ashlimeianwarren.saaf.Beans.WhatsThis.DataImage;
import com.ashlimeianwarren.saaf.R;

import java.io.File;

public class WhatsThisDataDisplayActivity extends ActionBarActivity
{

    TextView title;
    TextView description;
    ImageView imageViews[];

    Data data;
    DataImage dataImages[];

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_this_data_display);

        imageViews = new ImageView[2];

        title = (TextView) findViewById(R.id.txtTitle);
        description = (TextView) findViewById(R.id.txtDescription);

        imageViews[0] = (ImageView) findViewById(R.id.imgOne);
        imageViews[1] = (ImageView) findViewById(R.id.imgTwo);
        //TODO check string exists
        String dataString = getIntent().getStringExtra("dataName");
        System.out.println("Data String == "+dataString);
        data = new Data();

        data.retrieve(dataString, this);

        dataImages = new DataImage().retrieve(data.get_id(), this);
        System.out.println("Data Images Size: "+dataImages.length);
        title.setText(data.getDataName());
        description.setText(data.getDataDescription());

        for(int i = 0; i < imageViews.length; i++)
        {
            File image = new File(dataImages[i].getImagePath());
            if(image.exists())
            {
                Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());

                imageViews[i].setImageBitmap(bitmap);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_whats_this_data_display, menu);
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
