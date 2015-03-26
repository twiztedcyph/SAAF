package com.ashlimeianwarren.saaf;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ashlimeianwarren.saaf.Beans.WhatsThis.Data;
import com.ashlimeianwarren.saaf.Beans.WhatsThis.DataImage;
import com.ashlimeianwarren.saaf.Beans.WhatsThis.Tag;
import com.ashlimeianwarren.saaf.Implementation.DbCon;

import java.io.UnsupportedEncodingException;


public class MainActivity extends ActionBarActivity
{
    private String lastMessage = "No NFC Message";
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] intentFilters;
    private String[][] nfcTechLists;
    private SharedPreferences prefs;
    private final String TAG = "com.ashlimeianwarren.saaf";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DbCon dbCon = new DbCon(this, null);
        SQLiteDatabase db = dbCon.getWritableDatabase();
        db.close();
        db = null;
        dbCon = null;

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null)
        {
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        else
        {

            if (nfcAdapter.isEnabled())
            {
                System.out.println("NFC CHECK: Enabled");
            } else
            {
                System.out.println("NFC CHECK: Disabled");
            }
        }

        // create an intent with tag data and deliver to this activity
        pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // set an intent filter for all MIME data
        IntentFilter ndefIntent = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefIntent.addDataType("*/*");
            intentFilters = new IntentFilter[]{ndefIntent};
        } catch (Exception e) {
            Log.e("TagDispatch", e.toString());
        }

        nfcTechLists = new String[][]{new String[]{NfcF.class.getName()}};
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        Log.i("Intent", "Entered Intent");

        //String gotString = this.checkForNdef(intent);
        this.setIntent(intent);
    }

    @Override
    protected void onResume()
    {
        Log.i("Resume", "Entered Resume");
        super.onResume();

        prefs = getSharedPreferences(TAG, MODE_PRIVATE);
        if (prefs.getBoolean("firstrun", true))
        {
            //TODO add whats this content.
            Tag tag = new Tag("CMP");
            tag.persist(this);

            String dataName = "Graphics 2";
            String dataDesc = "This module introduces the fundamentals of 3D geometric " +
                    "transformations and viewing using OpenGL. It teaches the theory and " +
                    "implementation of fundamental visibility determination algorithms " +
                    "and techniques for lighting, shading and anti-aliasing. Issues " +
                    "involved with modern high performance graphics processor are also" +
                    " considered. It also studies 3D curves and fundamental geometric data" +
                    " structures.";

            Data data = new Data(dataName, dataDesc, tag.get_id());
            data.persist(this);

            DataImage dataImage = new DataImage("Have a nice day", "have_a_nice_day.png", data.get_id());
            dataImage.persist(this);

            dataImage = new DataImage("Have a bad day", "have_a_nice_day.png", data.get_id());
            dataImage.persist(this);

            dataName = "Software Engineering 2";
            dataDesc = "Industrial software development is seldom started from scratch, " +
                    "companies generally have large systems of legacy software that need " +
                    "to be maintained, improved and extended. This module focuses on " +
                    "advanced software engineering topics, such as reverse engineering to" +
                    " understand legacy software, refactoring and design patterns to improve" +
                    " the design of software systems and developing new software products" +
                    " using third-party software components. Assessment will be done by a " +
                    "group project which consists of a design and analysis task, and the " +
                    "group implementation task of a software project. Confidence in Java " +
                    "programming language skills as well as software engineering practice " +
                    "(phased development with agile methods, Unified Modeling Language, " +
                    "test-driven development) are pre-requisites. Software Engineering I " +
                    "(2M02) is required for this module.";

            data = new Data(dataName, dataDesc, tag.get_id());
            data.persist(this);

            dataImage = new DataImage("Have a nice day", "have_a_nice_day.png", data.get_id());
            dataImage.persist(this);

            dataImage = new DataImage("Have a bad day", "have_a_nice_day.png", data.get_id());
            dataImage.persist(this);

            dataName = "Programming 2";
            dataDesc = "This is a compulsory year long module for all computing students and" +
                    " is a continuation of CMP-4008Y. It contains greater breadth and depth " +
                    "and provides students with the range of skills needed for many of their " +
                    "subsequent modules. We recap Java and deepen your understanding of the " +
                    "language by teaching topics such as nested classes, enumeration, generics, " +
                    "reflection, collections and threaded programming. We then introduce C in " +
                    "order to improve your low level understanding of how programming works, " +
                    "before moving on to C++ in semester 2. We conclude by introducing C# to " +
                    "highlight the similarities and differences between languages.";

            data = new Data(dataName, dataDesc, tag.get_id());
            data.persist(this);

            dataImage = new DataImage("Have a nice day", "have_a_nice_day.png", data.get_id());
            dataImage.persist(this);

            dataImage = new DataImage("Have a bad day", "have_a_nice_day.png", data.get_id());
            dataImage.persist(this);
        }

        if (nfcAdapter == null)
        {
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, nfcTechLists);

        String gotString = this.checkForNdef(this.getIntent());
        Log.i("Resume", "Got String "+ gotString);

        Intent currentIntent = getIntent();

        currentIntent.removeExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        if(!gotString.equals("Ready To Scan"))
        {
            Intent nextIntent = new Intent(this, WhatsThisPlacesActivity.class);

            nextIntent.putExtra("tagID", gotString);

            startActivity(nextIntent);
        }
    }

    @Override
    protected void onPause()
    {

        super.onPause();

        if (nfcAdapter != null)
        {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private String checkForNdef(Intent intent)
    {
        String payloadString = "Ready To Scan";
        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction()) || NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction()))
        {
            System.out.println("ACTION_NDEF_DISCOVERED");
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if(rawMsgs != null)
            {

                NdefMessage[] msgs = new NdefMessage[rawMsgs.length];

                for(int i = 0; i < rawMsgs.length; i++)
                {
                    msgs[i] = (NdefMessage)rawMsgs[i];
                    NdefRecord[] record = msgs[i].getRecords();
                    for(int j = 0; j < record.length; j++)
                    {
                        byte [] payload = record[i].getPayload();

                        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

                        // Get the Language Code
                        int languageCodeLength = payload[0] & 0063;

                        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
                        // e.g. "en"

                        // Get the Text
                        try
                        {
                            payloadString = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
                        }
                        catch (UnsupportedEncodingException e)
                        {
                            Log.d("Encode Error", "Error: " + e);
                        }

                        Log.d("Payload String", payloadString);
                        Toast.makeText(this, payloadString, Toast.LENGTH_SHORT).show();
                        lastMessage = payloadString;

                        if (!lastMessage.equals("Ready To Scan"))
                        {
                            Intent nextIntent = new Intent(this, WhatsThisDataDisplayActivity.class);
                            nextIntent.putExtra("dataName", payloadString);
                            startActivity(nextIntent);
                        }
                    }
                }
            }
            else
            {
                Log.d("NFC Error","NFC Message are Null");
            }
        }



        return payloadString;
    }

    public void wheresMyCarClicked(View view)
    {
        Intent intent = new Intent(this, WheresMyCarMainActivity.class);
        startActivity(intent);
    }

    public void whatHappenedTodayClicked(View view)
    {
        Intent intent = new Intent(this, WhatHappenedTodayMainActivity.class);
        startActivity(intent);
    }

    public void whatsThisClicked(View view)
    {
        Intent intent = new Intent(this, WhatsThisMainActivity.class);
        startActivity(intent);
    }
}
