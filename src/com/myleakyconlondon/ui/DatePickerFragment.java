package com.myleakyconlondon.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * User: Elizabeth Hamlet
 * Date: 08/04/13
 * Time: 20:53
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    OnDateSelectedListener dListener;
    int buttonId;
    Date updateDate;

    public DatePickerFragment(int buttonId, Date updateDate) {

        this.buttonId = buttonId;
        this.updateDate = updateDate;
    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        try {
            dListener = (OnDateSelectedListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnDateSelectedListener");
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //todo get saved value
        final Calendar c = Calendar.getInstance();

        if(updateDate != null) {
            c.setTime(updateDate);
        }
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        //todo use string builder and display in suitable format for changing
        int month =  datePicker.getMonth() + 1;
        dListener.onDateSelected(datePicker.getDayOfMonth() + "/" + month + "/" + datePicker.getYear(), buttonId);
    }

    public interface OnDateSelectedListener {
        public void onDateSelected(String date, int buttonId);
    }
}
