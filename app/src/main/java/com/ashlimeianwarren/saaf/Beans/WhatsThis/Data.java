package com.ashlimeianwarren.saaf.Beans.WhatsThis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ashlimeianwarren.saaf.Implementation.DbCon;

import java.util.ArrayList;

/**
 * Data class.
 * <p/>
 * Defines a Data object that stores information related to an NFC tag.
 */
public class Data
{
    private int _id;
    private String dataName, dataDescription;
    private int tagId;
    private DbCon dbCon;

    /**
     * Default empty constructor.
     */
    public Data()
    {
    }

    /**
     * Constructor for a Data object.
     *
     * @param dataName        The name/title of this Data object.
     * @param dataDescription A description of the data.
     * @param tagId           The tag id this Data object is associated with.
     */
    public Data(String dataName, String dataDescription, int tagId)
    {
        this.dataName = dataName;
        this.dataDescription = dataDescription;
        this.tagId = tagId;
    }

    private Data(int _id, String dataName, String dataDescription, int tagId)
    {
        /*
        Private constructor with id included.
        Used to initialise from database.
         */
        this._id = _id;
        this.dataName = dataName;
        this.dataDescription = dataDescription;
        this.tagId = tagId;
    }

    /**
     * Persist or save this Data to the database.
     *
     * @param context The context from which this method was called.
     */
    public void persist(Context context)
    {
        dbCon = new DbCon(context, null);
        SQLiteDatabase db = dbCon.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DbCon.COLUMN_WT_NAME, this.dataName);
        contentValues.put(DbCon.COLUMN_WT_DESCRIPTION, this.dataDescription);
        contentValues.put(DbCon.COLUMN_WT_TAGID, this.tagId);

        this._id = (int) db.insert(DbCon.TABLE_WT_DATA, null, contentValues);

        db.close();
    }

    /**
     * Retrieve all Data for a specific tag id from the database.
     *
     * @param tagId   The tag id.
     * @param context The context from which this method was called.
     * @return An array of Data for the tag id.
     */
    public Data[] retrieve(int tagId, Context context)
    {
        ArrayList<Data> dataList = new ArrayList<>();

        String query = "SELECT *" +
                " FROM " + DbCon.TABLE_WT_DATA +
                " WHERE " + DbCon.COLUMN_WT_TAGID + " = " + tagId + ";";

        dbCon = new DbCon(context, null);
        SQLiteDatabase db = dbCon.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while (!(cursor.isAfterLast()))
        {
            int retId = cursor.getInt(cursor.getColumnIndex(DbCon.COLUMN_WT_ID));
            String retName = cursor.getString(cursor.getColumnIndex(DbCon.COLUMN_WT_NAME));
            String retDesc = cursor.getString(cursor.getColumnIndex(DbCon.COLUMN_WT_DESCRIPTION));
            int retTagId = cursor.getInt(cursor.getColumnIndex(DbCon.COLUMN_WT_TAGID));

            dataList.add(new Data(retId, retName, retDesc, retTagId));

            cursor.moveToNext();
        }
        db.close();
        cursor.close();

        Data[] result = new Data[dataList.size()];
        dataList.toArray(result);

        return result;
    }

    /**
     * Retrieve all Data for a specific tag id from the database.
     *
     * @param dataName The data name/title.
     * @param context The context from which this method was called.
     * @return An array of Data for the tag id.
     */
    public void retrieve(String dataName, Context context)
    {
        String query = "SELECT *" +
                " FROM " + DbCon.TABLE_WT_DATA +
                " WHERE " + DbCon.COLUMN_WT_NAME + " = '" + dataName + "';";

        dbCon = new DbCon(context, null);
        SQLiteDatabase db = dbCon.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        if (!(cursor.isAfterLast() && cursor.getString(cursor.getColumnIndex(DbCon.COLUMN_WT_NAME)).isEmpty()))
        {
            this._id = cursor.getInt(cursor.getColumnIndex(DbCon.COLUMN_WT_ID));
            this.dataName = cursor.getString(cursor.getColumnIndex(DbCon.COLUMN_WT_NAME));
            this.dataDescription = cursor.getString(cursor.getColumnIndex(DbCon.COLUMN_WT_DESCRIPTION));
            this.tagId = cursor.getInt(cursor.getColumnIndex(DbCon.COLUMN_WT_TAGID));
        }
        db.close();
        cursor.close();
    }

    /**
     * Get the data name/title.
     *
     * @return The data name/title.
     */
    public String getDataName()
    {
        return dataName;
    }

    /**
     * Set the data name/title.
     *
     * @param dataName The data name/title.
     */
    public void setDataName(String dataName)
    {
        this.dataName = dataName;
    }

    /**
     * Get the description.
     *
     * @return A description of the data.
     */
    public String getDataDescription()
    {
        return dataDescription;
    }

    /**
     * Set the description.
     *
     * @param dataDescription A description of the data.
     */
    public void setDataDescription(String dataDescription)
    {
        this.dataDescription = dataDescription;
    }

    /**
     * Get the tag id.
     *
     * @return The tag id this Data object is associated with.
     */
    public int getTagId()
    {
        return tagId;
    }

    /**
     * Set the tag id.
     *
     * @param tagId The tag id this Data object is associated with.
     */
    public void setTagId(int tagId)
    {
        this.tagId = tagId;
    }

    /**
     * Get the database id for this Data.
     *
     * @return The database id for this Data.
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
        return "Data:" +
                "\n_id = " + _id +
                "\ndataName = " + dataName +
                "\ndataDescription = " + dataDescription +
                "\ntagId = " + tagId;
    }
}
