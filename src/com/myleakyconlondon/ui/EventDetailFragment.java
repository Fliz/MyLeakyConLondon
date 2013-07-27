package com.myleakyconlondon.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.LoginFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.myleakyconlondon.adapter.DateAdapter;
import com.myleakyconlondon.adapter.EventBackUpAdapter;
import com.myleakyconlondon.model.Day;
import com.myleakyconlondon.model.DayHelper;
import com.myleakyconlondon.model.EventDao;
import com.myleakyconlondon.util.DateHelper;
import com.myleakyconlondon.dao.DataContract;
import com.myleakyconlondon.dao.EventProvider;
import com.myleakyconlondon.model.Event;
import com.myleakyconlondon.util.ValidationHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * User: Elizabeth
 * Date: 05/05/13
 * Time: 14:39
 */
public class EventDetailFragment extends Fragment {

    private long eventId = 0;
    private long backUpEventId = 0;
    private TextView title, location, description;
    private Spinner type, startDate, endDate, backUpEvent;
    private Button startTime, endTime;
    private View view;
    private long dayId = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.event_detail, container, false);
        view = rootView;
        setRetainInstance(true);

        title = (TextView) rootView.findViewById(R.id.detail_name);
        location = (TextView) rootView.findViewById(R.id.detail_location);
        description = (TextView) rootView.findViewById(R.id.detail_description);
        type = (Spinner) rootView.findViewById(R.id.detail_types);
        startDate = (Spinner) rootView.findViewById(R.id.set_StartDate);
        endDate = (Spinner) rootView.findViewById(R.id.set_EndDate);
        startTime = (Button) rootView.findViewById(R.id.set_StartTime);
        endTime = (Button) rootView.findViewById(R.id.set_EndTime);
        backUpEvent = (Spinner) rootView.findViewById(R.id.detail_backup);

        setTypes();
        loadDates();

        if (getActivity().getIntent().getExtras() != null) {

            Event event = getActivity().getIntent().getExtras().getParcelable("selectedEvent");
            eventId = event.getEventId();
            dayId = event.getDayId();
            backUpEventId = event.getBackUpEventId();

            loadEvents(dayId);

            title.setText(event.getTitle());
            description.setText(event.getDescription());
            location.setText(event.getLocation());
            ArrayAdapter typeAdapter = (ArrayAdapter) type.getAdapter();
            int spinnerPosition = typeAdapter.getPosition(event.getType());
            type.setSelection(spinnerPosition);
            setDaySelectionById(startDate, dayId);
            setDaySelectionById(endDate, event.getDayEndId());
            startTime.setText(DateHelper.formatTime(event.getStartTime()));
            endTime.setText(DateHelper.formatTime(event.getEndTime()));

            if(event.isBackUpEvent()) {
                CheckBox isBackUp = (CheckBox) rootView.findViewById(R.id.chkBackUp);
                isBackUp.setChecked(true);
                setBackUpSelectionById(backUpEvent);
            }

        }  else {
            //hide delete button
            loadEvents(dayId);
            ToggleButton deleteToggle = (ToggleButton) view.findViewById(R.id.delete_event);
            deleteToggle.setVisibility(View.GONE);
        }

        setUpButtons();


        return rootView;
    }

    private void loadEvents(long dayId) {

        List<Event> events = EventDao.getEvents(getActivity(),dayId);

        EventBackUpAdapter eventAdapter = new EventBackUpAdapter(getActivity(), android.R.layout.simple_spinner_item, events);
        eventAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

       backUpEvent.setAdapter(eventAdapter);
       eventAdapter.notifyDataSetChanged();
    }

    private void loadDates() {

        List<Day> days = DayHelper.getDays(getActivity());

        DateAdapter dateAdapter = new DateAdapter(getActivity(), android.R.layout.simple_spinner_item, days);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        startDate.setAdapter(dateAdapter);
        endDate.setAdapter(dateAdapter);
        dateAdapter.notifyDataSetChanged();

    }

    private void setDaySelectionById(Spinner spinner, long dayId) {

        for (int i = 0; i < spinner.getCount(); i++) {
            Day day = (Day)spinner.getItemAtPosition(i);

            if (day.getDayNumber() == dayId) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void setBackUpSelectionById(Spinner spinner) {

        for (int i = 0; i < spinner.getCount(); i++) {
            Event event = (Event)spinner.getItemAtPosition(i);

            if (event.getEventId() == backUpEventId) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void setTypes() {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner s = (Spinner)view.findViewById(R.id.detail_types);
        s.setAdapter(adapter);
    }

    private void setUpButtons() {

        Button endTime = (Button) view.findViewById(R.id.set_EndTime);
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                showEndTimePicker();
            }
        });

        Button startTime = (Button) view.findViewById(R.id.set_StartTime);
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                showStartTimePicker();
            }
        });

        Button save = (Button) view.findViewById(R.id.details_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                eventSave();
            }
        });
    }

    public void eventSave() {

        ValidationHelper validate = new ValidationHelper();

        ContentValues values = getEventValues(validate);
        String title = values.get(DataContract.Event.TITLE).toString();
        String startDate =  values.get(DataContract.Event.START_DATE).toString();
        String endDate = values.get(DataContract.Event.END_DATE).toString();

        if(validate.validateEvent(title, startDate, endDate)) {

           save(values);

        }  else {
            makeValidationToast(validate);
        }
    }

    private void save(ContentValues values) {

        if(eventId != 0) {
            updateOrDelete(values);
        } else {
            //insert
            getActivity().getContentResolver().insert(EventProvider.CONTENT_URI,values);
        }

        Intent intent = new Intent(getActivity(), LeakyConLondonScheduleActivity.class);
        startActivity(intent);
    }


    private ContentValues getEventValues(ValidationHelper validate) {

        ContentValues values = new ContentValues();

        EditText title = (EditText) view.findViewById(R.id.detail_name);
        EditText description = (EditText) view.findViewById(R.id.detail_description);
        EditText location = (EditText) view.findViewById(R.id.detail_location);
        Spinner type = (Spinner) view.findViewById(R.id.detail_types);
        CheckBox isBackUp = (CheckBox) view.findViewById(R.id.chkBackUp);
        Spinner backUpEvent = (Spinner) view.findViewById(R.id.detail_backup);

        values.put(DataContract.Event.TITLE, title.getText().toString());
        values.put(DataContract.Event.DESCRIPTION, description.getText().toString());
        values.put(DataContract.Event.LOCATION, location.getText().toString());
        values.put(DataContract.Event.TYPE, type.getSelectedItem().toString());
        values.put(DataContract.Event.IS_BACKUP_EVENT, isBackUp.isChecked() ? "1" : "0");

        if(isBackUp.isChecked()) {
          values.put(DataContract.Event.BACKUP_EVENT_ID, ((Event)backUpEvent.getSelectedItem()).getEventId());
        }

        values.put(DataContract.Event.START_DATE, formatDateTime(R.id.set_StartDate, R.id.set_StartTime));
        values.put(DataContract.Event.END_DATE, formatDateTime(R.id.set_EndDate, R.id.set_EndTime));
        values.put(DataContract.Event.DAY_ID, ((Day)startDate.getSelectedItem()).getDayNumber());
        values.put(DataContract.Event.DAY_END_ID, ((Day)endDate.getSelectedItem()).getDayNumber());
        return values;
    }

    private void updateOrDelete(ContentValues values) {

        if(isDeleteAction())  {
            deleteEvent();
        }   else {
            update(values);
        }
    }

    private void update(ContentValues values) {

        getActivity().getContentResolver().update(EventProvider.CONTENT_URI, values, DataContract.Event.EVENT_ID + " = " + eventId, null);
        Log.i("LCLInfo", "Updating event " + eventId);
    }

    private void makeValidationToast(ValidationHelper validate) {

        List<String> messages = validate.getMessages();

        for(String message : messages)  {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private  boolean isDeleteAction() {

        ToggleButton deleteToggle = (ToggleButton) view.findViewById(R.id.delete_event);
        return deleteToggle.isChecked();
    }

    private void deleteEvent() {

        getActivity().getContentResolver().delete(EventProvider.CONTENT_URI, DataContract.Event.EVENT_ID + " = " + eventId, null);
        Log.i("LCLInfo", "Deleting event " + eventId);
    }

    private Long formatDateTime(int dateId, int timeId) {

        Spinner date = (Spinner) view.findViewById(dateId);
        Button time = (Button) view.findViewById(timeId);

        String selectedDate = ((Day)date.getSelectedItem()).getDate();
        boolean isTimeProvided = ValidationHelper.isTimeProvided(time.getText().toString());

        String readyToFormatDate = isTimeProvided ? selectedDate + " " + time.getText().toString() : selectedDate + " 00:00";
        Date formattedDate = DateHelper.getFormattedDate(readyToFormatDate, "dd/MM/yyyy HH:mm");

        Calendar c = Calendar.getInstance();
        c.setTime(formattedDate);

        return c.getTimeInMillis();
    }

    public void showStartTimePicker() {

        DialogFragment newFragment = new TimePickerFragment(R.id.set_StartTime);
        newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
    }

    public void showEndTimePicker() {

        DialogFragment newFragment = new TimePickerFragment(R.id.set_EndTime);
        newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
    }
}
