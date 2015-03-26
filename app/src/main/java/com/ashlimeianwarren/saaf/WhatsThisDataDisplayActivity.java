package com.ashlimeianwarren.saaf;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashlimeianwarren.saaf.Beans.WhatsThis.Data;
import com.ashlimeianwarren.saaf.Beans.WhatsThis.DataImage;
import com.ashlimeianwarren.saaf.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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
                Bitmap bitmap = decodeFile(image);

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

    private Bitmap decodeFile(File f){
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //The new size we want to scale to
            final int REQUIRED_SIZE=70;

            //Find the correct scale value. It should be the power of 2.
            int scale=1;
            while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
                scale*=2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }

    public void imageOneClicked(View v)
    {
        File file = new File(dataImages[0].getImagePath());
        Intent i = new Intent();
        i.setAction(android.content.Intent.ACTION_VIEW);
        i.setDataAndType(Uri.fromFile(file), "image/*");
        startActivity(i);
    }

    public void imageTwoClicked(View v)
    {
        File file = new File(dataImages[1].getImagePath());
        Intent i = new Intent();
        i.setAction(android.content.Intent.ACTION_VIEW);
        i.setDataAndType(Uri.fromFile(file), "image/*");
        startActivity(i);
    }
}
