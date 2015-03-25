package com.ashlimeianwarren.saaf;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ashlimeianwarren.saaf.Beans.WhatsThis.*;
import com.ashlimeianwarren.saaf.Implementation.DbCon;

public class WhatsThisPlacesActivity extends ActionBarActivity
{
    private Tag currentTag;
    private TextView title;
   // private TextView description;
    private Data[] dataArray;
    private ListAdapter listAdapter;
    private ListView subList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_this_places);

        title = (TextView) findViewById(R.id.txtTitle);

        String tagName = getIntent().getStringExtra("tagID");
        currentTag = new Tag().retrieve(tagName,this);

        int tagID = currentTag.get_id();

        if(tagID == -1)
        {
            //TODO toast
            finish();
        }

        title.setText(currentTag.getTagText());

        dataArray =new Data().retrieve(tagID, this);

        subList = (ListView) findViewById(R.id.whatsThisList);
        listAdapter = new CustomDataListAdapter(this, dataArray);
        subList.setAdapter(listAdapter);


        subList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(WhatsThisPlacesActivity.this, WhatsThisDataDisplayActivity.class);
                String dataName =  dataArray[position].getDataName();
                intent.putExtra("dataName",dataName);
                startActivity(intent);
            }
        });

    }

    protected void onResume()
    {
        super.onResume();
        String tagName = getIntent().getStringExtra("tagID");
        currentTag = new Tag().retrieve(tagName,this);

        int tagID = currentTag.get_id();

        dataArray =new Data().retrieve(tagID, this);

        //description.setText(dataArray[0].getDataDescription());
        //title.setText(dataArray[0].getDataName());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_whats_this_places, menu);
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
