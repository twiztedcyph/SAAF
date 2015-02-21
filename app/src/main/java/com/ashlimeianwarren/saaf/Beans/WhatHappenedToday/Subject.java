package com.ashlimeianwarren.saaf.Beans.WhatHappenedToday;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ashlimeianwarren.saaf.Implementation.DbCon;

import java.util.ArrayList;

/**
 * Created by Twiz on 20/02/2015.
 */
public class Subject
{
    private int _id;
    private String title;
    private DbCon dbCon;

    public Subject(){}

    public Subject(String title)
    {
        this.title = title;
    }

    private Subject(int _id, String title)
    {
        this._id = _id;
        this.title = title;
    }

    public void persist(Context context)
    {
        dbCon = new DbCon(context, null);
        SQLiteDatabase db = dbCon.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DbCon.COLUMN_WHT_TITLE, this.title);

        this._id = (int)db.insert("wht_subject", null, contentValues);

        db.close();
    }

    public Subject[] retrieve(String title, Context context)
    {
        ArrayList<Subject> subjectList = new ArrayList<>();

        String query = "SELECT * FROM wht_subject WHERE title = '" + title + "';";

        dbCon = new DbCon(context, null);
        SQLiteDatabase db = dbCon.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast() && !cursor.getString(cursor.getColumnIndex("title")).isEmpty())
        {
            int retrievedId = cursor.getInt(cursor.getColumnIndex("_id"));
            String retrievedTitle = cursor.getString(cursor.getColumnIndex("title"));
            subjectList.add(new Subject(retrievedId, retrievedTitle));
        }
        db.close();
        cursor.close();

        Subject[] results = new Subject[subjectList.size()];
        subjectList.toArray(results);

        return results;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public long get_id()
    {
        return _id;
    }

    @Override
    public String toString()
    {
        return "Subject:" +
                "\n_id=" + _id +
                "\ntitle='" + title;
    }
}
