package com.ashlimeianwarren.saaf.Beans.WhatsThis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ashlimeianwarren.saaf.Implementation.DbCon;

import java.util.ArrayList;

/**
 * Created by Twiz on 20/02/2015.
 */
public class Data
{
    private int _id;
    private String dataName, dataDescription;
    private int tagId;
    private DbCon dbCon;

    public Data()
    {
    }

    public Data(String dataName, String dataDescription, int tagId)
    {
        this.dataName = dataName;
        this.dataDescription = dataDescription;
        this.tagId = tagId;
    }

    private Data(int _id, String dataName, String dataDescription, int tagId)
    {
        this._id = _id;
        this.dataName = dataName;
        this.dataDescription = dataDescription;
        this.tagId = tagId;
    }

    public void persist(Context context)
    {
        dbCon = new DbCon(context, null);
        SQLiteDatabase db = dbCon.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DbCon.COLUMN_WT_NAME, this.dataName);
        contentValues.put(DbCon.COLUMN_WT_DESCRIPTION, this.dataDescription);
        contentValues.put(DbCon.COLUMN_WT_TAGID, this.tagId);

        this._id = (int)db.insert(DbCon.TABLE_WT_DATA, null, contentValues);

        db.close();
    }

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

        while (!(cursor.isAfterLast() &&
                cursor.getString(cursor.getColumnIndex(DbCon.COLUMN_WT_NAME)).isEmpty()))
        {
            int retId = cursor.getInt(cursor.getColumnIndex(DbCon.COLUMN_WT_ID));
            String retName = cursor.getString(cursor.getColumnIndex(DbCon.COLUMN_WT_NAME));
            String retDesc = cursor.getString(cursor.getColumnIndex(DbCon.COLUMN_WT_DESCRIPTION));
            int retTagId = cursor.getInt(cursor.getColumnIndex(DbCon.COLUMN_WT_TAGID));

            dataList.add(new Data(retId, retName, retDesc, retTagId));
        }
        db.close();
        cursor.close();

        Data[] result = new Data[dataList.size()];
        dataList.toArray(result);

        return result;
    }

    public String getDataName()
    {
        return dataName;
    }

    public void setDataName(String dataName)
    {
        this.dataName = dataName;
    }

    public String getDataDescription()
    {
        return dataDescription;
    }

    public void setDataDescription(String dataDescription)
    {
        this.dataDescription = dataDescription;
    }

    public int getTagId()
    {
        return tagId;
    }

    public void setTagId(int tagId)
    {
        this.tagId = tagId;
    }

    public int get_id()
    {
        return _id;
    }

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
