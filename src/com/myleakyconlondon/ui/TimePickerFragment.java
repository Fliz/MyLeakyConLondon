package com.myleakyconlondon.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * User: Elizabeth Hamlet
 * Date: 08/04/13
 * Time: 20:57
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    OnTimeSelectedListener tListener;
    int buttonId;

    public TimePickerFragment(int buttonId) {
         this.buttonId = buttonId;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            tListener = (OnTimeSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnTimeSelectedListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
       //todo set on update
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {

        tListener.onTimeSelected(timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute(), buttonId);
    }

    public interface OnTimeSelectedListener {
        public void onTimeSelected(String time, int buttonId);
    }
}
