package com.ashlimeianwarren.saaf;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ashlimeianwarren.saaf.Beans.WhatsThis.*;

/**
 * Class used to control the activity which displays a list of previously scanned NFC tags.
 */

public class WhatsThisPlacesActivity extends ActionBarActivity
{
    private Tag currentTag;
    private TextView title;
    // private TextView description;
    private Data[] dataArray;
    private ListAdapter listAdapter;
    private ListView subList;

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
        setContentView(R.layout.activity_whats_this_places);
        getSupportActionBar().hide();

        title = (TextView) findViewById(R.id.wt_display_title);

        String tagName = getIntent().getStringExtra("tagID");
        currentTag = new Tag().retrieve(tagName, this);

        int tagID = currentTag.get_id();

        if (tagID == -1)
        {
            //TODO toast
            finish();
        }

        title.setText(currentTag.getTagText());

        dataArray = new Data().retrieve(tagID, this);

        subList = (ListView) findViewById(R.id.whatsThisList);
        listAdapter = new CustomDataListAdapter(this, dataArray);
        subList.setAdapter(listAdapter);


        subList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(WhatsThisPlacesActivity.this, WhatsThisDataDisplayActivity.class);
                String dataName = dataArray[position].getDataName();
                intent.putExtra("dataName", dataName);
                startActivity(intent);
            }
        });

    }

    /**
     * Called when the App is resumed and allows users to interact with the app again.
     */
    protected void onResume()
    {
        super.onResume();
        String tagName = getIntent().getStringExtra("tagID");
        currentTag = new Tag().retrieve(tagName, this);

        int tagID = currentTag.get_id();

        dataArray = new Data().retrieve(tagID, this);

        //description.setText(dataArray[0].getDataDescription());
        //title.setText(dataArray[0].getDataName());

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
        getMenuInflater().inflate(R.menu.menu_whats_this_places, menu);
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
}
