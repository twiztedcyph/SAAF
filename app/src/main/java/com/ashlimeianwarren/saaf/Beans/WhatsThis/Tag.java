package com.ashlimeianwarren.saaf.Beans.WhatsThis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ashlimeianwarren.saaf.Implementation.DbCon;

/**
 * Created by Twiz on 20/02/2015.
 */
public class Tag
{
    private int _id;
    private String tagText;
    private DbCon dbCon;

    public Tag()
    {
    }

    public Tag(String tagText)
    {
        this.tagText = tagText;
    }

    private Tag(int _id, String tagText)
    {
        this._id = _id;
        this.tagText = tagText;
    }

    public void persist(Context context)
    {
        dbCon = new DbCon(context, null);
        SQLiteDatabase db = dbCon.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DbCon.COLUMN_WT_TAGTEXT, this.tagText);

        this._id = (int)db.insert(DbCon.TABLE_WT_TAG, null, contentValues);

        db.close();
    }

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

    public String getTagText()
    {
        return tagText;
    }

    public void setTagText(String tagText)
    {
        this.tagText = tagText;
    }

    public int get_id()
    {
        return _id;
    }

    @Override
    public String toString()
    {
        return "Tag:" +
                "\n_id = " + _id +
                "\ntagText = " + tagText;
    }
}
