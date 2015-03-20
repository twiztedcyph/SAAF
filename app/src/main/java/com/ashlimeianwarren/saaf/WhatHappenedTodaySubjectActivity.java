package com.ashlimeianwarren.saaf;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.ashlimeianwarren.saaf.Beans.WhatHappenedToday.MediaNote;
import com.ashlimeianwarren.saaf.Beans.WhatHappenedToday.Note;
import com.ashlimeianwarren.saaf.Beans.WhatHappenedToday.TextNote;
import com.ashlimeianwarren.saaf.Implementation.MediaCapture;

import java.io.File;


public class WhatHappenedTodaySubjectActivity extends ActionBarActivity
{

    AlertDialog.Builder alertDialogBuilder;
    private Note[] noteArray;
    private ListAdapter listAdapter;
    private ListView listView;
    private int subjectId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_happened_today_subject);
        //Retrieving the subject id passed with the intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            subjectId = extras.getInt("subjectId");
        }

        noteArray = new MediaNote().retrieve(subjectId, this);
        listAdapter = new CustomNoteListAdapter(this, noteArray);
        listView = (ListView) findViewById(R.id.NoteActivityListView);
        listView.setAdapter(listAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                //TODO OPEN THE FILE

                switch(noteArray[position].getType())
                {
                    case "Text":
                        TextNote tNote = (TextNote) noteArray[position];

                        break;
                    case "Audio":
                        MediaNote mNote = (MediaNote) noteArray[position];

                        break;
                    case "Image":
                        MediaNote iNote = (MediaNote) noteArray[position];
                        File image = new File(iNote.getFilePath());
                        Intent i = new Intent();
                        i.setAction(android.content.Intent.ACTION_VIEW);
                        i.setDataAndType(Uri.fromFile(image), "image/");
                        startActivity(i);


                        break;
                    default:


                }

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                //TODO delete a note with a long click....
                int noteId =  noteArray[position].get_id();
                if(noteArray[position].getType() .equals("Text") )
                {
                    TextNote t = (TextNote) noteArray[position];
                    t.delete(noteId, WhatHappenedTodaySubjectActivity.this);
                }
                else
                {
                    MediaNote m = (MediaNote) noteArray[position];
                    m.delete(noteId, WhatHappenedTodaySubjectActivity.this);
                }

                refreshList();
                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_what_happened_today_note, menu);
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

    public void newAudioNote(View view)
    {

    }

    public void newImageNote(View view)
    {
        System.out.println("Image Note CLicked");
        MediaCapture image = new MediaCapture(this);
        String imageFile = image.captureImage();
        MediaNote iNote = new MediaNote("Image",imageFile,subjectId,"Image");
        iNote.persist(this);
        refreshList();
        System.out.println(iNote);

    }

    public void newTextNote(View view)
    {

    }

    private void refreshList()
    {
        noteArray = new MediaNote().retrieve(subjectId, WhatHappenedTodaySubjectActivity.this);
        listAdapter = new CustomNoteListAdapter(WhatHappenedTodaySubjectActivity.this, noteArray);
        listView = (ListView) findViewById(R.id.NoteActivityListView);
        listView.setAdapter(listAdapter);
    }
}
