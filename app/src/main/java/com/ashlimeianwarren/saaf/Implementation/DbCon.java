package com.ashlimeianwarren.saaf.Implementation;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Twiz on 20/02/2015.
 */
public class DbCon extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "saaf.db";

    /**********************************************************************/

    public static final String TABLE_WHT_SUBJECT = "wht_subject";
    public static final String COLUMN_WHT_ID = "_id";
    public static final String COLUMN_WHT_TITLE = "title";

    public static final String TABLE_WHT_MEDIANOTE = "wht_audio_note";
    public static final String COLUMN_WHT_MEDIATYPE = "media_type";
    public static final String COLUMN_WHT_FILEPATH = "file_path";
    public static final String COLUMN_WHT_SUBJECTID = "subject_id";

    public static final String TABLE_WHT_TEXTNOTE = "wht_text_note";
    public static final String COLUMN_WHT_TEXT = "subject_text";

    /**********************************************************************/

    public static final String TABLE_WT_TAG = "wt_tag";
    public static final String COLUMN_WT_ID = "_id";
    public static final String COLUMN_WT_TAGTEXT = "tag_text";

    public static final String TABLE_WT_DATA = "wt_data";
    public static final String COLUMN_WT_NAME = "data_name";
    public static final String COLUMN_WT_DESCRIPTION = "data_description";
    public static final String COLUMN_WT_TAGID = "tag_id";

    public static final String TABLE_WT_IMAGE = "wt_image";
    public static final String COLUMN_WT_IMAGETITLE = "image_title";
    public static final String COLUMN_WT_IMAGEPATH = "image_path";
    public static final String COLUMN_WT_DATAID = "data_id";

    /**********************************************************************/

    public static final String TABLE_WMC_LOCATIONS = "point_of_interest";
    public static final String COLUMN_WMC_ID = "_id";
    public static final String COLUMN_WMC_LONGITUDE = "longitude";
    public static final String COLUMN_WMC_LATITUDE = "latitude";
    public static final String COLUMN_WMC_INFORMATION = "information";

    /**********************************************************************/

    public final String TAG = "ashlimeianwarren.saaf";

    /**
     *
     * @param context
     * @param factory
     */
    public DbCon(Context context, SQLiteDatabase.CursorFactory factory)
    {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);

        getWritableDatabase();

        close();
    }

    /**
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.i(TAG, "Starting database creation.");
        String createWhtSubjectTable =
                "CREATE TABLE " + TABLE_WHT_SUBJECT +
                "(" +
                COLUMN_WHT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_WHT_TITLE + " VARCHAR(50) NOT NULL" +
                ");";

        String createWhtAudioTable =
                "CREATE TABLE " + TABLE_WHT_MEDIANOTE +
                        "(" +
                        COLUMN_WHT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_WHT_MEDIATYPE + " VARCHAR(1), " +
                        COLUMN_WHT_FILEPATH + " VARCHAR(200) NOT NULL, " +
                        COLUMN_WHT_SUBJECTID + " INTEGER REFERENCES " +
                            TABLE_WHT_SUBJECT + "(" + COLUMN_WHT_ID + ") ON DELETE CASCADE" +
                        ");";
        String createWhtTextTable =
                "CREATE TABLE " + TABLE_WHT_TEXTNOTE +
                        "(" +
                        COLUMN_WHT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_WHT_TEXT + " TEXT NOT NULL," +
                        COLUMN_WHT_SUBJECTID + " INTEGER REFERENCES " +
                            TABLE_WHT_SUBJECT + "(" + COLUMN_WHT_ID + ") ON DELETE CASCADE" +
                        ");";

        db.execSQL(createWhtSubjectTable);
        db.execSQL(createWhtAudioTable);
        db.execSQL(createWhtTextTable);
        /*************************************************************************/

        String createWtTagTable = "CREATE TABLE " + TABLE_WT_TAG +
                "(" +
                COLUMN_WT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_WT_TAGTEXT + " VARCHAR(50) UNIQUE NOT NULL" +
                ");";
        String createWtDataTable = "CREATE TABLE " + TABLE_WT_DATA +
                "(" +
                COLUMN_WT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_WT_NAME + " VARCHAR(50) NOT NULL, " +
                COLUMN_WT_DESCRIPTION + " TEXT NOT NULL, " +
                COLUMN_WT_TAGID + " INTEGER REFERENCES " +
                    TABLE_WT_TAG + "(" + COLUMN_WT_ID + ") ON DELETE CASCADE" +
                ");";
        String createWtImageTable = "CREATE TABLE " + TABLE_WT_IMAGE +
                "(" +
                COLUMN_WT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_WT_IMAGETITLE + " VARCHAR(50) NOT NULL, " +
                COLUMN_WT_IMAGEPATH + " VARCHAR(200) NOT NULL, " +
                COLUMN_WT_DATAID + " INTEGER REFERENCES " +
                    TABLE_WT_DATA + "(" + COLUMN_WT_ID + ") ON DELETE CASCADE" +
                ");";

        db.execSQL(createWtTagTable);
        db.execSQL(createWtDataTable);
        db.execSQL(createWtImageTable);
        /*************************************************************************/

        String createWmcTable = "CREATE TABLE " + TABLE_WMC_LOCATIONS +
                "(" +
                COLUMN_WMC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_WMC_LONGITUDE + " REAL NOT NULL, " +
                COLUMN_WMC_LATITUDE + " REAL NOT NULL, " +
                COLUMN_WMC_INFORMATION + " TEXT" +
                ");";
        db.execSQL(createWmcTable);

        Log.i(TAG, "Database creation complete");
    }

    /**
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WHT_SUBJECT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WHT_MEDIANOTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WHT_TEXTNOTE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WT_TAG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WT_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WT_IMAGE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WMC_LOCATIONS);

        onCreate(db);
    }
}
