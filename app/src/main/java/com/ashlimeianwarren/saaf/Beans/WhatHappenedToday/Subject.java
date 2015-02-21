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

        this._id = (int)db.insert(DbCon.TABLE_WHT_SUBJECT, null, contentValues);

        db.close();
    }

    public Subject[] retrieve(Context context)
    {
        ArrayList<Subject> subjectList = new ArrayList<>();

        String query = "SELECT * FROM " + DbCon.TABLE_WHT_SUBJECT + " WHERE 1;";

        dbCon = new DbCon(context, null);
        SQLiteDatabase db = dbCon.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast() && !cursor.getString(cursor.getColumnIndex("title")).isEmpty())
        {
            int retId = cursor.getInt(cursor.getColumnIndex("_id"));
            String retTitle = cursor.getString(cursor.getColumnIndex("title"));
            subjectList.add(new Subject(retId, retTitle));
        }
        db.close();
        cursor.close();

        Subject[] result = new Subject[subjectList.size()];
        subjectList.toArray(result);

        return result;
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
                "\n_id = " + _id +
                "\ntitle = " + title;
    }
}
