package com.ashlimeianwarren.saaf.Beans.WheresMyCar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.ashlimeianwarren.saaf.Implementation.DbCon;
import java.util.ArrayList;

/**
 * Created by Twiz on 20/02/2015.
 */
public class PointOfInterest
{
    private int _id;
    private float longitude, latitude;
    private String info;
    private DbCon dbCon;

    public PointOfInterest()
    {
    }

    public PointOfInterest(float longitude, float latitude, String info)
    {
        this.longitude = longitude;
        this.latitude = latitude;
        this.info = info;
    }

    public PointOfInterest(int _id, float longitude, float latitude, String info)
    {
        this._id = _id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.info = info;
    }

    public void persist(Context context)
    {
        dbCon = new DbCon(context, null);
        SQLiteDatabase db = dbCon.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DbCon.COLUMN_WMC_LATITUDE, this.latitude);
        contentValues.put(DbCon.COLUMN_WMC_LONGITUDE, this.longitude);
        contentValues.put(DbCon.COLUMN_WMC_INFORMATION, this.info);

        this._id = (int)db.insert(DbCon.TABLE_WMC_LOCATIONS, null, contentValues);

        db.close();
    }

    public PointOfInterest[] retrieve(Context context)
    {
        ArrayList<PointOfInterest> poiList = new ArrayList<>();

        String query = "SELECT * FROM " + DbCon.TABLE_WMC_LOCATIONS + " WHERE 1;";

        dbCon = new DbCon(context, null);
        SQLiteDatabase db = dbCon.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(!(cursor.isAfterLast() &&
                cursor.getString(cursor.getColumnIndex(DbCon.COLUMN_WMC_INFORMATION)).isEmpty()))
        {
            int retId = cursor.getInt(cursor.getColumnIndex(DbCon.COLUMN_WMC_ID));
            float retLat = cursor.getFloat(cursor.getColumnIndex(DbCon.COLUMN_WMC_LATITUDE));
            float retLong = cursor.getFloat(cursor.getColumnIndex(DbCon.COLUMN_WMC_LONGITUDE));
            String retInfo = cursor.getString(cursor.getColumnIndex(DbCon.COLUMN_WMC_INFORMATION));
            poiList.add(new PointOfInterest(retId, retLat, retLong, retInfo));
        }
        db.close();
        cursor.close();

        PointOfInterest[] result = new PointOfInterest[poiList.size()];
        poiList.toArray(result);

        return result;
    }

    public float getLongitude()
    {
        return longitude;
    }

    public void setLongitude(float longitude)
    {
        this.longitude = longitude;
    }

    public float getLatitude()
    {
        return latitude;
    }

    public void setLatitude(float latitude)
    {
        this.latitude = latitude;
    }

    public String getInfo()
    {
        return info;
    }

    public void setInfo(String info)
    {
        this.info = info;
    }

    public int get_id()
    {
        return _id;
    }

    @Override
    public String toString()
    {
        return "PointOfInterest:" +
                "\n_id = " + _id +
                "\nlongitude = " + longitude +
                "\nlatitude = " + latitude +
                "\ninfo = " + info;
    }
}
