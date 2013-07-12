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
import com.myleakyconlondon.model.Day;
import com.myleakyconlondon.model.DayHelper;
import com.myleakyconlondon.model.Event;
import com.myleakyconlondon.model.EventDao;
import com.myleakyconlondon.util.DateHelper;

/**
 * User: Elizabeth
 * Date: 23/06/13
 * Time: 17:31
 */
public class ConfigFragment extends Fragment implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private OnDaySelectedListener daySelectedListener;
    private DayCursorAdapter dayCursorAdapter;
    private Button save, add;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(DAY_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.config_fragment, container, false);
        setUpDayList((ListView)view.findViewById(R.id.eventDays));

        save = (Button) view.findViewById(R.id.saveConfig);
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
             //todo delete

        //todo delete selected items
           ContentValues values = getDayValues();
           getActivity().getContentResolver().insert(DaysProvider.CONTENT_URI, values);
    }


    private ContentValues getDayValues() {

        ContentValues values = new ContentValues();

        Button date = (Button) view.findViewById(R.id.addDay);
        EditText dayNumber = (EditText) view.findViewById(R.id.dayNumber);
             //todo validate inc int
        values.put(DataContract.Day.DATE, DateHelper.getDateInMillis(date.getText() + " 00:00"));
        values.put(DataContract.Day.DAY_NUMBER, Integer.parseInt(dayNumber.getText().toString()));
        return values;
    }

    public interface OnDaySelectedListener {
        public void onDaySelected(Day selectedDay);
    }

    private static final int DAY_LOADER = 1;

    private class ReceiverThread extends Thread {
        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    dayCursorAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}

