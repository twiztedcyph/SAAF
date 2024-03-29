package com.ashlimeianwarren.saaf.Beans.WhatHappenedToday;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ashlimeianwarren.saaf.Implementation.DbCon;

import java.util.ArrayList;

/**
 * Media note class.
 * <p/>
 * Defines a MediaNote object that points to
 * images, audio or video on a specific subject.
 */
public class MediaNote extends Note
{
    private String mediaType;
    private String filePath;
    private String name;


    /**
     * Default empty constructor.
     */
    public MediaNote()
    {
    }

    /**
     * Constructor for a MediaNote object.
     *
     * @param mediaType "Image" or "Audio".
     * @param filePath  The path to the media file.
     * @param subjectId The subject id for this MediaNote.
     * @param fileType  "Image" or "Audio".
     * @param name      "Name of the file e.g. 'Picture of lecturer'"
     */
    public MediaNote(String mediaType, String filePath, int subjectId, String fileType, String name)
    {
        super(subjectId, fileType);
        this.mediaType = mediaType;
        this.filePath = filePath;
        this.name = name;
    }

    /**
     * Constructor for a MediaNote object.
     *
     * @param _id       The database ID for this MediaNote.
     * @param mediaType "Image" or "Audio".
     * @param filePath  The path to the media file.
     * @param subjectId The subject id for this MediaNote.
     * @param fileType  "Image" or "Audio".
     */
    private MediaNote(int _id, String mediaType, String filePath, int subjectId, String fileType, String name)
    {
        super(_id, subjectId, fileType);
        this.mediaType = mediaType;
        this.filePath = filePath;
        this.name = name;
    }

    /**
     * Persist or save this MediaNote to the database.
     *
     * @param context The context from which this method was called.
     */
    public void persist(Context context)
    {
        dbCon = new DbCon(context, null);
        SQLiteDatabase db = dbCon.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbCon.COLUMN_WHT_MEDIATYPE, mediaType);
        contentValues.put(DbCon.COLUMN_WHT_FILEPATH, filePath);
        contentValues.put(DbCon.COLUMN_WHT_SUBJECTID, subjectId);
        contentValues.put(DbCon.COLUMN_WHT_NAME, name);
        this._id = (int) db.insert(DbCon.TABLE_WHT_MEDIANOTE, null, contentValues);

        db.close();
    }

    /**
     * Delete this MediaNote from to the database.
     *
     * @param context The context from which this method was called.
     * @param noteId  The id of the note to be deleted
     */
    public void delete(int noteId, Context context)
    {
        dbCon = new DbCon(context, null);
        SQLiteDatabase db = dbCon.getWritableDatabase();

        String query = "DELETE FROM " + DbCon.TABLE_WHT_MEDIANOTE +
                " WHERE " + DbCon.COLUMN_WHT_ID + " = " + noteId + ";";

        db.execSQL(query);
        db.close();
    }

    /**
     * Retrieve all MediaNotes for a specific subject from the database.
     *
     * @param subjectId The subject id.
     * @param context   The context from which this method was called.
     * @return An array of MediaNotes for the subject.
     */
    public MediaNote[] retrieve(int subjectId, Context context)
    {
        ArrayList<MediaNote> mediaNoteList = new ArrayList<>();

        String query = "SELECT * FROM " + DbCon.TABLE_WHT_MEDIANOTE +
                " WHERE " + DbCon.COLUMN_WHT_SUBJECTID + " = " + subjectId + ";";

        dbCon = new DbCon(context, null);
        SQLiteDatabase db = dbCon.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();


        while (!cursor.isAfterLast() &&
                !cursor.getString(cursor.getColumnIndex(DbCon.COLUMN_WHT_FILEPATH)).isEmpty())
        {
            int retId = cursor.getInt(cursor.getColumnIndex(DbCon.COLUMN_WHT_ID));
            String retMediaType = cursor.getString(cursor.getColumnIndex(DbCon.COLUMN_WHT_MEDIATYPE));
            String retFilePath = cursor.getString(cursor.getColumnIndex(DbCon.COLUMN_WHT_FILEPATH));
            int retSubjectId = cursor.getInt(cursor.getColumnIndex(DbCon.COLUMN_WHT_SUBJECTID));
            String retName = cursor.getString(cursor.getColumnIndex(DbCon.COLUMN_WHT_NAME));

            mediaNoteList.add(new MediaNote(retId, retMediaType, retFilePath, retSubjectId, retMediaType, retName));
            cursor.moveToNext();

        }
        db.close();
        cursor.close();

        MediaNote[] result = new MediaNote[mediaNoteList.size()];
        mediaNoteList.toArray(result);

        return result;
    }

    /**
     * Get the media file path.
     *
     * @return The media file path.
     */
    public String getFilePath()
    {
        return filePath;
    }

    /**
     * Set the media file path.
     *
     * @param filePath The media file path.
     */
    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }
    /**
     * Get the media note name.
     *
     * @return The media media .
     */
    public String getNoteName()
    {
        return name;
    }

    /**
     * Get a string representation of this object.
     *
     * @return A string representation of this object.
     */
    @Override
    public String toString()
    {
        return "MediaNote:" +
                "\n_id = " + _id +
                "\nmediaType = " + mediaType +
                "\nfilePath = " + filePath +
                "\nsubjectId = " + subjectId;
    }
}
