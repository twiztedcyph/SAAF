package com.ashlimeianwarren.saaf;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.ashlimeianwarren.saaf.Beans.WhatHappenedToday.MediaNote;
import com.ashlimeianwarren.saaf.Beans.WhatHappenedToday.Note;
import com.ashlimeianwarren.saaf.Beans.WhatHappenedToday.TextNote;
import com.ashlimeianwarren.saaf.Implementation.AndroidAudio;
import com.ashlimeianwarren.saaf.Implementation.AndroidMusic;
import com.ashlimeianwarren.saaf.Implementation.MediaCapture;

import java.io.File;
import java.util.Arrays;


public class WhatHappenedTodaySubjectActivity extends ActionBarActivity
{

    boolean mStartRecording = true;
    private Note[] noteArray, mediaArray, textArray;
    private ListAdapter listAdapter;
    private ListView listView;
    private int subjectId;
    private Button newAudioButton, newImageButton, newTextButton;
    private MediaCapture sound = null;
    private String soundFile = null;
    private int audioButtonWidth = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_happened_today_subject);
        //Retrieving the subject id passed with the intent
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            subjectId = extras.getInt("subjectId");
        }

        newAudioButton = (Button) findViewById(R.id.newAudioButton);
        newImageButton = (Button) findViewById(R.id.newImageButton);
        newTextButton = (Button) findViewById(R.id.newTextButton);
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
                        System.out.println(tNote);
                        Intent intent = new Intent(WhatHappenedTodaySubjectActivity.this, WhatHappendTodayNoteActivity.class);
                        intent.putExtra("subjectId", subjectId);
                        intent.putExtra("noteId", tNote.get_id());
                        intent.putExtra("currentText", tNote.getTextNote());
                        startActivity(intent);
                        break;
                    case "Audio":
                        MediaNote mNote = (MediaNote) noteArray[position];
                        AndroidAudio audio = new AndroidAudio(WhatHappenedTodaySubjectActivity.this);
                        AndroidMusic music = audio.createMusic(mNote.getFilePath());
                        music.play();
                        System.out.println(mNote.getFilePath());
                        break;
                    case "Image":
                        MediaNote iNote = (MediaNote) noteArray[position];
                        File image = new File(iNote.getFilePath());
                        Intent i = new Intent();
                        i.setAction(android.content.Intent.ACTION_VIEW);
                        i.setDataAndType(Uri.fromFile(image), "image/*");
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
                    File mediaFile = new File(m.getFilePath());
                    m.delete(noteId, WhatHappenedTodaySubjectActivity.this);
                    mediaFile.delete();
                }

                refreshList();
                return false;
            }
        });
        refreshList();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        refreshList();
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
        if (mStartRecording == true)
        {
            sound = new MediaCapture(this);
            //sound.onRecord(mStartRecording);
            soundFile = sound.captureSound();
            mStartRecording = !mStartRecording;
            newAudioButton.setText("Stop recording.");
            newAudioButton.setBackgroundColor(Color.RED);
            newImageButton.setVisibility(View.GONE);
            newTextButton.setVisibility(View.GONE);
            audioButtonWidth = newAudioButton.getLayoutParams().width;
            ViewGroup.LayoutParams paramsNew = newAudioButton.getLayoutParams();
            paramsNew.width = 1000;
            newAudioButton.setLayoutParams(paramsNew);
        } else
        {
            //sound.onRecord(mStartRecording);
            sound.stopCaptureSound();
            MediaNote sNote = new MediaNote("Audio", soundFile, subjectId, "Audio");
            sNote.persist(this);
            refreshList();
            mStartRecording = true;

            ViewGroup.LayoutParams paramsNew = newAudioButton.getLayoutParams();
            paramsNew.width = audioButtonWidth;
            newAudioButton.setText("New Audio Note");
            newAudioButton.setBackgroundColor(Color.LTGRAY);
            newImageButton.setVisibility(View.VISIBLE);
            newTextButton.setVisibility(View.VISIBLE);
            newAudioButton.setLayoutParams(paramsNew);

        }
    }

    public void newImageNote(View view)
    {
        System.out.println("Image Note CLicked");
        MediaCapture image = new MediaCapture(this);
        String imageFile = image.captureImage();
        MediaNote iNote = new MediaNote("Image", imageFile, subjectId, "Image");
        iNote.persist(this);
        refreshList();
        System.out.println(iNote);

    }

    public void newTextNote(View view)
    {
        Intent intent = new Intent(this, WhatHappendTodayNoteActivity.class);
        intent.putExtra("subjectId", subjectId);
        intent.putExtra("noteId", 0);
        intent.putExtra("currentText", "");
        startActivity(intent);
    }

    private void refreshList()
    {
        mediaArray = new MediaNote().retrieve(subjectId, this);
        textArray = new TextNote().retrieve(subjectId, this);
        noteArray = concat(mediaArray, textArray);
        Arrays.sort(noteArray);
        listAdapter = new CustomNoteListAdapter(this, noteArray);
        listView = (ListView) findViewById(R.id.NoteActivityListView);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                switch (noteArray[position].getType())
                {
                    case "Text":
                        TextNote tNote = (TextNote) noteArray[position];
                        System.out.println(tNote);
                        Intent intent = new Intent(WhatHappenedTodaySubjectActivity.this, WhatHappendTodayNoteActivity.class);
                        intent.putExtra("subjectId", subjectId);
                        intent.putExtra("noteId", tNote.get_id());
                        intent.putExtra("currentText", tNote.getTextNote());
                        startActivity(intent);
                        break;
                    case "Audio":
                        MediaNote mNote = (MediaNote) noteArray[position];
                        AndroidAudio audio = new AndroidAudio(WhatHappenedTodaySubjectActivity.this);
                        AndroidMusic music = audio.createMusic(mNote.getFilePath());
                        music.play();
                        System.out.println(mNote.getFilePath());
                        break;
                    case "Image":
                        MediaNote iNote = (MediaNote) noteArray[position];
                        File image = new File(iNote.getFilePath());
                        Intent i = new Intent();
                        i.setAction(android.content.Intent.ACTION_VIEW);
                        i.setDataAndType(Uri.fromFile(image), "image/*");
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
                int noteId = noteArray[position].get_id();
                if (noteArray[position].getType().equals("Text"))
                {
                    TextNote t = (TextNote) noteArray[position];
                    t.delete(noteId, WhatHappenedTodaySubjectActivity.this);
                } else
                {
                    MediaNote m = (MediaNote) noteArray[position];
                    File mediaFile = new File(m.getFilePath());
                    m.delete(noteId, WhatHappenedTodaySubjectActivity.this);
                    mediaFile.delete();
                }

                refreshList();
                return false;
            }
        });
    }

    private Note[] concat(Note[] media, Note[] text)
    {
        int aLen = media.length;
        int bLen = text.length;
        Note[] result = new Note[aLen + bLen];
        System.arraycopy(media, 0, result, 0, aLen);
        System.arraycopy(text, 0, result, aLen, bLen);
        return result;
    }
}
