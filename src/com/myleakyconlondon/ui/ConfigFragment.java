package com.myleakyconlondon.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.myleakyconlondon.adapter.DayCursorAdapter;
import com.myleakyconlondon.dao.DataContract;
import com.myleakyconlondon.dao.DaysProvider;
import com.myleakyconlondon.dao.EventProvider;
import com.myleakyconlondon.model.*;
import com.myleakyconlondon.util.DateHelper;
import com.myleakyconlondon.util.ValidationHelper;

import java.util.List;

/**
 * User: Elizabeth
 * Date: 23/06/13
 * Time: 17:31
 */
public class ConfigFragment extends Fragment implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor>, ConfirmDeleteFragment.NoticeDialogListener {

    private OnDaySelectedListener daySelectedListener;
    private DayCursorAdapter dayCursorAdapter;
    private Button save, add, delete;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(DAY_LOADER, null, this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Button date = (Button) view.findViewById(R.id.addDay);
        EditText dayNumber = (EditText) view.findViewById(R.id.dayNumber);

        outState.putString("dayNumber", dayNumber.getText().toString());

        if(!date.getText().toString().trim().equals("Add New Day"))  {
            outState.putCharSequence("date", date.getText());
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null) {

            Button date = (Button) view.findViewById(R.id.addDay);
            EditText dayNumber = (EditText) view.findViewById(R.id.dayNumber);

            if(savedInstanceState.containsKey("date")) {
                date.setText(savedInstanceState.getCharSequence("date"));
                Log.i("fix", "date " + date.getText().toString());
                Log.i("fix", " day "  + dayNumber.getText().toString());
            }
            dayNumber.setText(savedInstanceState.getString("dayNumber"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.config_fragment, container, false);
        ListView dayList = (ListView)view.findViewById(R.id.eventDays);

        setUpDayList(dayList);

        save = (Button) view.findViewById(R.id.saveConfig);
        delete = (Button) view.findViewById(R.id.deleteDays);
        add = (Button) view.findViewById(R.id.addDay);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                DialogFragment newFragment = new DatePickerFragment(R.id.addDay, null);
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                saveDate();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                delete();
            }
        });

       //todo set on click for day list

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        daySelectedListener = (OnDaySelectedListener) activity;
    }

    private void setUpDayList(ListView dayList) {

        dayCursorAdapter = new DayCursorAdapter(getActivity(), null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        dayList.setAdapter(dayCursorAdapter);

        dayList.setOnItemClickListener(this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {

        CursorLoader cursorLoader = null;

        switch (loaderId) {
            case DAY_LOADER:
                cursorLoader = new CursorLoader(getActivity(), DaysProvider.CONTENT_URI, DataContract.Day.COLUMNS, null, null, DataContract.Day.DAY_NUMBER + " ASC");
                break;
        }

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

        switch (cursorLoader.getId()) {
            case DAY_LOADER:
                dayCursorAdapter.changeCursor(cursor);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

        switch (cursorLoader.getId()) {
            case DAY_LOADER:
                dayCursorAdapter.changeCursor(null);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        //todo dialog. followed by delete action
        //todo checkbox checked
        final Day selectedDay = DayHelper.cursorToDay((Cursor) adapterView.getItemAtPosition(position));
        daySelectedListener.onDaySelected(selectedDay);

       // DialogFragment dialog = new ConfirmDeleteFragment();
       // dialog.show(getActivity().getSupportFragmentManager(), "NoticeDialogFragment");
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //do nothing
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //do nothing
    }

    private void saveDate() {

        Button date = (Button) view.findViewById(R.id.addDay);
        EditText dayNumber = (EditText) view.findViewById(R.id.dayNumber);

        ValidationHelper validation = new ValidationHelper();

        if(validation.validateDay(date.getText().toString(), dayNumber.getText().toString())) {

           ContentValues values = getDayValues(date.getText().toString(), dayNumber.getText().toString());
           getActivity().getContentResolver().insert(DaysProvider.CONTENT_URI, values);
           clearForm(date, dayNumber);

        } else {
            List<String> messages = validation.getMessages();

            for(String message : messages)  {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void clearForm(Button date, EditText dayNumber) {

         date.setText("Add new Day");
         dayNumber.setText("");
    }

    private void delete() {

        //todo delete
        Log.i("fix", " hmm");

        for(int i = 0; i < dayCursorAdapter.getCount(); i++)  {
            Day day = DayHelper.cursorToDay((Cursor)dayCursorAdapter.getItem(i));

            //todo delete on click
            Log.i("fix", " row " + day.getDayNumber());


         // Log.i("fix", " the tag " + day.isChecked() + " " + day.getDayId());
        }
    }

    private ContentValues getDayValues(String date, String dayNumber) {

        //todo validate is int
        ContentValues values = new ContentValues();

        values.put(DataContract.Day.DATE, DateHelper.getDateInMillis(date + " 00:00"));
        values.put(DataContract.Day.DAY_NUMBER, Integer.parseInt(dayNumber));
        return values;
    }


    private void deleteDay() {

        int selectedDayNumber= 0;
        deleteAssociatedEvents(selectedDayNumber);

        //todo set day number
        getActivity().getContentResolver().delete(DaysProvider.CONTENT_URI, DataContract.Day.DAY_ID + " = " + selectedDayNumber, null);
        Log.i("Info", "Deleting day " + selectedDayNumber);
    }

    private void deleteAssociatedEvents(int selectedDayNumber) {

        getActivity().getContentResolver().delete(EventProvider.CONTENT_URI, DataContract.Event.DAY_ID + " = " + selectedDayNumber, null);
        Log.i("Info", "Deleting events for " + selectedDayNumber);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        Log.i("Info", "confirm delete");

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
        Log.i("Info", "cance; delete");
    }


    public interface OnDaySelectedListener {
        public void onDaySelected(Day selectedDay);
    }

    private static final int DAY_LOADER = 1;


}

