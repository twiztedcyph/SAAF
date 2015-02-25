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
public class MediaNote
{
    /*
    ASH....
    Note that I changed this. We no longer have audio, image and maybe video notes...
    Instead we have media notes and must just set the type...
    i = image
    a = audio
    v = video
    I can make this an enum if required.
     */
    private int _id;
    private String mediaType;
    private String filePath;
    private int subjectId;
    private DbCon dbCon;

    /**
     * Default empty constructor.
     */
    public MediaNote()
    {
    }

    /**
     * Constructor for a MediaNote object.
     *
     * @param mediaType 'i' - Image, 'a' - Audio, 'v' - Video.
     * @param filePath  The path to the media file.
     * @param subjectId The subject id for this MediaNote.
     */
    public MediaNote(String mediaType, String filePath, int subjectId)
    {
        this.mediaType = mediaType;
        this.filePath = filePath;
        this.subjectId = subjectId;
    }

    private MediaNote(int _id, String mediaType, String filePath, int subjectId)
    {
        /*
        Private constructor with id included.
        Used to initialise from database.
         */
        this._id = _id;
        this.mediaType = mediaType;
        this.filePath = filePath;
        this.subjectId = subjectId;
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
        this._id = (int) db.insert(DbCon.TABLE_WHT_MEDIANOTE, null, contentValues);

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

            mediaNoteList.add(new MediaNote(retId, retMediaType, retFilePath, retSubjectId));
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
     * Get the subject id.
     *
     * @return The subject id.
     */
    public long getSubjectId()
    {
        return subjectId;
    }

    /**
     * Set the subject id.
     *
     * @param subjectId The subject id.
     */
    public void setSubjectId(int subjectId)
    {
        this.subjectId = subjectId;
    }

    /**
     * Get the database id for this MediaNote.
     *
     * @return The database id for this MediaNote.
     */
    public long get_id()
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
        return "MediaNote:" +
                "\n_id = " + _id +
                "\nmediaType = " + mediaType +
                "\nfilePath = " + filePath +
                "\nsubjectId = " + subjectId;
    }
}
