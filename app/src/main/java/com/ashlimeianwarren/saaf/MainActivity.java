package com.ashlimeianwarren.saaf;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.ashlimeianwarren.saaf.Implementation.DbCon;

import java.io.UnsupportedEncodingException;


public class MainActivity extends ActionBarActivity
{
    private String lastMessage = "No NFC Message";
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] intentFilters;
    private String[][] nfcTechLists;

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


        if (nfcAdapter == null)
        {
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, nfcTechLists);

        String gotString = this.checkForNdef(this.getIntent());
        Log.i("Resume", "Got String "+gotString);

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
