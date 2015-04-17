package com.ashlimeianwarren.saaf;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ashlimeianwarren.saaf.Beans.WhatHappenedToday.TextNote;

/**
 * Class used to interact with a user when they are viewing available notes.
 */
public class WhatHappendTodayNoteActivity extends ActionBarActivity
{
    private EditText textNoteInput;
    private TextView titleText;
    private int subjectId, noteId;
    private String currentText, textName;

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
        setContentView(R.layout.activity_what_happend_today_note);
        getSupportActionBar().hide();
    }

    /**
     * Called when the App is resumed and allows users to interact with the app again.
     */
    @Override
    protected void onResume()
    {
        super.onResume();

        textNoteInput = (EditText) findViewById(R.id.textNoteInput);
        titleText = (TextView) findViewById(R.id.wht_note_title);

        Intent intent = getIntent();
        subjectId = intent.getIntExtra("subjectId", 0);
        // TODO: Should never be 0.... must test fr this at some stage...
        if (subjectId == 0)
        {
            System.out.println("ZERRROOOOOOOO!!!!");
        }

        noteId = intent.getIntExtra("noteId", 0);

        currentText = intent.getStringExtra("currentText");
        textName = intent.getStringExtra("noteName");

        titleText.setText(textName);
        textNoteInput.setText(currentText);
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
        getMenuInflater().inflate(R.menu.menu_what_happened_today_note, menu);
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
     * Method for handling when the first "Save Text" button is clicked. Depending on whether the
     * user has created a new TextNote or is editing an old one a TextNote is either created and
     * persisted to the database or updated.
     *
     * @param view The view that has been clicked
     */
    public void saveTextClicked(View view)
    {
        System.out.println("NoteId: " + noteId);

        if (textNoteInput.getText().toString().isEmpty())
        {
            textNoteInput.setText(" ");
        }

        if (noteId > 0)
        {
            System.out.println("update");
            TextNote textNote = new TextNote(noteId, textName, textNoteInput.getText().toString(), subjectId, "Text");
            textNote.update(this);
        }
        else
        {
            System.out.println("persist");
            TextNote textNote = new TextNote(textName, textNoteInput.getText().toString(), subjectId, "Text");

            textNote.persist(this);
            System.out.println(textNote);
        }
        finish();
    }
}
