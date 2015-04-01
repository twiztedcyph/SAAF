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
            }
            else
            {
                System.out.println("NFC CHECK: Disabled");
            }
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

            prefs.edit().putBoolean("firstrun", false).apply();
            System.out.println("LOADING DATA");
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

            DataImage dataImage = new DataImage("3d teapot mesh", "graphics_one.png", data.get_id());
            dataImage.persist(this);

            dataImage = new DataImage("Basic 3d modeling", "graphics_two.png", data.get_id());
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

            dataImage = new DataImage("Programming code", "software_one.png", data.get_id());
            dataImage.persist(this);

            dataImage = new DataImage("Software engineering", "software_two.png", data.get_id());
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

            dataImage = new DataImage("We have no idea", "prog_one.png", data.get_id());
            dataImage.persist(this);

            dataImage = new DataImage("Java code", "prog_two.png", data.get_id());
            dataImage.persist(this);

            dataName = "Dr Joost Noppen";
            dataDesc = "Joost Noppen is lecturer in Software Engineering at the University of East " +
                    "Anglia in Norwich (UK). Joost holds an M.Sc. degree and a Ph. D. degree in " +
                    "Computer Science from the University of Twente in the Netherlands with a " +
                    "special focus on computational intelligence in Software Engineering. Joost " +
                    "has held positions in leading Software Engineering research groups across " +
                    "Europe, such as the École des Mines and the University of Lancaster, where " +
                    "he has worked in international research projects. In 2008 Joost was awarded " +
                    "a prestigious Marie Curie Intra-European fellowship from the EU for his " +
                    "work on Software Product Line development.\n\nJoost has over 14 years " +
                    "experience in Software Engineering both from an academic and industrial " +
                    "perspective. He has founded a successful web development company as a " +
                    "student and he has developed software in collaboration with multi-national " +
                    "companies. In his spare time Joost is exploring the application of Software " +
                    "Engineering principles to mobile games development practice.";

            data = new Data(dataName, dataDesc, tag.get_id());
            data.persist(this);

            dataImage = new DataImage("Dr Toast Noppen", "joost_one.png", data.get_id());
            dataImage.persist(this);

            dataImage = new DataImage("Some Joost", "joost_two.png", data.get_id());
            dataImage.persist(this);

            tag = new Tag("BIO");
            tag.persist(this);

            dataName = "Skills For Biologists";
            dataDesc = "This year-long module combines small-group seminars and workshops with " +
                    "supporting lecture-based sessions. Material will support various first-year " +
                    "modules such as BIO-4001A, BIO-4002B, BIO-4003A and BIO-4004B. Students " +
                    "will learn how to access scientific material and to use it critically in " +
                    "essays, oral presentations and posters. This module will explore how such " +
                    "scientific material is disseminated to scientists and to the general public. " +
                    "A combination of lectures and workshops will be used to introduce a range " +
                    "of topics in maths and statistics that are absolutely essential for a " +
                    "contemporary undergraduate studying the biological sciences. THIS MODULE IS " +
                    "ONLY AVAILABLE TO YEAR 1 STUDENTS. THIS MODULE IS NOT AVAILABLE TO " +
                    "VISITING/EXCHANGE STUDENTS.";

            data = new Data(dataName, dataDesc, tag.get_id());
            data.persist(this);

            dataImage = new DataImage("Biologist doing biology things", "sfb_one.png", data.get_id());
            dataImage.persist(this);

            dataImage = new DataImage("Cells doing cell things", "sfb_two.png", data.get_id());
            dataImage.persist(this);

            dataName = "Fundamentals Of Molecular Biology";
            dataDesc = "The module aims to provide an introduction to the basic aspects of " +
                    "biochemistry, molecular biology and genetics. The module explores the " +
                    "fundamental properties of macromolecules, DNA structure, synthesis and " +
                    "replication, as well as the structure and function of proteins. The genetic " +
                    "code, genes and their expression will be covered as well as the rapidly " +
                    "expanding area of molecular biology. The module also covers chromosome " +
                    "structure, mechanisms of heredity, medical genetics and cytogenetics.";

            data = new Data(dataName, dataDesc, tag.get_id());
            data.persist(this);

            dataImage = new DataImage("Molecules", "mollbio_one.png", data.get_id());
            dataImage.persist(this);

            dataImage = new DataImage("Synapsy looking things", "mollbio_two.png", data.get_id());
            dataImage.persist(this);

            dataName = "Fundamentals Of Cell Biology";
            dataDesc = "The module aims to provide an introduction to the basic aspects of " +
                    "biochemistry and cell biology. Basic biochemical processes will be " +
                    "explored, as well as catalysis and enzymology. There will be an " +
                    "introduction to the nature of the living cell, its membranes, and " +
                    "organelles, how cells communicate and also how they are visualised." +
                    "\n" +
                    "\n" +
                    "Cell biology (formerly cytology, from the Greek kytos, \"contain\") is a " +
                    "branch of biology that studies cells – their physiological properties, " +
                    "their structure, the organelles they contain, interactions with their " +
                    "environment, their life cycle, division, death and cell function. This is " +
                    "done both on a microscopic and molecular level. Cell biology research " +
                    "encompasses both the great diversity of single-celled organisms like " +
                    "bacteria and protozoa, as well as the many specialized cells in " +
                    "multicellular organisms such as humans, plants, and sponges.\n" +
                    "Knowing the components of cells and how cells work is fundamental to all " +
                    "biological sciences. Appreciating the similarities and differences between " +
                    "cell types is particularly important to the fields of cell and molecular " +
                    "biology as well as to biomedical fields such as cancer research and " +
                    "developmental biology. These fundamental similarities and differences " +
                    "provide a unifying theme, sometimes allowing the principles learned from " +
                    "studying one cell type to be extrapolated and generalized to other cell " +
                    "types. Therefore, research in cell biology is closely related to genetics, " +
                    "biochemistry, molecular biology, immunology, and developmental biology.";

            data = new Data(dataName, dataDesc, tag.get_id());
            data.persist(this);

            dataImage = new DataImage("Cell biology", "cellbio_one.png", data.get_id());
            dataImage.persist(this);

            dataImage = new DataImage("More cell biology", "cellbio_two.png", data.get_id());
            dataImage.persist(this);

            tag = new Tag("ENV");
            tag.persist(this);

            dataName = "Global Environmental Challenges";
            dataDesc = "What are the most pressing environmental challenges facing the world " +
                    "today? How do we understand these problems through cutting-edge " +
                    "environmental science research? What are the possibilities for building " +
                    "sustainable solutions to address them in policy and society? In this module " +
                    "you will tackle these questions by taking an interdisciplinary approach to " +
                    "consider challenges relating to climate change, biodiversity, water " +
                    "resources, natural hazards, and technological risks. In doing so you will " +
                    "gain an insight into environmental science research 'in action' and develop " +
                    "essential academic study skills needed to explore these issues. Please note " +
                    "that ENV students, BIO Ecology students, NAT SCI students and SCI " +
                    "Foundation Year students can request a space on this module. Please note " +
                    "that NAT SCI and SCI Foundation Year students wishing to select this " +
                    "module must obtain a signature from their advisor confirming they will " +
                    "meet the marking requirements (which will be to mark the independent " +
                    "essay component of the module assessments). The advisor must confirm " +
                    "agreement in writing to env_ug.hub@uea.ac.uk).";

            data = new Data(dataName, dataDesc, tag.get_id());
            data.persist(this);

            dataImage = new DataImage("Global warming", "gec_one.png", data.get_id());
            dataImage.persist(this);

            dataImage = new DataImage("Deforestation", "gec_two.png", data.get_id());
            dataImage.persist(this);

        }
        else
        {
            System.out.println("NOT FIRST RUN");
        }

        if (nfcAdapter == null)
        {
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, nfcTechLists);

        String gotString = this.checkForNdef(this.getIntent());
        Log.i("Resume", "Got String " + gotString);

        Intent currentIntent = getIntent();

        currentIntent.removeExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        if (!gotString.equals("Ready To Scan"))
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
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction()) || NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction()))
        {
            System.out.println("ACTION_NDEF_DISCOVERED");
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if (rawMsgs != null)
            {

                NdefMessage[] msgs = new NdefMessage[rawMsgs.length];

                for (int i = 0; i < rawMsgs.length; i++)
                {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                    NdefRecord[] record = msgs[i].getRecords();
                    for (int j = 0; j < record.length; j++)
                    {
                        byte[] payload = record[i].getPayload();

                        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

                        // Get the Language Code
                        int languageCodeLength = payload[0] & 0063;

                        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
                        // e.g. "en"

                        // Get the Text
                        try
                        {
                            payloadString = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
                        } catch (UnsupportedEncodingException e)
                        {
                            Log.d("Encode Error", "Error: " + e);
                        }

                        Log.d("Payload String", payloadString);
                        //Toast.makeText(this, payloadString, Toast.LENGTH_SHORT).show();
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
                Log.d("NFC Error", "NFC Message are Null");
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
