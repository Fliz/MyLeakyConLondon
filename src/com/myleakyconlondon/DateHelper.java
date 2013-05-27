package com.myleakyconlondon;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * User: Elizabeth
 * Date: 26/05/13
 * Time: 16:54
 */
public class DateHelper {

    public static String GetFormattedDateTime(String date, String time)    {

        Log.i("test", "datetime");
        Date dateTime = getFormattedDate(date + " " + time);
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(dateTime);
    }

    public static String getFormattedDateFromDateTime(String dateTime)    {

        Date date = getFormattedDate(dateTime);
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

    public static String getFormattedTimeFromDateTime(String dateTime)    {

        Date date = getFormattedDate(dateTime);
        return new SimpleDateFormat("HH:mm").format(date);
    }

    public static String formatDateTime(Date dateTime)    {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(dateTime);
    }

    public static Date getFormattedDate(String date) {

        Date dateTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
        try {
            dateTime = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateTime;
    }
}
