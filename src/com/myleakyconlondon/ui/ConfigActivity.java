package com.myleakyconlondon.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;
import com.myleakyconlondon.model.Day;
import com.myleakyconlondon.model.DayHelper;
import com.myleakyconlondon.model.Event;
import com.myleakyconlondon.util.DateHelper;

import java.util.List;

/**
 * User: Elizabeth
 * Date: 23/06/13
 * Time: 17:30
 */
public class ConfigActivity extends FragmentActivity implements ConfigFragment.OnDaySelectedListener, DatePickerFragment.OnDateSelectedListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.config);

        if (savedInstanceState != null) {
            //todo get saved values
        }

        final android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //todo duel pane
        // ft.addToBackStack(null);

         ConfigFragment configFragment = new ConfigFragment();
         ft.replace(R.id.config_fragment, configFragment).commit();
    }

    @Override
    public void onDaySelected(Day selectedDay) {

        //todo on day selected
        Log.i("fix", "HOLY GRAIL");
    }

    @Override
    public void onDateSelected(String date, int buttonId) {

        Button dateButton = (Button)findViewById(R.id.addDay);
        dateButton.setText(DateHelper.getFormattedDateFromDateTime(date + " 00:00"));
    }
}
