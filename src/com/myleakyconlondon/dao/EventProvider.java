package com.myleakyconlondon.dao;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import android.util.Log;
import com.myleakyconlondon.dao.DataContract.Event;

/**
 * User: Elizabeth Hamlet
 */

public class EventProvider extends ContentProvider {

    private static final String AUTHORITY = "com.myleakyconlondon.dao.EventProvider";
    private static final String BASE_PATH = "events";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    private EventSQLiteHelper eventDatabaseHelper;

    @Override
    public boolean onCreate() {
        eventDatabaseHelper = new EventSQLiteHelper(getContext());
        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase eventDatabase = eventDatabaseHelper.getWritableDatabase();

        final String nullColumnHack = null;
        final long id = eventDatabase.insertWithOnConflict(DataContract.Event.TABLE_NAME, nullColumnHack, values, SQLiteDatabase.CONFLICT_IGNORE);

        if (id > -1) {
            final Uri insertedId = ContentUris.withAppendedId(CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(insertedId, null);

            return insertedId;
        }
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase eventDatabase;

        try {
            eventDatabase = eventDatabaseHelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            eventDatabase = eventDatabaseHelper.getReadableDatabase();
        }
        String groupBy = null;
        String having = null;

        final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (URI_MATCHER.match(uri)) {
            case SINGLEROW:
                final String rowId = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(Event.EVENT_ID + " = " + rowId);
            default:
                break;
        }
        queryBuilder.setTables(Event.TABLE_NAME);

        final Cursor cursor = queryBuilder.query(eventDatabase, projection, selection, selectionArgs, groupBy, having, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase eventDatabase = eventDatabaseHelper.getWritableDatabase();
        int updateCount = 0;

        switch (URI_MATCHER.match(uri)) {
            case SINGLEROW:

                final String rowId = uri.getPathSegments().get(1);
                selection = Event.EVENT_ID + " = " + rowId + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
            default:
                break;
        }

        updateCount = eventDatabase.update(Event.TABLE_NAME, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri,null);

        return updateCount;
    }


    @Override
    public String getType(Uri uri) {

        switch (URI_MATCHER.match(uri)) {
            case ALLROWS:
                return "vnd.android.cursor.dir/vnd.myleakyconlondon.events";
            case SINGLEROW:
                return "vnd.android.cursor.item/vnd.myleakyconlondon.event";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase eventDatabase = eventDatabaseHelper.getWritableDatabase();

        switch (URI_MATCHER.match(uri)) {
            case SINGLEROW:
                final String rowId = uri.getPathSegments().get(1);
                selection = Event.EVENT_ID + " = " + rowId + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
            default:
                break;
        }

        if (selection == null) {
            selection = "1";
        }

        int deleteCount = eventDatabase.delete(Event.TABLE_NAME, selection, selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);

        return deleteCount;
    }

    private static final int ALLROWS = 1;
    private static final int SINGLEROW = 2;
    private static final UriMatcher URI_MATCHER;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, Event.TABLE_NAME, ALLROWS);
        URI_MATCHER.addURI(AUTHORITY, Event.TABLE_NAME + "/#", SINGLEROW);
    }
}