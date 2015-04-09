package com.ashlimeianwarren.saaf;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.ashlimeianwarren.saaf.Beans.WhatHappenedToday.MediaNote;
import com.ashlimeianwarren.saaf.Beans.WhatHappenedToday.Note;
import com.ashlimeianwarren.saaf.Beans.WhatHappenedToday.Subject;
import com.ashlimeianwarren.saaf.Beans.WhatHappenedToday.TextNote;
import com.ashlimeianwarren.saaf.Implementation.AndroidAudio;
import com.ashlimeianwarren.saaf.Implementation.AndroidMusic;
import com.ashlimeianwarren.saaf.Implementation.MediaCapture;

import java.io.File;
import java.util.Arrays;

/**
 * Class for controlling and simulating being inside a subject folder allowing users to create, view,
 * edit and delete different types of notes such as TextNotes, AudioNotes and ImageNotes.
 */

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
    private AlertDialog.Builder alertDialogBuilder;
    int clickedPosition;
    private int latestNoteId;

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
        setContentView(R.layout.activity_what_happened_today_subject);
        //Retrieving the subject id passed with the intent
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            subjectId = extras.getInt("subjectId");
        }

        newAudioButton = (Button) findViewById(R.id.wht_subject_audio_button);
        newImageButton = (Button) findViewById(R.id.wht_subject_image_button);
        newTextButton = (Button) findViewById(R.id.wht_subject_text_button);
        noteArray = new MediaNote().retrieve(subjectId, this);
        listAdapter = new CustomNoteListAdapter(this, noteArray);
        listView = (ListView) findViewById(R.id.wht_subject_listview);
        listView.setAdapter(listAdapter);


        /**
         * Register a callback to be invoked when an item in this AdapterView has
         * been clicked.
         *
         * @param listener The callback that will be invoked.
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                //TODO OPEN THE FILE

                switch (noteArray[position].getType())
                {
                    case "Text":
                        TextNote tNote = (TextNote) noteArray[position];
                        System.out.println(tNote);
                        Intent intent = new Intent(WhatHappenedTodaySubjectActivity.this, WhatHappendTodayNoteActivity.class);
                        intent.putExtra("subjectId", subjectId);
                        intent.putExtra("noteId", tNote.get_id());
                        intent.putExtra("currentText", tNote.getTextNote());
                        intent.putExtra("noteName", tNote.getTextName());
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


        /**
         * Register a callback to be invoked when an item in this AdapterView has
         * been clicked and held
         *
         * @param listener The callback that will run
         */
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                System.out.println("Entered Long Click On Create Section");
                clickedPosition = position;

                AlertDialog.Builder alert = new AlertDialog.Builder(WhatHappenedTodaySubjectActivity.this);
                alert.setTitle("Confirm Deletion");
                alert.setMessage("Are you sure you want to delete this note?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                int noteId = noteArray[clickedPosition].get_id();

                                if (noteArray[clickedPosition].getType().equals("Text"))
                                {
                                    TextNote t = (TextNote) noteArray[clickedPosition];
                                    t.delete(noteId, WhatHappenedTodaySubjectActivity.this);
                                } else
                                {
                                    MediaNote m = (MediaNote) noteArray[clickedPosition];
                                    File mediaFile = new File(m.getFilePath());
                                    m.delete(noteId, WhatHappenedTodaySubjectActivity.this);
                                    mediaFile.delete();
                                }

                                refreshList();
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
        // refreshList();
    }

    /**
     * Called when the App is resumed and allows users to interact with the app again.
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        refreshList();
    }

    /**
     * Method used for controlling our custom list adapters
     *
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
     * Method for creating a new Audio Note and persisting it to the database.
     *
     * @param view The view that has been clicked
     */
    public void newAudioNote(View view)
    {
        if (mStartRecording == true)
        {
            sound = new MediaCapture(this);
            //sound.onRecord(mStartRecording);
            soundFile = sound.captureSound();
            mStartRecording = !mStartRecording;
            newAudioButton.setText("STOP RECORDING");
            newAudioButton.setBackgroundResource(R.drawable.button_style_recording);
            newAudioButton.setTextColor(getResources().getColorStateList(R.color.button_text_colour));
            newImageButton.setVisibility(View.GONE);
            newTextButton.setVisibility(View.GONE);
            audioButtonWidth = newAudioButton.getLayoutParams().width;
            ViewGroup.LayoutParams paramsNew = newAudioButton.getLayoutParams();
            paramsNew.width = 1000;
            newAudioButton.setLayoutParams(paramsNew);
        }
        else
        {
            //sound.onRecord(mStartRecording);
            sound.stopCaptureSound();
            MediaNote sNote = new MediaNote("Audio", soundFile, subjectId, "Audio");
            sNote.persist(this);
            refreshList();
            mStartRecording = true;

            ViewGroup.LayoutParams paramsNew = newAudioButton.getLayoutParams();
            paramsNew.width = audioButtonWidth;
            newAudioButton.setText("RECORD\nAUDIO");
            newAudioButton.setBackgroundResource(R.drawable.button_style);
            newAudioButton.setTextColor(getResources().getColorStateList(R.color.button_text_colour));
            newImageButton.setVisibility(View.VISIBLE);
            newTextButton.setVisibility(View.VISIBLE);
            newAudioButton.setLayoutParams(paramsNew);

        }
    }

    /**
     * Method for creating a new Image Note and persisting it to the database.
     *
     * @param view The view that has been clicked
     */
    public void newImageNote(View view)
    {
        System.out.println("Image Note CLicked");
        MediaCapture image = new MediaCapture(this);
        String imageFile = image.captureImage();
        MediaNote iNote = new MediaNote("Image", imageFile, subjectId, "Image");
        iNote.persist(this);
        latestNoteId = iNote.get_id();
        System.out.println(iNote);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //Checking to determine if the file was actually taken
        if (resultCode == 0)
        {
            MediaNote latestImageNote = new MediaNote();
            latestImageNote.delete(latestNoteId,this);
        }
        refreshList();
        }

    /**
     * Method for creating a new Text Note and persisting it to the database.
     *
     * @param view The view that has been clicked
     */
    public void newTextNote(View view)
    {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.wht_subject_dialog, null);
        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        alertDialogBuilder
                .setCancelable(false)
                .setTitle("Enter Note Ttile:")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                Intent intent = new Intent(WhatHappenedTodaySubjectActivity.this, WhatHappendTodayNoteActivity.class);
                                //TODO get the name for the text here....

                                intent.putExtra("subjectId", subjectId);
                                intent.putExtra("noteId", 0);
                                String enteredTitle = userInput.getText().toString();
                                if (!enteredTitle.isEmpty())
                                {
                                    intent.putExtra("noteName", userInput.getText().toString());
                                } else
                                {
                                    intent.putExtra("noteName", "Text Note");
                                }
                                intent.putExtra("currentText", "");
                                startActivity(intent);
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

    /**
     * A method which automatically updates a list displayed to a user so that new additions or
     * deletions are immediately shown to the user.
     */
    private void refreshList()
    {
        mediaArray = new MediaNote().retrieve(subjectId, this);
        textArray = new TextNote().retrieve(subjectId, this);
        noteArray = concat(mediaArray, textArray);
        Arrays.sort(noteArray);
        listAdapter = new CustomNoteListAdapter(this, noteArray);
        listView = (ListView) findViewById(R.id.wht_subject_listview);
        listView.setAdapter(listAdapter);

    }

    /**
     * Method for joining two note lists together.
     *
     * @param media Media Note List
     * @param text  Text Note List
     * @return      List of combined notes
     */
    private Note[] concat (Note[]media, Note[]text)
    {
        int aLen = media.length;
        int bLen = text.length;
        Note[] result = new Note[aLen + bLen];
        System.arraycopy(media, 0, result, 0, aLen);
        System.arraycopy(text, 0, result, aLen, bLen);
        return result;
    }
}
