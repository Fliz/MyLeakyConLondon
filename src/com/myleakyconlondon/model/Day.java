package com.myleakyconlondon.model;

import com.myleakyconlondon.util.DateHelper;

import java.util.Date;

/**
 * User: Elizabeth
 * Date: 23/06/13
 * Time: 14:09
 */
public class Day {

    private int dayNumber;
    private String date;

    public Day(int dayNumber, Date date) {

        this.dayNumber = dayNumber;
        setDate(date);
    }

    public String getDate() {
        return date;
    }

    public void setDate(Date date) {

        this.date = DateHelper.formatDate(date);
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public int getDayNumber() {

        return dayNumber;
    }
}
