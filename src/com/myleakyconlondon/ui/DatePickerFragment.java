package com.myleakyconlondon.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.*;

import java.util.Calendar;

/**
 * User: Elizabeth Hamlet
 * Date: 08/04/13
 * Time: 20:53
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    OnDateSelectedListener dListener;
    int buttonId;

    public DatePickerFragment(int buttonId) {
            this.buttonId = buttonId;
    }

    @Override
    public void onAttach(Activity activity) {
        Log.i("test", "activity " + activity);
        super.onAttach(activity);
        try {
            dListener = (OnDateSelectedListener) activity;

        } catch (ClassCastException e) {
            Log.i("test", "exception " + e);
            throw new ClassCastException(activity.toString() + " must implement OnDateSelectedListener");
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //todo get saved value
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        //todo use string builder and display in suitable format for changing
        dListener.onDateSelected(datePicker.getDayOfMonth() + "/" + datePicker.getMonth() + "/" + datePicker.getYear(), buttonId);
    }

    public interface OnDateSelectedListener {
        public void onDateSelected(String date, int buttonId);
    }
}
