package com.ashlimeianwarren.saaf.Implementation;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * DbCon class.
 * <p/>
 * All connections to the onboard SQLite database are handled here.
 */
public class DbCon extends SQLiteOpenHelper
{
    /*
    Definition of all table and column names.
     */

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "saaf.db";
    private Context context;

    /**
     * ******************************************************************
     */

    public static final String TABLE_WHT_SUBJECT = "wht_subject";
    public static final String COLUMN_WHT_ID = "_id";
    public static final String COLUMN_WHT_TITLE = "title";

    public static final String TABLE_WHT_MEDIANOTE = "wht_audio_note";
    public static final String COLUMN_WHT_MEDIATYPE = "media_type";
    public static final String COLUMN_WHT_FILEPATH = "file_path";
    public static final String COLUMN_WHT_SUBJECTID = "subject_id";
    public static final String COLUMN_WHT_NAME = "name";

    public static final String TABLE_WHT_TEXTNOTE = "wht_text_note";
    public static final String COLUMN_WHT_TEXTNAME = "subject_text_name";
    public static final String COLUMN_WHT_TEXT = "subject_text";

    /**
     * ******************************************************************
     */

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

    /**
     * ******************************************************************
     */

    public static final String TABLE_WMC_LOCATIONS = "point_of_interest";
    public static final String COLUMN_WMC_ID = "_id";
    public static final String COLUMN_WMC_LONGITUDE = "longitude";
    public static final String COLUMN_WMC_LATITUDE = "latitude";
    public static final String COLUMN_WMC_INFORMATION = "information";

    /**
     * ******************************************************************
     */

    // Tag for logging.
    public final String TAG = "ashlimeianwarren.saaf";

    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context The context used to open or create the database.
     * @param factory The factory used for creating cursor objects, or null for the default.
     */
    public DbCon(Context context, SQLiteDatabase.CursorFactory factory)
    {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        this.context = context;
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.i(TAG, "Starting database creation.");

        //Setup the what happened today tables.
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
                        COLUMN_WHT_NAME + " VARCHAR(150) NOT NULL," +
                        COLUMN_WHT_SUBJECTID + " INTEGER REFERENCES " +
                        TABLE_WHT_SUBJECT + "(" + COLUMN_WHT_ID + ") ON DELETE CASCADE" +
                        ");";
        String createWhtTextTable =
                "CREATE TABLE " + TABLE_WHT_TEXTNOTE +
                        "(" +
                        COLUMN_WHT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_WHT_TEXTNAME + "  TEXT NOT NULL," +
                        COLUMN_WHT_TEXT + " TEXT NOT NULL," +
                        COLUMN_WHT_SUBJECTID + " INTEGER REFERENCES " +
                        TABLE_WHT_SUBJECT + "(" + COLUMN_WHT_ID + ") ON DELETE CASCADE" +
                        ");";

        db.execSQL(createWhtSubjectTable);
        db.execSQL(createWhtAudioTable);
        db.execSQL(createWhtTextTable);
        /*************************************************************************/

        //Setup the what's this tables.
        String createWtTagTable = "CREATE TABLE " + TABLE_WT_TAG +
                "(" +
                COLUMN_WT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_WT_TAGTEXT + " VARCHAR(50) UNIQUE NOT NULL" +
                ");";
        String createWtDataTable = "CREATE TABLE " + TABLE_WT_DATA +
                "(" +
                COLUMN_WT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_WT_NAME + " VARCHAR(50) UNIQUE NOT NULL, " +
                COLUMN_WT_DESCRIPTION + " TEXT NOT NULL, " +
                COLUMN_WT_TAGID + " INTEGER REFERENCES " +
                TABLE_WT_TAG + "(" + COLUMN_WT_ID + ") ON DELETE CASCADE" +
                ");";
        String createWtImageTable = "CREATE TABLE " + TABLE_WT_IMAGE +
                "(" +
                COLUMN_WT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_WT_IMAGETITLE + " VARCHAR(50) UNIQUE NOT NULL, " +
                COLUMN_WT_IMAGEPATH + " VARCHAR(200) NOT NULL, " +
                COLUMN_WT_DATAID + " INTEGER REFERENCES " +
                TABLE_WT_DATA + "(" + COLUMN_WT_ID + ") ON DELETE CASCADE" +
                ");";

        db.execSQL(createWtTagTable);
        db.execSQL(createWtDataTable);
        db.execSQL(createWtImageTable);
        /*************************************************************************/

        //Setup the wheres my car table.
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

    public void clear(SQLiteDatabase db)
    {
        db.execSQL("DELETE FROM " + TABLE_WHT_SUBJECT);
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //Drop all tables.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WHT_SUBJECT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WHT_MEDIANOTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WHT_TEXTNOTE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WT_TAG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WT_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WT_IMAGE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WMC_LOCATIONS);

        //Recreate the database.
        onCreate(db);
    }
}
