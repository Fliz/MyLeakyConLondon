package com.myleakyconlondon.dao;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;

/**
 * User: Elizabeth Hamlet
 */
public class EventSQLiteHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT,";
    private static final String TEXT_NOT_NULL = " TEXT NOT NULL,";

    private static final String DATABASE_CREATE =
            "CREATE TABLE " + DataContract.Event.TABLE_NAME + "(" +
                    DataContract.Event.EVENT_ID + " INTEGER PRIMARY KEY," +
                    DataContract.Event.TITLE + TEXT_NOT_NULL +
                    DataContract.Event.DESCRIPTION + TEXT_TYPE +
                    DataContract.Event.START_DATE + " INTEGER," +
                    DataContract.Event.END_DATE + " INTEGER," +
                    DataContract.Event.LOCATION + TEXT_TYPE +
                    DataContract.Event.TYPE + TEXT_TYPE +
                    DataContract.Event.IS_BACKUP_EVENT + TEXT_TYPE +
                    DataContract.Event.BACKUP_EVENT_ID + " INTEGER," +
                    DataContract.Event.DAY_ID + " INTEGER," +
                    DataContract.Event.DAY_END_ID + " INTEGER" +
            ");";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "LondonLeakycon.db";

    public EventSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        Log.i(EventSQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to "+ newVersion + ".");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataContract.Event.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
