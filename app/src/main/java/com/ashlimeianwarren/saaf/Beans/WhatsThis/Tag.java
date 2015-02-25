package com.ashlimeianwarren.saaf.Beans.WhatsThis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ashlimeianwarren.saaf.Implementation.DbCon;

/**
 * Tag class.
 *
 * Defines an object that will store NFC tag information.
 */
public class Tag
{
    private int _id;
    private String tagText;
    private DbCon dbCon;

    /**
     * Default empty constructor.
     */
    public Tag()
    {
    }

    /**
     * Constructor for a Tag object.
     *
     * @param tagText The unique string identifier for the tag.
     */
    public Tag(String tagText)
    {
        this.tagText = tagText;
    }

    private Tag(int _id, String tagText)
    {
        this._id = _id;
        this.tagText = tagText;
    }

    /**
     * Persist or save this Tag to the database.
     *
     * @param context The context from which this method was called.
     */
    public void persist(Context context)
    {
        dbCon = new DbCon(context, null);
        SQLiteDatabase db = dbCon.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DbCon.COLUMN_WT_TAGTEXT, this.tagText);

        this._id = (int) db.insert(DbCon.TABLE_WT_TAG, null, contentValues);

        db.close();
    }

    /**
     * Retrieve a tag from the database by its unique string identifier.
     *
     * @param tagText The unique string identifier for the tag.
     * @param context The context from which this method was called.
     * @return A tag associated with the input tag text.
     */
    public Tag retrieve(String tagText, Context context)
    {
        String query = "SELECT * FROM " + DbCon.TABLE_WT_TAG +
                " WHERE " + DbCon.COLUMN_WT_TAGTEXT + " = '" + tagText + "';";

        dbCon = new DbCon(context, null);
        SQLiteDatabase db = dbCon.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        int retId = cursor.getInt(cursor.getColumnIndex(DbCon.COLUMN_WT_ID));
        String retTagText = cursor.getString(cursor.getColumnIndex(DbCon.COLUMN_WT_TAGTEXT));

        return new Tag(retId, retTagText);
    }

    /**
     * Get the unique string identifier for the tag.
     *
     * @return  The unique string identifier for the tag.
     */
    public String getTagText()
    {
        return tagText;
    }

    /**
     * Set the unique string identifier for the tag.
     *
     * @param tagText The unique string identifier for the tag.
     */
    public void setTagText(String tagText)
    {
        this.tagText = tagText;
    }

    /**
     * Get the database id for this tag.
     *
     * @return
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
        return "Tag:" +
                "\n_id = " + _id +
                "\ntagText = " + tagText;
    }
}
