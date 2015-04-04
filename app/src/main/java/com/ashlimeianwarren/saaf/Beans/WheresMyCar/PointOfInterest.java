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
    private double longitude, latitude;
    private String info;
    private DbCon dbCon;

    /**
     * Default empty constructor.
     */
    public PointOfInterest()
    {
    }

    /**
     * Constructor for a PointOfInterest object.
     *
     * @param longitude The longitude of the object.
     * @param latitude  The latitude of the object.
     * @param info      Any associated information for the object.
     */
    public PointOfInterest(double longitude, double latitude, String info)
    {
        this.longitude = longitude;
        this.latitude = latitude;
        this.info = info;
    }

    /**
     * Constructor for a PointOfInterest object.
     *
     * @param _id       The id for the object.
     * @param longitude The longitude of the object.
     * @param latitude  The latitude of the object.
     * @param info      Any associated information for the object.
     */
    public PointOfInterest(int _id, double longitude, double latitude, String info)
    {
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

        while (!(cursor.isAfterLast()))
        {
            int retId = cursor.getInt(cursor.getColumnIndex(DbCon.COLUMN_WMC_ID));
            double retLat = cursor.getDouble(cursor.getColumnIndex(DbCon.COLUMN_WMC_LATITUDE));
            double retLong = cursor.getDouble(cursor.getColumnIndex(DbCon.COLUMN_WMC_LONGITUDE));
            String retInfo = cursor.getString(cursor.getColumnIndex(DbCon.COLUMN_WMC_INFORMATION));
            poiList.add(new PointOfInterest(retId, retLong, retLat, retInfo));
            cursor.moveToNext();
        }
        db.close();
        cursor.close();

        PointOfInterest[] result = new PointOfInterest[poiList.size()];
        poiList.toArray(result);

        return result;
    }

    /**
     * Delete a PointOfInterest from the database.
     *
     * @param pointOfInterestId     ID of the PointOfInterest for deletion.
     * @param context               The context from which this method was called.
     */
    public void delete(int pointOfInterestId, Context context)
    {
        dbCon = new DbCon(context, null);

        SQLiteDatabase db = dbCon.getWritableDatabase();
        String query = "DELETE FROM " + DbCon.TABLE_WMC_LOCATIONS +
                " WHERE " + DbCon.COLUMN_WMC_ID + " = " + pointOfInterestId + ";";
        db.execSQL(query);
        db.close();
    }

    /**
     * Get the longitude for this object.
     *
     * @return The longitude of the object.
     */
    public double getLongitude()
    {
        return longitude;
    }

    /**
     * Set the longitude for this object.
     *
     * @param longitude The longitude of the object.
     */
    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    /**
     * Get the latitude for this object.
     *
     * @return The latitude of the object.
     */
    public double getLatitude()
    {
        return latitude;
    }

    /**
     * Set the latitude for this object.
     *
     * @param latitude The latitude of the object.
     */
    public void setLatitude(double latitude)
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
     * @return The database id for this PointOfInterest.
     */
    public int get_id()
    {
        return _id;
    }

    /**
     * Method to return a string representation of this PointOfInterest.
     * @return String representation of this PointOfInterest.
     */
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
