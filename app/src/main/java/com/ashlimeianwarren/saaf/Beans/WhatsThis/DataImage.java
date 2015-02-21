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
public class DataImage
{
    private int _id;
    private String imageTitle, imagePath;
    private int dataId;
    private DbCon dbCon;

    public DataImage()
    {
    }

    public DataImage(String imageTitle, String imagePath, int dataId)
    {
        this.imageTitle = imageTitle;
        this.imagePath = imagePath;
        this.dataId = dataId;
    }

    private DataImage(int _id, String imageTitle, String imagePath, int dataId)
    {
        this._id = _id;
        this.imageTitle = imageTitle;
        this.imagePath = imagePath;
        this.dataId = dataId;
    }

    public void persist(Context context)
    {
        dbCon = new DbCon(context, null);
        SQLiteDatabase db = dbCon.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DbCon.COLUMN_WT_IMAGETITLE, this.imageTitle);
        contentValues.put(DbCon.COLUMN_WT_IMAGEPATH, this.imagePath);
        contentValues.put(DbCon.COLUMN_WT_DATAID, this.dataId);

        this._id = (int)db.insert(DbCon.TABLE_WT_IMAGE, null, contentValues);

        db.close();
    }

    public DataImage[] retrieve(int dataId, Context context)
    {
        ArrayList<DataImage> dataImageList = new ArrayList<>();

        String query = "SELECT * FROM " + DbCon.TABLE_WT_IMAGE +
                " WHERE " + DbCon.COLUMN_WT_DATAID + " = " + dataId + ";";

        dbCon = new DbCon(context, null);
        SQLiteDatabase db = dbCon.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast() &&
                !cursor.getString(cursor.getColumnIndex(DbCon.COLUMN_WT_IMAGETITLE)).isEmpty())
        {
            int retId = cursor.getInt(cursor.getColumnIndex(DbCon.COLUMN_WT_ID));
            String retTitle = cursor.getString(cursor.getColumnIndex(DbCon.COLUMN_WT_IMAGETITLE));
            String retpath = cursor.getString(cursor.getColumnIndex(DbCon.COLUMN_WT_IMAGEPATH));
            int retDataId = cursor.getInt(cursor.getColumnIndex(DbCon.COLUMN_WT_DATAID));

            dataImageList.add(new DataImage(retId, retTitle, retpath, retDataId));
        }
        db.close();
        cursor.close();

        DataImage[] result = new DataImage[dataImageList.size()];
        dataImageList.toArray(result);
        return result;
    }

    public String getImageTitle()
    {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle)
    {
        this.imageTitle = imageTitle;
    }

    public String getImagePath()
    {
        return imagePath;
    }

    public void setImagePath(String imagePath)
    {
        this.imagePath = imagePath;
    }

    public int getDataId()
    {
        return dataId;
    }

    public void setDataId(int dataId)
    {
        this.dataId = dataId;
    }

    public int get_id()
    {
        return _id;
    }

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
