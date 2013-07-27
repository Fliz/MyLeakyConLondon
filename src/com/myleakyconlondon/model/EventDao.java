package com.myleakyconlondon.model;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.myleakyconlondon.dao.DataContract;
import com.myleakyconlondon.dao.DaysProvider;
import com.myleakyconlondon.dao.EventProvider;
import com.myleakyconlondon.util.DateHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        event.setStartTime(DateHelper.getDateTimeFromMillis(cursor.getString(3)));
        event.setEndTime(DateHelper.getDateTimeFromMillis(cursor.getString(4)));
        event.setLocation(cursor.getString(5));
        event.setType(cursor.getString(6));
        event.setBackUpEvent(cursor.getString(7).equals("1"));
        event.setBackUpEventId(cursor.getLong(8));
        event.setDayId(cursor.getLong(9));
        event.setDayEndId(cursor.getLong(10));

        return event;
    }

    public static List<Event> cursorToDisplayEvents(Cursor cursor) {

        List events = new ArrayList();

        if(cursor != null && cursor.moveToFirst())     {

            do  {
                Event event = new Event();
                event.setEventId(cursor.getInt(0));
                event.setTitle(cursor.getString(1));
                event.setDayId(cursor.getColumnIndex(DataContract.Event.DAY_ID));

                events.add(event);
            }  while (cursor.moveToNext());
        }
        return  events;
    }

    public static List<Event> getEvents(Context context, long dayId) {

         Cursor events = getBackEventsCursor(context,dayId);
         return cursorToDisplayEvents(events);
    }

    private static Cursor getBackEventsCursor(Context context, long dayId) {

        String[] projection = new String[] {  DataContract.Event.EVENT_ID,
                DataContract.Event.TITLE, DataContract.Event.DAY_ID
        };
        String selectionArgs[] = getArgs(dayId);
        String selection = dayId != 0 ? DataContract.Event.DAY_ID + " = ?" : null;

       return context.getContentResolver().query(EventProvider.CONTENT_URI, projection, selection, selectionArgs, null);
    }

    private static String[] getArgs(long dayId) {

        if(dayId != 0 ) {
            //todo sort
            return new String[] {dayId + ""};
        }
        return null;
    }

}