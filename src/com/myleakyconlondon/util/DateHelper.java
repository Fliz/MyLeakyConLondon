package com.myleakyconlondon.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * User: Elizabeth
 * Date: 26/05/13
 * Time: 16:54
 */
public class DateHelper {

    public static String GetFormattedDateTime(String date, String time)    {

        Date dateTime = getFormattedDate(date + " " + time, "dd/MM/yyyy HH:mm");
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(dateTime);
    }

    public static String getFormattedDateFromDateTime(String dateTime)    {

        Date date = getFormattedDate(dateTime, "dd/MM/yyyy HH:mm");
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

    public static String getFormattedTimeFromDateTime(String dateTime)    {

        Date date = getFormattedDate(dateTime, "dd/MM/yyyy HH:mm");
        return new SimpleDateFormat("HH:mm").format(date);
    }

    public static String formatDateTime(Date dateTime)    {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(dateTime);
    }

    public static String formatDate(Date dateTime)    {
        return new SimpleDateFormat("dd/MM/yyyy").format(dateTime);
    }

    public static String formatTime(Date dateTime)    {
        return new SimpleDateFormat("HH:mm").format(dateTime);
    }

    public static Date getFormattedDate(String date, String format) {

        Date dateTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            dateTime = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateTime;
    }

    public static Date getDateTimeFromMillis(String millis)    {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(millis));

        return cal.getTime();
    }

    public static Long getDateInMillis(String dateTime) {

        Date formattedDate = DateHelper.getFormattedDate(dateTime, "dd/MM/yyyy HH:mm");
        Calendar c = Calendar.getInstance();
        c.setTime(formattedDate);

        return c.getTimeInMillis();
    }
}
