package com.ashlimeianwarren.saaf;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.ashlimeianwarren.saaf.Beans.WhatHappenedToday.Subject;
import com.ashlimeianwarren.saaf.Framework.Input;
import com.ashlimeianwarren.saaf.Implementation.MultiTouchHandler;


public class WhatHappenedTodayMainActivity extends ActionBarActivity
{

    private MultiTouchHandler multiTouchHandler;
    AlertDialog.Builder alertDialogBuilder;
    private Subject[] subjectArray;
    private ListAdapter listAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_happened_today_main);
        subjectArray = new Subject().retrieve(this);
        listAdapter = new CustomFolderListAdapter(this, subjectArray);
        listView = (ListView) findViewById(R.id.mainActivity_listView);
        listView.setAdapter(listAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                //TODO SEND TO WHAT HAPPENED TODAY NOTE ACTIVITY
                Intent intent = new Intent(WhatHappenedTodayMainActivity.this, WhatHappenedTodayNoteActivity.class);
                int subjectId =  subjectArray[position].get_id();
                intent.putExtra("subjectId",subjectId);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                //TODO delete a subject with a long click....
                int subjectId =  subjectArray[position].get_id();
                subjectArray[position].delete(subjectId, WhatHappenedTodayMainActivity.this);

                subjectArray = new Subject().retrieve(WhatHappenedTodayMainActivity.this);
                listAdapter = new CustomFolderListAdapter(WhatHappenedTodayMainActivity.this, subjectArray);
                listView = (ListView) findViewById(R.id.mainActivity_listView);
                listView.setAdapter(listAdapter);
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_what_happened_today_main, menu);
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

    public void newFolderClicked(View view)
    {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.wht_subject_dialog, null);
        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        alertDialogBuilder
                .setCancelable(false)
                .setTitle("Enter Folder Name:")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                Subject s = new Subject();
                                s.setTitle(userInput.getText().toString());

                                s.persist(WhatHappenedTodayMainActivity.this);

                                subjectArray = new Subject().retrieve(WhatHappenedTodayMainActivity.this);
                                listAdapter = new CustomFolderListAdapter(WhatHappenedTodayMainActivity.this, subjectArray);
                                listView = (ListView) findViewById(R.id.mainActivity_listView);
                                listView.setAdapter(listAdapter);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.cancel();
                            }
                        });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}
