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


public class WhatHappendTodayNoteActivity extends ActionBarActivity
{
    private EditText textNoteInput;
    private TextView titleText;
    private int subjectId, noteId;
    private String currentText, textName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_happend_today_note);
    }

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
