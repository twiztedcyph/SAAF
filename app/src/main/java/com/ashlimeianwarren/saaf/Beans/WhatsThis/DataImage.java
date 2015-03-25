package com.ashlimeianwarren.saaf.Beans.WhatsThis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ashlimeianwarren.saaf.Implementation.DbCon;

import java.util.ArrayList;

/**
 * DataImage class.
 * <p/>
 * Defines an object that points to images associated with a Data object.
 */
public class DataImage
{
    private int _id;
    private String imageTitle, imagePath;
    private int dataId;
    private DbCon dbCon;

    /**
     * Default empty constructor.
     */
    public DataImage()
    {
    }

    /**
     * Constructor for a DataImage object.
     *
     * @param imageTitle The title of the image.
     * @param imagePath  The path to the image.
     * @param dataId     The associated Data object's id.
     */
    public DataImage(String imageTitle, String imagePath, int dataId)
    {
        this.imageTitle = imageTitle;
        this.imagePath = imagePath;
        this.dataId = dataId;
    }

    private DataImage(int _id, String imageTitle, String imagePath, int dataId)
    {
        /*
        Private constructor with id included.
        Used to initialise from database.
         */
        this._id = _id;
        this.imageTitle = imageTitle;
        this.imagePath = imagePath;
        this.dataId = dataId;
    }

    /**
     * Persist or save this DataImage to the database.
     *
     * @param context The context from which this method was called.
     */
    public void persist(Context context)
    {
        dbCon = new DbCon(context, null);
        SQLiteDatabase db = dbCon.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DbCon.COLUMN_WT_IMAGETITLE, this.imageTitle);
        contentValues.put(DbCon.COLUMN_WT_IMAGEPATH, this.imagePath);
        contentValues.put(DbCon.COLUMN_WT_DATAID, this.dataId);

        this._id = (int) db.insert(DbCon.TABLE_WT_IMAGE, null, contentValues);

        db.close();
    }

    /**
     * Retrieve all DataImages for a specific data id from the database.
     *
     * @param dataId  The associated Data object's id.
     * @param context The context from which this method was called.
     * @return An array of DataImages for the data id.
     */
    public DataImage[] retrieve(int dataId, Context context)
    {
        ArrayList<DataImage> dataImageList = new ArrayList<>();

        String query = "SELECT * FROM " + DbCon.TABLE_WT_IMAGE +
                " WHERE " + DbCon.COLUMN_WT_DATAID + " = " + dataId + ";";

        dbCon = new DbCon(context, null);
        SQLiteDatabase db = dbCon.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast() &&
                !cursor.getString(cursor.getColumnIndex(DbCon.COLUMN_WT_IMAGETITLE)).isEmpty())
        {
            int retId = cursor.getInt(cursor.getColumnIndex(DbCon.COLUMN_WT_ID));
            String retTitle = cursor.getString(cursor.getColumnIndex(DbCon.COLUMN_WT_IMAGETITLE));
            String retpath = cursor.getString(cursor.getColumnIndex(DbCon.COLUMN_WT_IMAGEPATH));
            int retDataId = cursor.getInt(cursor.getColumnIndex(DbCon.COLUMN_WT_DATAID));
            dataImageList.add(new DataImage(retId, retTitle, retpath, retDataId));
            cursor.moveToNext();
        }
        db.close();
        cursor.close();

        DataImage[] result = new DataImage[dataImageList.size()];
        dataImageList.toArray(result);
        return result;
    }

    /**
     * Get the title.
     *
     * @return The title of the image.
     */
    public String getImageTitle()
    {
        return imageTitle;
    }

    /**
     * Set the title.
     *
     * @param imageTitle The title of the image.
     */
    public void setImageTitle(String imageTitle)
    {
        this.imageTitle = imageTitle;
    }

    /**
     * Get the path to the image.
     *
     * @return The path to the image.
     */
    public String getImagePath()
    {
        return imagePath;
    }

    /**
     * Set the path to the image.
     *
     * @param imagePath The path to the image.
     */
    public void setImagePath(String imagePath)
    {
        this.imagePath = imagePath;
    }

    /**
     * Get the data id.
     *
     * @return The associated Data object's id.
     */
    public int getDataId()
    {
        return dataId;
    }

    /**
     * Set the data id.
     *
     * @param dataId The associated Data object's id.
     */
    public void setDataId(int dataId)
    {
        this.dataId = dataId;
    }

    /**
     * Get the database id for this DataImage.
     *
     * @return The database id for this DataImage.
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
        return "DataImage:" +
                "\n_id = " + _id +
                "\nimageTitle = " + imageTitle +
                "\nimagePath = " + imagePath +
                "\ndataId = " + dataId;
    }
}
