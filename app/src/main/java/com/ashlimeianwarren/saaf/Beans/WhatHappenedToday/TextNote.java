package com.ashlimeianwarren.saaf.Beans.WhatHappenedToday;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ashlimeianwarren.saaf.Implementation.DbCon;

import java.util.ArrayList;

/**
 * Text note class.
 * <p/>
 * Defines a TextNote object that contains textual information on a subject.
 */
public class TextNote
{
    private int _id;
    private String textNote;
    private int subjectId;
    private DbCon dbCon;

    /**
     * Default empty constructor.
     */
    public TextNote()
    {
    }

    /**
     * Constructor for a TextNote object.
     *
     * @param textNote  The text note.
     * @param subjectId The subject id for this TextNote.
     */
    public TextNote(String textNote, int subjectId)
    {
        this.textNote = textNote;
        this.subjectId = subjectId;
    }

    private TextNote(int _id, String textNote, int subjectId)
    {
        /*
        Private constructor with id included.
        Used to initialise from database.
         */
        this._id = _id;
        this.textNote = textNote;
        this.subjectId = subjectId;
    }

    /**
     * Persist or save this TextNote to the database.
     *
     * @param context The context from which this method was called.
     */
    public void persist(Context context)
    {
        dbCon = new DbCon(context, null);
        SQLiteDatabase db = dbCon.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbCon.COLUMN_WHT_TEXT, textNote);
        contentValues.put(DbCon.COLUMN_WHT_SUBJECTID, subjectId);
        this._id = (int) db.insert(DbCon.TABLE_WHT_MEDIANOTE, null, contentValues);

        db.close();
    }

    /**
     * Retrieve all TextNotes for a specific subject from the database.
     *
     * @param subjectId The subject id.
     * @param context   The context from which this method was called.
     * @return An array of TextNotes for the subject.
     */
    public TextNote[] retrieve(int subjectId, Context context)
    {
        ArrayList<TextNote> textNoteList = new ArrayList<>();

        String query = "SELECT * FROM " + DbCon.TABLE_WHT_TEXTNOTE +
                " WHERE " + DbCon.COLUMN_WHT_SUBJECTID + " = " + subjectId + ";";

        dbCon = new DbCon(context, null);
        SQLiteDatabase db = dbCon.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast() &&
                !cursor.getString(cursor.getColumnIndex(DbCon.COLUMN_WHT_TEXT)).isEmpty())
        {
            int retId = cursor.getInt(cursor.getColumnIndex(DbCon.COLUMN_WHT_ID));
            String retTextNote = cursor.getString(cursor.getColumnIndex(DbCon.COLUMN_WHT_TEXT));
            int retSubjectId = cursor.getInt(cursor.getColumnIndex(DbCon.COLUMN_WHT_SUBJECTID));

            textNoteList.add(new TextNote(retId, retTextNote, retSubjectId));
        }
        db.close();
        cursor.close();

        TextNote[] result = new TextNote[textNoteList.size()];
        textNoteList.toArray(result);

        return result;
    }

    /**
     * Get the text for this note.
     *
     * @return The text for this note.
     */
    public String getTextNote()
    {
        return textNote;
    }

    /**
     * Set the text for this note.
     *
     * @param textNote The text note.
     */
    public void setTextNote(String textNote)
    {
        this.textNote = textNote;
    }

    /**
     * Get the subject id for this note.
     *
     * @return The subject if for this note.
     */
    public int getSubjectId()
    {
        return subjectId;
    }

    /**
     * Set the subject id for this note.
     *
     * @param subjectId The subject id for this note.
     */
    public void setSubjectId(int subjectId)
    {
        this.subjectId = subjectId;
    }

    /**
     * Get the database id for this TextNote.
     *
     * @return The database id for this TextNote.
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
        return "TextNote:" +
                "\n_id = " + _id +
                "\ntextNote = " + textNote +
                "\nsubjectId = " + subjectId;
    }
}