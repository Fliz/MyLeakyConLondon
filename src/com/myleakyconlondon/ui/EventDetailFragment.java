package com.myleakyconlondon.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.myleakyconlondon.DateHelper;
import com.myleakyconlondon.dao.DataContract;
import com.myleakyconlondon.dao.EventProvider;
import com.myleakyconlondon.model.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: Elizabeth
 * Date: 05/05/13
 * Time: 14:39
 */
public class EventDetailFragment extends Fragment {

    private long eventId;
    private TextView title, location, description;
    private Spinner type;
    private Button startDate, endDate, startTime, endTime;
    private View view;

    public static EventDetailFragment getInstance(Event event) {

        final EventDetailFragment eventDetailFragment = new EventDetailFragment();
        final Bundle args = new Bundle();

        if (event != null) {

            args.putLong(DataContract.Event.EVENT_ID, event.getEventId());
            args.putLong(DataContract.Event.BACKUP_EVENT_ID, event.getBackUpEventId());
            args.putString(DataContract.Event.TITLE, event.getTitle());
            args.putString(DataContract.Event.DESCRIPTION, event.getDescription());
            args.putString(DataContract.Event.LOCATION, event.getLocation());
            args.putString(DataContract.Event.TYPE, event.getType());
            args.putString(DataContract.Event.START_DATE, DateHelper.formatDateTime(event.getStartTime()));
            args.putString(DataContract.Event.END_DATE, DateHelper.formatDateTime(event.getEndTime()));
            args.putBoolean(DataContract.Event.IS_BACKUP_EVENT, event.isBackUpEvent());
            eventDetailFragment.setArguments(args);
        }

        return eventDetailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.event_detail, container, false);
        view = rootView;

        title = (TextView) rootView.findViewById(R.id.detail_name);
        location = (TextView) rootView.findViewById(R.id.detail_location);
        description = (TextView) rootView.findViewById(R.id.detail_description);
        type = (Spinner) rootView.findViewById(R.id.detail_types);
        startDate = (Button) rootView.findViewById(R.id.set_StartDate);
        endDate = (Button) rootView.findViewById(R.id.set_EndDate);
        startTime = (Button) rootView.findViewById(R.id.set_StartTime);
        endTime = (Button) rootView.findViewById(R.id.set_EndTime);

       //todo set up save button to do add or edit/delete
        setTypes();

        if (getArguments() != null) {

            eventId = getArguments().getLong(DataContract.Event.EVENT_ID);
            title.setText(getArguments().getString(DataContract.Event.TITLE));
            description.setText(getArguments().getString(DataContract.Event.DESCRIPTION));
            location.setText(getArguments().getString(DataContract.Event.LOCATION));
            ArrayAdapter typeAdapter = (ArrayAdapter) type.getAdapter();
            int spinnerPosition = typeAdapter.getPosition(getArguments().getString(DataContract.Event.TYPE));
            type.setSelection(spinnerPosition);
            startDate.setText(DateHelper.getFormattedDateFromDateTime(getArguments().getString(DataContract.Event.START_DATE)));
            endDate.setText(DateHelper.getFormattedDateFromDateTime(getArguments().getString(DataContract.Event.END_DATE)));
            startTime.setText(DateHelper.getFormattedTimeFromDateTime(getArguments().getString(DataContract.Event.START_DATE)));
            endTime.setText(DateHelper.getFormattedTimeFromDateTime(getArguments().getString(DataContract.Event.END_DATE)));
        }

        setUpButtons();

        return rootView;
    }

    private void setTypes() {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        Spinner s = (Spinner)view.findViewById(R.id.detail_types);
        s.setAdapter(adapter);
    }

    private void setUpButtons() {

        Button startDate = (Button) view.findViewById(R.id.set_StartDate);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                setUpDatePicker(R.id.set_StartDate);
            }
        });

        Button endDate = (Button) view.findViewById(R.id.set_EndDate);
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                setUpDatePicker(R.id.set_EndDate);
            }
        });

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

        SaveEvent();
        Intent intent = new Intent(getActivity(), LeakyConLondonScheduleActivity.class);
        startActivity(intent);
    }

    private void SaveEvent() {

        Uri mNewUri;

        ContentValues mNewValues = new ContentValues();
        EditText title = (EditText) view.findViewById(R.id.detail_name);
        EditText description = (EditText) view.findViewById(R.id.detail_description);
        EditText location = (EditText) view.findViewById(R.id.detail_location);
        Spinner type = (Spinner) view.findViewById(R.id.detail_types);

        mNewValues.put(DataContract.Event.TITLE, title.getText().toString());
        mNewValues.put(DataContract.Event.DESCRIPTION, description.getText().toString());
        mNewValues.put(DataContract.Event.LOCATION, location.getText().toString());
        mNewValues.put(DataContract.Event.TYPE, type.getSelectedItem().toString());
        mNewValues.put(DataContract.Event.IS_BACKUP_EVENT, false);
        mNewValues.put(DataContract.Event.START_DATE, formatDateTime(R.id.set_StartDate, R.id.set_StartTime));
        mNewValues.put(DataContract.Event.END_DATE, formatDateTime(R.id.set_EndDate, R.id.set_EndTime));

        mNewUri =  getActivity().getContentResolver().insert(EventProvider.CONTENT_URI,mNewValues);
    }

    private String formatDateTime(int dateId, int timeId) {

        //todo validate a fix
        Button date = (Button) view.findViewById(dateId);
        Button time = (Button) view.findViewById(timeId);

        return DateHelper.GetFormattedDateTime(date.getText().toString(), time.getText().toString());
    }

    public void showStartTimePicker() {

        DialogFragment newFragment = new TimePickerFragment(R.id.set_StartTime);
        newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
    }

    public void showEndTimePicker() {

        DialogFragment newFragment = new TimePickerFragment(R.id.set_EndTime);
        newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
    }

    private void setUpDatePicker(int pickerId)  {

        DialogFragment newFragment = new DatePickerFragment(pickerId);
        Log.i("test", "show date..  ");
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }





}
