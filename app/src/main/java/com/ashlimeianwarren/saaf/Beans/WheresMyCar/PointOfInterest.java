package com.ashlimeianwarren.saaf.Beans.WheresMyCar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ashlimeianwarren.saaf.Implementation.DbCon;

import java.util.ArrayList;

/**
 * PointOfInterest class.
 * <p/>
 * Defines an object that stores details about a point of interest.
 */
public class PointOfInterest
{
    private int _id;
    private float longitude, latitude;
    private String info;
    private DbCon dbCon;

    /**
     * Default empty constructor.
     */
    public PointOfInterest()
    {
    }

    /**
     * Constructor for a PointOfInterst object.
     *
     * @param longitude The longitude of the object.
     * @param latitude  The latitude of the object.
     * @param info      Any associated information for the object.
     */
    public PointOfInterest(float longitude, float latitude, String info)
    {
        this.longitude = longitude;
        this.latitude = latitude;
        this.info = info;
    }

    public PointOfInterest(int _id, float longitude, float latitude, String info)
    {
        /*
        Private constructor with id included.
        Used to initialise from database.
         */
        this._id = _id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.info = info;
    }

    /**
     * Persist or save this PointOfInterest to the database.
     *
     * @param context The context from which this method was called.
     */
    public void persist(Context context)
    {
        dbCon = new DbCon(context, null);
        SQLiteDatabase db = dbCon.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DbCon.COLUMN_WMC_LATITUDE, this.latitude);
        contentValues.put(DbCon.COLUMN_WMC_LONGITUDE, this.longitude);
        contentValues.put(DbCon.COLUMN_WMC_INFORMATION, this.info);

        this._id = (int) db.insert(DbCon.TABLE_WMC_LOCATIONS, null, contentValues);

        db.close();
    }

    /**
     * Retrieve all PointOfInterest from the database.
     *
     * @param context The context from which this method was called.
     * @return An array of all PointOfInterest from the database.
     */
    public PointOfInterest[] retrieve(Context context)
    {
        ArrayList<PointOfInterest> poiList = new ArrayList<>();

        String query = "SELECT * FROM " + DbCon.TABLE_WMC_LOCATIONS + " WHERE 1;";

        dbCon = new DbCon(context, null);
        SQLiteDatabase db = dbCon.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while (!(cursor.isAfterLast() &&
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

    /**
     * Get the longitude for this object.
     *
     * @return The longitude of the object.
     */
    public float getLongitude()
    {
        return longitude;
    }

    /**
     * Set the longitude for this object.
     *
     * @param longitude The longitude of the object.
     */
    public void setLongitude(float longitude)
    {
        this.longitude = longitude;
    }

    /**
     * Get the latitude for this object.
     *
     * @return The latitude of the object.
     */
    public float getLatitude()
    {
        return latitude;
    }

    /**
     * Set the latitude for this object.
     *
     * @param latitude The latitude of the object.
     */
    public void setLatitude(float latitude)
    {
        this.latitude = latitude;
    }

    /**
     * Get associated information.
     *
     * @return Any associated information for the object.
     */
    public String getInfo()
    {
        return info;
    }

    /**
     * Set associated information.
     *
     * @param info Any associated information for the object.
     */
    public void setInfo(String info)
    {
        this.info = info;
    }

    /**
     * Get the database id for this PointOfInterest.
     *
     * @return
     */
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
