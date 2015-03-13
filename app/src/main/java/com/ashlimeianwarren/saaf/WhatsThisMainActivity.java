package com.ashlimeianwarren.saaf;

import android.nfc.NfcEvent;
import android.support.v7.app.ActionBarActivity;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcF;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class WhatsThisMainActivity extends ActionBarActivity
{

    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String NFC_Tag = "NFC Class";

    private TextView textView;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] intentFilters;
    private String[][] nfcTechLists;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d("Created", "NFC Activity Created");
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_whats_this_main);
        Button btag = (Button) findViewById(R.id.button_tag);
        btag.setOnClickListener(new View.OnClickListener()
        {
            // @Override
            public void onClick(View arg0)
            {
                startActivity(new Intent(WhatsThisMainActivity.this, WhatsThisMainActivity.class));
            }
        });


        textView = (TextView) findViewById(R.id.whatsthis_maintext);

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
                textView.setText("NFC ready to scan");
            } else
            {
                textView.setText("NFC is disabled.");
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
    protected void onResume()
    {
        super.onResume();

        if (nfcAdapter.isEnabled()) {
            textView.setText("NFC is Ready to Scan");
        } else {
            textView.setText("NFC is disabled.");
        }

        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction()))
        {
            Intent intent = getIntent();
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if(rawMsgs != null)
            {
                NdefMessage[] msgs = new NdefMessage[rawMsgs.length];

                for(int i = 0; i < rawMsgs.length; i++)
                {
                    msgs[i] = (NdefMessage)rawMsgs[i];
                    Log.i("NFC MEssage My Way", msgs[i].toString());
                    NdefRecord[] record = msgs[i].getRecords();
                    for(int j = 0; j < record.length; j++)
                    {
                        Log.d("Byte Array", "Position "+j+" Payload: "+record[i].getPayload());
                    }
                    textView.setText(msgs[i].toString());
                }
            }
            else
            {
                Log.d("NFC Error","NFC Message are Null");
            }
        }
        else if(NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction()))
        {
            Intent intent = getIntent();
            Log.d("Tech Discovered", "Action Tech");
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Log.d("Tag Details", tag.toString());

            Parcelable[] rawMsgs2 = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if(rawMsgs2 != null)
            {
                NdefMessage[] msgs = new NdefMessage[rawMsgs2.length];

                for(int i = 0; i < rawMsgs2.length; i++)
                {
                    msgs[i] = (NdefMessage)rawMsgs2[i];
                    Log.i("NFC MEssage My Way", msgs[i].toString());
                    NdefRecord[] record = msgs[i].getRecords();
                    for(int j = 0; j < record.length; j++)
                    {
                        Log.d("Byte Array", "Position "+j+" Payload: "+record[i].getPayload());
                    }
                    textView.setText(msgs[i].toString());
                }
            }
            else
            {
                Log.d("NFC Error","NFC Message are Null");
            }

        }
        else if(getIntent().getAction() != null)
        {
            Log.d("Intent is", getIntent().toString());
            Log.d("Intent is", "Not Null");
        }
        else
        {
            Log.d("Null action", "Intent Action Is null");
        }

        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, nfcTechLists);
        }
    }

    @Override
    protected void onPause() {

        super.onPause();

        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_whats_this_main, menu);
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
