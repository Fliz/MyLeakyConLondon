package com.myleakyconlondon.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * User: Elizabeth
 * Date: 21/06/13
 * Time: 21:10
 */
public class DaysSQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_CREATE =
            "CREATE TABLE " + DataContract.Day.TABLE_NAME + "(" +
                    DataContract.Day.DAY_ID + " INTEGER PRIMARY KEY," +
                    DataContract.Day.DATE + " INTEGER," +
                    DataContract.Day.DAY_NUMBER + " INTEGER" +
                    ");";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "LondonLeakyconDays.db";

    public DaysSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        Log.i(DaysSQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ".");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataContract.Day.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
