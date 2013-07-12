package com.myleakyconlondon.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import com.myleakyconlondon.util.DateHelper;

/**
 * User: Elizabeth
 * Date: 15/06/13
 * Time: 10:11
 */
public class EventDetailActivity extends FragmentActivity implements TimePickerFragment.OnTimeSelectedListener, DatePickerFragment.OnDateSelectedListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);

        if (savedInstanceState != null) {
            //todo get saved values
        }
    }

    @Override
    public void onTimeSelected(String time, int buttonId) {

        Button timeButton = (Button)findViewById(buttonId);
        timeButton.setText(time);
    }

    @Override
    public void onDateSelected(String date, int buttonId) {

        Button dateButton = (Button)findViewById(buttonId);
        dateButton.setText(DateHelper.getFormattedDateFromDateTime(date + " 00:00"));
    }
}
