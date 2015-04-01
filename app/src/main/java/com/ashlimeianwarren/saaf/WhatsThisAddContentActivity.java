package com.ashlimeianwarren.saaf;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ashlimeianwarren.saaf.Beans.WhatsThis.Data;
import com.ashlimeianwarren.saaf.Beans.WhatsThis.DataImage;
import com.ashlimeianwarren.saaf.Beans.WhatsThis.Tag;
import com.ashlimeianwarren.saaf.Implementation.MediaCapture;


public class WhatsThisAddContentActivity extends ActionBarActivity
{
    private EditText tagName, dataTitle, dataText, photoTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_this_add_content);

        tagName = (EditText) findViewById(R.id.tagNameInput);
        dataTitle = (EditText) findViewById(R.id.dataTitleInput);
        dataText = (EditText) findViewById(R.id.dataTextInput);
        photoTitle = (EditText) findViewById(R.id.photoTitleInput);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_whats_this_add_content, menu);
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

    public void savePhotoClicked(View view)
    {
        String dt = dataTitle.getText().toString();
        String pt = photoTitle.getText().toString();
        /*
        So in order to make this i need to first have a tag and data object....
        Tag is only needed so the data object can exist though...
         */
        Data data = new Data();
        data.retrieve(dt, this);

        if (data.get_id() > 0 && !pt.isEmpty())
        {
            MediaCapture image = new MediaCapture(this);
            String imageFile = image.captureImage();
            DataImage dataImage = new DataImage(pt, imageFile, data.get_id());
            dataImage.persist(this);
            System.out.println("Saving: " + dataImage);
            System.out.println("Data: " + data);
            System.out.println("IMAGE SAVED");
        }
        else
        {
            System.out.println("DATA OBJECT NOT FOUND");
        }
    }

    public void saveTextClicked(View view)
    {
        String tt = tagName.getText().toString();
        String dt = dataTitle.getText().toString();
        String dtxt = dataText.getText().toString();

        Tag tag = new Tag();
        tag.retrieveNew(tt, this);

        if (tag.get_id() > 0 && !dt.isEmpty() && !dtxt.isEmpty())
        {
            Data data = new Data(dt, dtxt, tag.get_id());
            data.persist(this);

            System.out.println("DATA SAVED");
        }
        else
        {
            System.out.println("TAG RECORD NOT FOUND");
        }
    }

    public void saveTagClicked(View view)
    {
        String tt = tagName.getText().toString();

        if (!tt.isEmpty())
        {
            Tag tag = new Tag(tt);
            tag.persist(this);

            System.out.println("TAG SAVED");
        }
        else
        {
            System.out.println("TAG TEXT IS EMPTY");
        }
    }

}
