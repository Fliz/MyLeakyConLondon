package com.myleakyconlondon.model;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.myleakyconlondon.dao.DataContract;
import com.myleakyconlondon.dao.DaysProvider;
import com.myleakyconlondon.util.DateHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: Elizabeth
 * Date: 23/06/13
 * Time: 13:59
 */
public class DayHelper {

    public static List<Integer> getUniqueDays(Context context) {

        Cursor uniqueDays = getDayCursor(context);
        return cursorToUniqueDays(uniqueDays);
    }

    public static List<Day> getDays(Context context) {

        Cursor days = getDayCursor(context);
        return cursorToDays(days);
    }

    private static Cursor getDayCursor(Context context) {

        String[] projection = new String[] {  DataContract.Day.DAY_ID,
                DataContract.Day.DATE, DataContract.Day.DAY_NUMBER
        };

        return context.getContentResolver().query(DaysProvider.CONTENT_URI, projection, null, null, DataContract.Day.DAY_NUMBER + " ASC");
    }

    private static List<Day> cursorToDays(Cursor cursor) {

        List days = new ArrayList();

        if(cursor != null && cursor.moveToFirst())     {

            do  {
                Day day = new Day(cursor.getInt(0), new Date(cursor.getLong(1)));

                days.add(day);
            }  while (cursor.moveToNext());
        }

        return days;
    }

    private static List<Integer> cursorToUniqueDays(Cursor cursor) {

        List uniqueDays = new ArrayList();

        if(cursor != null && cursor.moveToFirst()) {
            do {
                uniqueDays.add(cursor.getInt(2));
            } while (cursor.moveToNext());
        }


        return uniqueDays;
    }

    public static Day cursorToDay(Cursor cursor)  {

        Day day = new Day(cursor.getInt(2), new Date(cursor.getLong(1)));

        return day;
    }


}
