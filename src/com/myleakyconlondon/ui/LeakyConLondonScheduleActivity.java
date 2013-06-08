package com.myleakyconlondon.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.myleakyconlondon.DateHelper;
import com.myleakyconlondon.dao.DataContract;
import com.myleakyconlondon.model.Event;
import com.myleakyconlondon.model.EventDao;

/**
 * User: Elizabeth Hamlet
 */
public class LeakyConLondonScheduleActivity extends FragmentActivity implements EventsFragment.OnAddSelectedListener, EventsFragment.OnEventSelectedListener, TimePickerFragment.OnTimeSelectedListener, DatePickerFragment.OnDateSelectedListener {

    public boolean isDualPane = false;

    @Override
        public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (savedInstanceState == null) {

            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.events_fragment, new EventsFragment()).commit();
        } else {
            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.events_fragment, EventDetailFragment.getInstance(null)).commit();
            EventDetailFragment event_detail_fragment = (EventDetailFragment) getSupportFragmentManager().findFragmentByTag("event_detail_fragment");
            //todo fix
        }

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
        dateButton.setText(DateHelper.getFormattedDateFromDateTime(date + " 00:00"));
    }



}
