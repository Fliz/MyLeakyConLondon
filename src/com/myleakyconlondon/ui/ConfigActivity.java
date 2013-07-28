package com.myleakyconlondon.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ToggleButton;
import com.myleakyconlondon.dao.DataContract;
import com.myleakyconlondon.dao.DaysProvider;
import com.myleakyconlondon.dao.EventProvider;
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
public class ConfigActivity extends FragmentActivity implements ConfigFragment.OnDaySelectedListener, DatePickerFragment.OnDateSelectedListener, ConfirmDeleteFragment.NoticeDialogListener  {

    private int dayId = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.config);

        if (savedInstanceState != null) {
            //todo get saved values
        }

        final android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

         ConfigFragment configFragment = new ConfigFragment();
         ft.replace(R.id.config_fragment, configFragment).commit();
    }

    @Override
    public void onDaySelected(Day selectedDay) {

        dayId = selectedDay.getDayNumber();
        DialogFragment dialog = new ConfirmDeleteFragment();
        dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        deleteDay(dayId);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
         //do nothing
    }

    private void deleteDay(int selectedDayNumber) {

        deleteAssociatedEvents(selectedDayNumber);
        getContentResolver().delete(DaysProvider.CONTENT_URI, DataContract.Day.DAY_ID + " = " + selectedDayNumber, null);
        Log.i("Info", "Deleting day " + selectedDayNumber);
    }

    private void deleteAssociatedEvents(int selectedDayNumber) {

        getContentResolver().delete(EventProvider.CONTENT_URI, DataContract.Event.DAY_ID + " = " + selectedDayNumber, null);
        Log.i("Info", "Deleting events for " + selectedDayNumber);
    }

    @Override
    public void onDateSelected(String date, int buttonId) {

        Button dateButton = (Button)findViewById(R.id.addDay);
        dateButton.setText(DateHelper.getFormattedDateFromDateTime(date + " 00:00"));
    }
}
