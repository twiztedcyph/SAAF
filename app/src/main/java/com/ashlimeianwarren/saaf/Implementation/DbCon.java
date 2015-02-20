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

    private static final String TABLE_WHT_SUBJECT = "wht_subject";
    private static final String COLUMN_WHT_ID = "_id";
    private static final String COLUMN_WHT_TITLE = "title";

    private static final String TABLE_WHT_AUDIONOTE = "wht_audio_note";
    private static final String COLUMN_WHT_FILEPATH = "file_path";
    private static final String COLUMN_WHT_SUBJECTID = "subject_id";

    private static final String TABLE_WHT_IMAGENOTE = "wht_image_note";

    private static final String TABLE_WHT_TEXTNOTE = "wht_text_note";
    private static final String COLUMN_WHT_TEXT = "subject_text";

    /**********************************************************************/

    private static final String TABLE_WT_TAG = "wt_tag";
    private static final String COLUMN_WT_ID = "_id";
    private static final String COLUMN_WT_TAGTEXT = "tag_text";

    private static final String TABLE_WT_DATA = "wt_data";
    private static final String COLUMN_WT_NAME = "data_name";
    private static final String COLUMN_WT_DESCRIPTION = "data_description";
    private static final String COLUMN_WT_TAGID = "tag_id";

    private static final String TABLE_WT_IMAGE = "wt_image";
    private static final String COLUMN_WT_IMAGETITLE = "image_title";
    private static final String COLUMN_WT_IMAGEPATH = "image_path";
    private static final String COLUMN_WT_DATAID = "data_id";

    /**********************************************************************/

    private static final String TABLE_WMC_LOCATIONS = "point_of_interest";
    private static final String COLUMN_WMC_ID = "_id";
    private static final String COLUMN_WMC_LONGITUDE = "longitude";
    private static final String COLUMN_WMC_LATITUDE = "latitude";
    private static final String COLUMN_WMC_INFORMATION = "information";

    /**********************************************************************/

    public final String TAG = "com.ashlimeianwarren.saaf";

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
        String createWhtImageTable =
                "CREATE TABLE " + TABLE_WHT_IMAGENOTE +
                "(" +
                COLUMN_WHT_ID +  " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_WHT_FILEPATH + " VARCHAR(200) NOT NULL," +
                COLUMN_WHT_SUBJECTID + " INTEGER REFERENCES " +
                        TABLE_WHT_SUBJECT +
                        "(" + COLUMN_WHT_ID + ") ON DELETE CASCADE" +
                ");";
        String createWhtAudioTable =
                "CREATE TABLE " + TABLE_WHT_AUDIONOTE +
                        "(" +
                        COLUMN_WHT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_WHT_FILEPATH + " VARCHAR(200) NOT NULL," +
                        COLUMN_WHT_SUBJECTID + " INTEGER REFERENCES " +
                        TABLE_WHT_SUBJECT +
                        "(" + COLUMN_WHT_ID + ") ON DELETE CASCADE" +
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
        db.execSQL(createWhtImageTable);
        db.execSQL(createWhtAudioTable);
        db.execSQL(createWhtTextTable);
        /*************************************************************************/

        String createWtTagTable = "CREATE TABLE " + TABLE_WT_TAG +
                "(" +
                COLUMN_WT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_WT_TAGTEXT + " VARCHAR(50) NOT NULL" +
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WHT_IMAGENOTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WHT_AUDIONOTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WHT_TEXTNOTE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WT_TAG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WT_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WT_IMAGE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WMC_LOCATIONS);

        onCreate(db);
    }
}
