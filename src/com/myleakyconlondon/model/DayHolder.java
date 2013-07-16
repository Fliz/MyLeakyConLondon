package com.myleakyconlondon.model;

import android.widget.TextView;

/**
 * User: Elizabeth
 * Date: 15/07/13
 * Time: 19:27
 */
public class DayHolder {

    TextView date;
    int dayId;
    boolean isChecked;

    public TextView getDate() {
        return date;
    }

    public void setDate(TextView date) {
        this.date = date;
    }

    public int getDayId() {
        return dayId;
    }

    public void setDayId(int dayId) {
        this.dayId = dayId;
    }



    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public DayHolder(TextView date) {

        this.date = date;
    }


}
