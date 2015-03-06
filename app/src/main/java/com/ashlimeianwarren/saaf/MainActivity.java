package com.ashlimeianwarren.saaf;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ashlimeianwarren.saaf.Beans.WhatHappenedToday.Subject;
import com.ashlimeianwarren.saaf.Implementation.DbCon;


public class MainActivity extends ActionBarActivity
{

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
