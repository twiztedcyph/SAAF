package com.ashlimeianwarren.saaf;

/**
 * What's This Data Display Activity.
 *
 * Activity for controlling the display of data once an NFC tag has been scanned.
 */
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ashlimeianwarren.saaf.Beans.WhatHappenedToday.Subject;
import com.ashlimeianwarren.saaf.Beans.WhatHappenedToday.TextNote;
import com.ashlimeianwarren.saaf.Beans.WhatsThis.Data;
import com.ashlimeianwarren.saaf.Beans.WhatsThis.DataImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class WhatsThisDataDisplayActivity extends ActionBarActivity
{

    TextView title;
    TextView description;
    ImageView imageViews[];

    Data data;
    DataImage dataImages[];

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
        setContentView(R.layout.activity_whats_this_data_display);
        getSupportActionBar().hide();

        //For the moment we are limiting each NFC tag to only storing and displaying two images.
        imageViews = new ImageView[2];

        title = (TextView) findViewById(R.id.wt_display_title);
        description = (TextView) findViewById(R.id.txtDescription);

        imageViews[0] = (ImageView) findViewById(R.id.imgOne);
        imageViews[1] = (ImageView) findViewById(R.id.imgTwo);

        //TODO check string exists

        //Get the dataString passed across which was read from the NFC tag
        String dataString = getIntent().getStringExtra("dataName");
        System.out.println("Data String == " + dataString);
        data = new Data();

        //Find the data that matches the scanned String's title
         data.retrieve(dataString, this);
         dataImages = new DataImage().retrieve(data.get_id(), this);

        if(data.getDataName() == null || data.getDataName().equals("null"))
        {

            Toast.makeText(this, "NFC Tag invalid, look for the UEA logo for a vaild tag", Toast.LENGTH_SHORT).show();
            finish();

            return;
        }

        System.out.println(data);
        System.out.println(dataImages);

        System.out.println("Data Images Size: " + dataImages.length);
        title.setText(data.getDataName());
        description.setText(data.getDataDescription());

        for (int i = 0; i < imageViews.length; i++)
        {
            //Get a bitmap image from the file path
            Bitmap bitmap = decodeFile(dataImages[i].getImagePath());

            imageViews[i].setImageBitmap(bitmap);
        }
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
        getMenuInflater().inflate(R.menu.menu_whats_this_data_display, menu);
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
     * Method for transforming a File into a bitmap image
     *
     * @param f File to be transformed
     * @return  Bitmap image from the File
     */
    private Bitmap decodeFile(File f)
    {
        try
        {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            //The new size we want to scale to
            final int REQUIRED_SIZE = 70;

            //Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e)
        {
        }
        return null;
    }

    /**
     * Method to find and decode a bitmap image from a given file path.
     *
     * @param fileName File path from which to find Bitmap Image.
     * @return         Bitmap image from file path.
     */
    private Bitmap decodeFile(String fileName)
    {
        try
        {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            InputStream is = getAssets().open(fileName);
            BitmapFactory.decodeStream(is, null, o);

            //The new size we want to scale to
            final int REQUIRED_SIZE = 70;

            //Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            is.reset();
            return BitmapFactory.decodeStream(is, null, o2);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method for handling when the first image is clicked
     * @param v The view that has been clicked
     */
    public void imageOneClicked(View v)
    {
        String name = dataImages[0].getImagePath();
        String desc = dataImages[0].getImageTitle();
        Intent intent = new Intent(this, WhatsThisImageDisplayActivity.class);
        intent.putExtra("imageName", name);
        intent.putExtra("imageDesc", desc);
        startActivity(intent);
    }

    /**
     * Method for handling when the second image is clicked
     * @param v The view that has been clicked
     */
    public void imageTwoClicked(View v)
    {
        String name = dataImages[1].getImagePath();
        String desc = dataImages[1].getImageTitle();
        Intent intent = new Intent(this, WhatsThisImageDisplayActivity.class);
        intent.putExtra("imageName", name);
        intent.putExtra("imageDesc", desc);
        startActivity(intent);
    }

    public void saveAsNoteClicked(View view)
    {
        Subject subject = new Subject();
        subject.setTitle(data.getDataName());
        subject.persist(this);

        TextNote textNote = new TextNote();
        textNote.setSubjectId(subject.get_id());
        textNote.setTextName("Description");
        textNote.setTextNote(data.getDataDescription());
        textNote.setType("text");
        textNote.persist(this);
        Toast toast = Toast.makeText(getApplicationContext(), "Saved as note", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 400);
        toast.show();
    }
}
