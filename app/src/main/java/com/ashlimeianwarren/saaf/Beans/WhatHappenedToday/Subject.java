package com.ashlimeianwarren.saaf.Beans.WhatHappenedToday;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ashlimeianwarren.saaf.Implementation.DbCon;

import java.util.ArrayList;

/**
 * Subject class.
 * <p/>
 * Defines a subject object for use in the what happened todat app.
 */
public class Subject
{
    private int _id;
    private String title;
    private DbCon dbCon;

    /**
     * Default empty constructor.
     */
    public Subject()
    {
    }

    /**
     * Constructor for a Subject object.
     *
     * @param title The subject title.
     */
    public Subject(String title)
    {
        this.title = title;
    }

    private Subject(int _id, String title)
    {
        /*
        Private constructor with id included.
        Used to initialise from database.
         */
        this._id = _id;
        this.title = title;
    }

    /**
     * Persist or save this Subject to the database.
     *
     * @param context The context from which this method was called.
     */
    public void persist(Context context)
    {
        dbCon = new DbCon(context, null);
        SQLiteDatabase db = dbCon.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DbCon.COLUMN_WHT_TITLE, this.title);

        this._id = (int) db.insert(DbCon.TABLE_WHT_SUBJECT, null, contentValues);

        db.close();
    }

    /**
     * Delete this subject from to the database.
     *
     * @param context   The context from which this method was called.
     * @param subjectId The id of the subject to be deleted
     */
    public void delete(int subjectId, Context context)
    {
        dbCon = new DbCon(context, null);
        SQLiteDatabase db = dbCon.getWritableDatabase();

        String query = "DELETE FROM " + DbCon.TABLE_WHT_SUBJECT +
                " WHERE " + DbCon.COLUMN_WHT_ID + " = " + subjectId + ";";

        db.execSQL(query);
        db.close();
    }

    /**
     * Retrieve an array of all Subjects from the database.
     *
     * @param context The context from which this method was called.
     * @return An array of all Subjects contained in the database.
     */
    public Subject[] retrieve(Context context)
    {
        ArrayList<Subject> subjectList = new ArrayList<>();

        String query = "SELECT * FROM " + DbCon.TABLE_WHT_SUBJECT + " WHERE 1;";

        dbCon = new DbCon(context, null);
        SQLiteDatabase db = dbCon.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        int count = 0;
        while (!cursor.isAfterLast())
        {
            int retId = cursor.getInt(cursor.getColumnIndex("_id"));
            String retTitle = cursor.getString(cursor.getColumnIndex("title"));
            subjectList.add(new Subject(retId, retTitle));
            cursor.moveToNext();
            System.out.println(subjectList.size());
        }
        db.close();
        cursor.close();

        Subject[] result = new Subject[subjectList.size()];
        subjectList.toArray(result);

        return result;
    }

    /**
     * Get the title.
     *
     * @return The subject title.
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Set the title.
     *
     * @param title The subject title.
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * Get the database id for this subject.
     *
     * @return The database if for this subject.
     */
    public int get_id()
    {
        return _id;
    }

    /**
     * Get a string representation of this object.
     *
     * @return A string representation of this object.
     */
    @Override
    public String toString()
    {
        return "Subject:" +
                "\n_id = " + _id +
                "\ntitle = " + title;
    }
}
