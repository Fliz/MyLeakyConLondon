package com.myleakyconlondon.model;

import android.database.Cursor;
import android.util.Log;
import com.myleakyconlondon.DateHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * User: Elizabeth
 * Date: 18/05/13
 * Time: 11:50
 */
public class EventDao {


    public static Event cursorToEvent(Cursor cursor) {

        Event event = new Event();

        event.setEventId(cursor.getLong(0));
        event.setTitle(cursor.getString(1));
        event.setDescription(cursor.getString(2));
        event.setStartTime(DateHelper.getFormattedDate(cursor.getString(3)));
        event.setEndTime(DateHelper.getFormattedDate(cursor.getString(4)));
        event.setLocation(cursor.getString(5));
        event.setType(cursor.getString(6));
        event.setBackUpEvent(Boolean.getBoolean(cursor.getString(7)));
        event.setBackUpEventId(cursor.getLong(8));

        return event;
    }
}
