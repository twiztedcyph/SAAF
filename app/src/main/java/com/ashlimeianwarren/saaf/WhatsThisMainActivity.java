package com.ashlimeianwarren.saaf;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class WhatsThisMainActivity extends ActionBarActivity
{

    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String NFC_Tag = "NFC Class";
    private String lastMessage = "No NFC Message";

    private TextView textView;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] intentFilters;
    private String[][] nfcTechLists;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.i("Create", "Entered Create");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_whats_this_main);


        textView = (TextView) findViewById(R.id.whatsthis_maintext);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null)
        {
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // create an intent with tag data and deliver to this activity
        pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // set an intent filter for all MIME data
        IntentFilter ndefIntent = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try
        {
            ndefIntent.addDataType("*/*");
            intentFilters = new IntentFilter[]{ndefIntent};
        } catch (Exception e)
        {
            Log.e("TagDispatch", e.toString());
        }

        nfcTechLists = new String[][]{new String[]{NfcF.class.getName()}};
        textView.setText(lastMessage);

        //String gotString = this.checkForNdef(this.getIntent());
        //Log.i("Create ", "Got String "+gotString);
        //textView.setText(gotString);
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        Log.i("Intent", "Entered Intent");

        //String gotString = this.checkForNdef(intent);
        //this.setIntent(intent);
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
        textView.setText(lastMessage);

        //String gotString = this.checkForNdef(this.getIntent());
        //Log.i("Resume", "Got String "+gotString);
        //textView.setText(gotString);

        Intent currentIntent = getIntent();

        currentIntent.removeExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

//        if(!gotString.equals("Ready To Scan"))
//        {
//            Intent nextIntent = new Intent(this, WhatsThisPlacesActivity.class);
//
//            nextIntent.putExtra("tagID", gotString);
//
//            startActivity(nextIntent);
//        }
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

//    private String checkForNdef(Intent intent)
//    {
//        String payloadString = "Ready To Scan";
//        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction()))
//        {
//            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
//
//            if(rawMsgs != null)
//            {
//
//                NdefMessage[] msgs = new NdefMessage[rawMsgs.length];
//
//                for(int i = 0; i < rawMsgs.length; i++)
//                {
//                    msgs[i] = (NdefMessage)rawMsgs[i];
//                    NdefRecord[] record = msgs[i].getRecords();
//                    for(int j = 0; j < record.length; j++)
//                    {
//                        byte [] payload = record[i].getPayload();
//
//                        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
//
//                        // Get the Language Code
//                        int languageCodeLength = payload[0] & 0063;
//
//                        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
//                        // e.g. "en"
//
//                        // Get the Text
//                        try
//                        {
//                            payloadString = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
//                        }
//                        catch (UnsupportedEncodingException e)
//                        {
//                            Log.d("Encode Error", "Error: "+e);
//                        }
//
//                        Log.d("Payload String", payloadString);
//                        Toast.makeText(this,payloadString,Toast.LENGTH_SHORT).show();
//                        lastMessage = payloadString;
//                        textView.setText(lastMessage);
//                        return payloadString;
//                    }
//                }
//            }
//            else
//            {
//                Log.d("NFC Error","NFC Message are Null");
//            }
//        }
//        else if(NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction()))
//        {
//            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
//
//            if(rawMsgs != null)
//            {
//
//                NdefMessage[] msgs = new NdefMessage[rawMsgs.length];
//
//                for(int i = 0; i < rawMsgs.length; i++)
//                {
//                    msgs[i] = (NdefMessage)rawMsgs[i];
//                    NdefRecord[] record = msgs[i].getRecords();
//                    for(int j = 0; j < record.length; j++)
//                    {
//                        byte [] payload = record[i].getPayload();
//
//                        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
//
//                        // Get the Language Code
//                        int languageCodeLength = payload[0] & 0063;
//
//                        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
//                        // e.g. "en"
//
//                        // Get the Text
//                        try
//                        {
//                            payloadString = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
//                        }
//                        catch (UnsupportedEncodingException e)
//                        {
//                            Log.d("Encode Error", "Error: "+e);
//                        }
//
//                        Log.d("Payload String", payloadString);
//                        Toast.makeText(this,payloadString,Toast.LENGTH_SHORT).show();
//                        lastMessage = payloadString;
//                        textView.setText(lastMessage);
//                    }
//                }
//            }
//            else
//            {
//                Log.d("NFC Error","NFC Message are Null");
//            }
//        }
//
//        return payloadString;
//    }

    public void peopleButtonClicked(View view)
    {
        System.out.println("people button clicked.");
    }

    public void placesButtonClicked(View view)
    {
        System.out.println("places button clicked.");
    }

    public void addContentClicked(View view)
    {
        Intent intent = new Intent(this, WhatsThisAddContentActivity.class);
        startActivity(intent);
    }
}
