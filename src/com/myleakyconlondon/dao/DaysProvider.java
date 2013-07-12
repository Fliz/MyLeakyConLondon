package com.myleakyconlondon.dao;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * User: Elizabeth
 * Date: 23/06/13
 * Time: 13:29
 */
public class DaysProvider extends ContentProvider {

    private static final String AUTHORITY = "com.myleakyconlondon.dao.DaysProvider";
    private static final String BASE_PATH = "days";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    private DaysSQLiteHelper dayDatabaseHelper;

    @Override
    public boolean onCreate() {
        dayDatabaseHelper = new DaysSQLiteHelper(getContext());
        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase dayDatabase = dayDatabaseHelper.getWritableDatabase();

        final String nullColumnHack = null;
        final long id = dayDatabase.insertWithOnConflict(DataContract.Day.TABLE_NAME, nullColumnHack, values, SQLiteDatabase.CONFLICT_IGNORE);

        if (id > -1) {
            final Uri insertedId = ContentUris.withAppendedId(CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(insertedId, null);

            return insertedId;
        }
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase dayDatabase;

        try {
            dayDatabase = dayDatabaseHelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            dayDatabase = dayDatabaseHelper.getReadableDatabase();
        }
        String groupBy = null;
        String having = null;

        final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (URI_MATCHER.match(uri)) {
            case SINGLEROW:
                final String rowId = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(DataContract.Day.DAY_ID + " = " + rowId);
            default:
                break;
        }

        queryBuilder.setTables(DataContract.Day.TABLE_NAME);

        final Cursor cursor = queryBuilder.query(dayDatabase, projection, selection, selectionArgs, groupBy, having, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase dayDatabase = dayDatabaseHelper.getWritableDatabase();
        int updateCount = 0;

        switch (URI_MATCHER.match(uri)) {
            case SINGLEROW:
                final String rowId = uri.getPathSegments().get(1);
                selection = DataContract.Day.DAY_ID + " = " + rowId + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
                updateCount = dayDatabase.update(DataContract.Day.TABLE_NAME, values, selection, selectionArgs);
            default:
                break;
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return updateCount;
    }

    @Override
    public String getType(Uri uri) {

        switch (URI_MATCHER.match(uri)) {
            case ALLROWS:
                return "vnd.android.cursor.dir/vnd.myleakyconlondon.days";
            case SINGLEROW:
                return "vnd.android.cursor.item/vnd.myleakyconlondon.day";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase dayDatabase = dayDatabaseHelper.getWritableDatabase();

        switch (URI_MATCHER.match(uri)) {
            case SINGLEROW:
                final String rowId = uri.getPathSegments().get(1);
                selection = DataContract.Day.DAY_ID + " = " + rowId + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
            default:
                break;
        }

        if (selection == null) {
            selection = "1";
        }

        int deleteCount = dayDatabase.delete(DataContract.Day.TABLE_NAME, selection, selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);

        return deleteCount;
    }

    private static final int ALLROWS = 1;
    private static final int SINGLEROW = 2;
    private static final UriMatcher URI_MATCHER;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, DataContract.Day.TABLE_NAME, ALLROWS);
        URI_MATCHER.addURI(AUTHORITY, DataContract.Day.TABLE_NAME + "/#", SINGLEROW);
    }
}
