package com.ashlimeianwarren.saaf;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ashlimeianwarren.saaf.Beans.WhatHappenedToday.Subject;
import com.ashlimeianwarren.saaf.Implementation.MultiTouchHandler;

/**
 * Main Activity of the "What Happened Today" section of the app allowing for users to create,
 * view and open subject folders in which notes are stored.
 */
public class WhatHappenedTodayMainActivity extends ActionBarActivity
{
    private AlertDialog.Builder alertDialogBuilder;
    private Subject[] subjectArray;
    private ListAdapter listAdapter;
    private ListView listView;
    private int clickedPosition;

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
        setContentView(R.layout.activity_what_happened_today_main);
        subjectArray = new Subject().retrieve(this);
        listAdapter = new CustomFolderListAdapter(this, subjectArray);
        listView = (ListView) findViewById(R.id.wht_main_listview);
        listView.setAdapter(listAdapter);
        getSupportActionBar().hide();

        /**
         * Register a callback to be invoked when an item in this AdapterView has
         * been clicked.
         *
         * @param listener The callback that will be invoked.
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            /**
             * Callback method to be invoked when an item in this AdapterView has been clicked.
             *
             * @param parent    The AdapterView where the click happened.
             * @param view      The view within the AdapterView that was clicked (this will be a
             *                  view provided by the adapter)
             * @param position  The position of the view in the adapter.
             * @param id        The row id of the item that was clicked.
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                //TODO SEND TO WHAT HAPPENED TODAY NOTE ACTIVITY
                Intent intent = new Intent(WhatHappenedTodayMainActivity.this, WhatHappenedTodaySubjectActivity.class);
                int subjectId = subjectArray[position].get_id();
                intent.putExtra("subjectId", subjectId);
                startActivity(intent);
            }
        });

        /**
         * Register a callback to be invoked when an item in this AdapterView has
         * been clicked and held
         *
         * @param listener The callback that will run
         */
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            /**
             * Callback method to be invoked when an item in this view has been clicked and held.
             *
             * @param parent    The AbsListView where the click happened
             * @param view      The view within the AbsListView that was clicked
             * @param position  The position of the view in the list
             * @param id        The row id of the item that was clicked
             * @return          true if the callback consumed the long click, false otherwise
             */
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                clickedPosition = position;
                AlertDialog.Builder alert = new AlertDialog.Builder(WhatHappenedTodayMainActivity.this);
                alert.setTitle("Confirm Deletion");
                alert.setMessage("Are you sure you want to delete this folder?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                   @Override
                        public void onClick(DialogInterface dialog, int which)
                   {
                       //TODO delete a subject with a long click....
                       int subjectId = subjectArray[clickedPosition].get_id();
                       subjectArray[clickedPosition].delete(subjectId, WhatHappenedTodayMainActivity.this);

                       subjectArray = new Subject().retrieve(WhatHappenedTodayMainActivity.this);
                       listAdapter = new CustomFolderListAdapter(WhatHappenedTodayMainActivity.this, subjectArray);
                       listView = (ListView) findViewById(R.id.wht_main_listview);
                       listView.setAdapter(listAdapter);
                       dialog.dismiss();
                   }
                }
                );

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });


                alert.show();
                return true;
            }
        });

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
        getMenuInflater().inflate(R.menu.menu_what_happened_today_main, menu);
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
     * Method for creating a new subject folder in which notes can be stored and persisting it to
     * the database for later viewing.
     *
     * @param view The view that has been clicked
     */
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
                                listView = (ListView) findViewById(R.id.wht_main_listview);
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
