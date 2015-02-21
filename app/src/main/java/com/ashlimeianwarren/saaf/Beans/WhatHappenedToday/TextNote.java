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
public class TextNote
{
    private int _id;
    private String textNote;
    private int subjectId;
    private DbCon dbCon;

    public TextNote()
    {
    }

    public TextNote(String textNote, int subjectId)
    {
        this.textNote = textNote;
        this.subjectId = subjectId;
    }

    private TextNote(int _id, String textNote, int subjectId)
    {
        this._id = _id;
        this.textNote = textNote;
        this.subjectId = subjectId;
    }

    public void persist(Context context)
    {
        dbCon = new DbCon(context, null);
        SQLiteDatabase db = dbCon.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbCon.COLUMN_WHT_TEXT, textNote);
        contentValues.put(DbCon.COLUMN_WHT_SUBJECTID, subjectId);
        this._id = (int)db.insert(DbCon.TABLE_WHT_MEDIANOTE, null, contentValues);

        db.close();
    }

    public TextNote[] retrieve(int subjectId, Context context)
    {
        ArrayList<TextNote> textNoteList = new ArrayList<>();

        String query = "SELECT * FROM " + DbCon.TABLE_WHT_TEXTNOTE +
                " WHERE " + DbCon.COLUMN_WHT_SUBJECTID + " = " + subjectId + ";";

        dbCon = new DbCon(context, null);
        SQLiteDatabase db = dbCon.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast() &&
                !cursor.getString(cursor.getColumnIndex(DbCon.COLUMN_WHT_TEXT)).isEmpty())
        {
            int retId = cursor.getInt(cursor.getColumnIndex(DbCon.COLUMN_WHT_ID));
            String retTextNote = cursor.getString(cursor.getColumnIndex(DbCon.COLUMN_WHT_TEXT));
            int retSubjectId = cursor.getInt(cursor.getColumnIndex(DbCon.COLUMN_WHT_SUBJECTID));
        }
        db.close();
        cursor.close();

        TextNote[] result = new TextNote[textNoteList.size()];
        textNoteList.toArray(result);

        return result;
    }

    public String getTextNote()
    {
        return textNote;
    }

    public void setTextNote(String textNote)
    {
        this.textNote = textNote;
    }

    public int getSubjectId()
    {
        return subjectId;
    }

    public void setSubjectId(int subjectId)
    {
        this.subjectId = subjectId;
    }

    public int get_id()
    {
        return _id;
    }

    @Override
    public String toString()
    {
        return "TextNote:" +
                "\n_id = " + _id +
                "\ntextNote = " + textNote +
                "\nsubjectId = " + subjectId;
    }
}
