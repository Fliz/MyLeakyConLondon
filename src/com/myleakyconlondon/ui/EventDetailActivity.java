package com.myleakyconlondon.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.myleakyconlondon.dao.DataContract;
import com.myleakyconlondon.dao.EventProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * User: Elizabeth Hamlet
 * Date: 08/04/13
 * Time: 09:18
 */
public class EventDetailActivity extends FragmentActivity implements TimePickerFragment.OnTimeSelectedListener, DatePickerFragment.OnDateSelectedListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail);
        setTypes();
    }

    private void setTypes() {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        Spinner s = (Spinner) findViewById(R.id.detail_types);
        s.setAdapter(adapter);
    }

    public void eventHome(View view) {
        Intent intent = new Intent(this, LeakyConLondonScheduleActivity.class);
        startActivity(intent);
    }

    public void eventSave(View view) {

        SaveEvent();
        Intent intent = new Intent(this, LeakyConLondonScheduleActivity.class);
        startActivity(intent);
    }

    private void SaveEvent() {

        Uri mNewUri;

        ContentValues mNewValues = new ContentValues();
        EditText title = (EditText) findViewById(R.id.detail_name);
        EditText description = (EditText) findViewById(R.id.detail_description);
        EditText location = (EditText) findViewById(R.id.detail_location);
        Spinner type = (Spinner) findViewById(R.id.detail_types);

        mNewValues.put(DataContract.Event.TITLE, title.getText().toString());
        mNewValues.put(DataContract.Event.DESCRIPTION, description.getText().toString());
        mNewValues.put(DataContract.Event.LOCATION, location.getText().toString());
        mNewValues.put(DataContract.Event.TYPE, type.getSelectedItem().toString());
        mNewValues.put(DataContract.Event.IS_BACKUP_EVENT, false);
        mNewValues.put(DataContract.Event.START_DATE, formatDateTime(R.id.set_StartDate, R.id.set_StartTime));
        mNewValues.put(DataContract.Event.END_DATE, formatDateTime(R.id.set_EndDate, R.id.set_EndTime));

        mNewUri = getContentResolver().insert(EventProvider.CONTENT_URI,mNewValues);
    }

    private String formatDateTime(int dateId, int timeId) {

        //todo validate a fix
        Date dateTime = null;
        Button date = (Button)findViewById(dateId);
        Button time = (Button)findViewById(timeId);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
           dateTime = sdf.parse(date.getText() + " " + time.getText());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(dateTime);
    }

    public void showStartDatePicker(View v) {
        setUpDatePicker(R.id.set_StartDate);
    }

    public void showStartTimePicker(View v) {
        DialogFragment newFragment = new TimePickerFragment(R.id.set_StartTime);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showEndDatePicker(View v) {
       setUpDatePicker(R.id.set_EndDate);
    }

    public void showEndTimePicker(View v) {
        DialogFragment newFragment = new TimePickerFragment(R.id.set_EndTime);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    private void setUpDatePicker(int pickerId)  {
        DialogFragment newFragment = new DatePickerFragment(pickerId);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onTimeSelected(String time, int buttonId) {

       Button timeButton = (Button) findViewById(buttonId);
       timeButton.setText(time);
    }

    @Override
    public void onDateSelected(String date, int buttonId) {

        Button dateButton = (Button) findViewById(buttonId);
        dateButton.setText(date);
    }
}
