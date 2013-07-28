package com.myleakyconlondon.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
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
import com.myleakyconlondon.model.*;
import com.myleakyconlondon.util.DateHelper;
import com.myleakyconlondon.util.ValidationHelper;

import java.util.List;

/**
 * User: Elizabeth
 * Date: 23/06/13
 * Time: 17:31
 */
public class ConfigFragment extends Fragment implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private OnDaySelectedListener daySelectedListener;
    private DayCursorAdapter dayCursorAdapter;
    private Button save, add;
    private TextView dayNumber;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(DAY_LOADER, null, this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.config_fragment, container, false);
        ListView dayList = (ListView)view.findViewById(R.id.eventDays);

        setUpDayList(dayList);

        save = (Button) view.findViewById(R.id.saveConfig);
        add = (Button) view.findViewById(R.id.addDay);
        dayNumber = (TextView) view.findViewById(R.id.dayNumber);

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

        return view;
    }

    private String getDayNumberDefault() {
        return Integer.toString(dayCursorAdapter.getCount() + 1);
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

        dayList.post(new Runnable() {
            @Override
            public void run() {
                dayNumber.setText(getDayNumberDefault());
            }
        });
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
           Log.i("fix", "I am selected");
        final Day selectedDay = DayHelper.cursorToDay((Cursor) adapterView.getItemAtPosition(position));
        daySelectedListener.onDaySelected(selectedDay);
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

           Intent intent = new Intent(getActivity(), LeakyConLondonScheduleActivity.class);
           startActivity(intent);

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

    private ContentValues getDayValues(String date, String dayNumber) {

        ContentValues values = new ContentValues();

        values.put(DataContract.Day.DATE, DateHelper.getDateInMillis(date + " 00:00"));
        values.put(DataContract.Day.DAY_NUMBER, Integer.parseInt(dayNumber));
        return values;
    }

    public interface OnDaySelectedListener {
        public void onDaySelected(Day selectedDay);
    }

    private static final int DAY_LOADER = 1;


}

