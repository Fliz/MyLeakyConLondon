package com.myleakyconlondon.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.myleakyconlondon.model.Event;

/**
 * User: Elizabeth Hamlet
 */
public class LeakyConLondonScheduleActivity extends FragmentActivity implements EventsFragment.OnAddSelectedListener, EventsFragment.OnEventSelectedListener, TimePickerFragment.OnTimeSelectedListener, DatePickerFragment.OnDateSelectedListener {

    public boolean isDualPane = false;

    @Override
        public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.events_fragment, new EventsFragment()).commit();
    }

    @Override
    public void onEventSelected(Event selectedEvent) {

        final android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //todo duel pane
        ft.addToBackStack(null);
        ft.replace(R.id.events_fragment, EventDetailFragment.getInstance(selectedEvent)).commit();
    }

    @Override
    public void onAddSelected() {

        final android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //todo duel pane
        ft.addToBackStack(null);
        ft.replace(R.id.events_fragment, EventDetailFragment.getInstance(null)).commit();

    }

    @Override
    public void onTimeSelected(String time, int buttonId) {

        Button timeButton = (Button)findViewById(buttonId);
        timeButton.setText(time);
    }

    @Override
    public void onDateSelected(String date, int buttonId) {

        Button dateButton = (Button)findViewById(buttonId);
        dateButton.setText(date);
    }



}
