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
public class TextNote extends Note
{

    private String textName, textNote;


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
    public TextNote(String textName, String textNote, int subjectId, String fileType)
    {
        super(subjectId, fileType);
        this.textName = textName;
        this.textNote = textNote;
    }

    public TextNote(int _id, String textName, String textNote, int subjectId, String fileType)
    {
        super(_id, subjectId, fileType);
        this.textName = textName;
        this.textNote = textNote;
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
        contentValues.put(DbCon.COLUMN_WHT_TEXTNAME, textName);
        contentValues.put(DbCon.COLUMN_WHT_TEXT, textNote);
        contentValues.put(DbCon.COLUMN_WHT_SUBJECTID, subjectId);
        this._id = (int) db.insert(DbCon.TABLE_WHT_TEXTNOTE, null, contentValues);

        db.close();
    }


    /**
     * Update this TextNote in the database.
     * 0
     * @param context   The context from which this method was called.
     */
    public void update(Context context)
    {
        dbCon = new DbCon(context, null);
        SQLiteDatabase db = dbCon.getWritableDatabase();

        String query = "UPDATE " + DbCon.TABLE_WHT_TEXTNOTE +
                " SET " + DbCon.COLUMN_WHT_TEXT +
                " = '" + this.getTextNote() +
                "' WHERE " + DbCon.COLUMN_WHT_ID + " = " + this.get_id() +
                ";";

        db.execSQL(query);
        db.close();
    }

    /**
     * Delete this text note from to the database.
     *
     * @param context The context from which this method was called.
     * @param textId  The id of the note to be deleted
     */
    public void delete(int textId, Context context)
    {
        dbCon = new DbCon(context, null);
        SQLiteDatabase db = dbCon.getWritableDatabase();

        String query = "DELETE FROM " + DbCon.TABLE_WHT_TEXTNOTE +
                " WHERE " + DbCon.COLUMN_WHT_ID + " = " + textId + ";";

        db.execSQL(query);
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
            String retTextName = cursor.getString(cursor.getColumnIndex(DbCon.COLUMN_WHT_TEXTNAME));
            String retTextNote = cursor.getString(cursor.getColumnIndex(DbCon.COLUMN_WHT_TEXT));
            int retSubjectId = cursor.getInt(cursor.getColumnIndex(DbCon.COLUMN_WHT_SUBJECTID));

            textNoteList.add(new TextNote(retId, retTextName, retTextNote, retSubjectId, "Text"));
            cursor.moveToNext();
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
     * Get the name of this text note.
     *
     * @return The name of this text note.
     */
    public String getTextName()
    {
        return textName;
    }

    /**
     * Set the name of this text note.
     *
     * @param textName The name of this text note.
     */
    public void setTextName(String textName)
    {
        this.textName = textName;
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
                "\ntextName = " + textName +
                "\ntextNote = " + textNote +
                "\nsubjectId = " + subjectId;
    }
}
