package com.myleakyconlondon.ui;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
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
import com.myleakyconlondon.adapter.EventCursorAdapter;
import com.myleakyconlondon.dao.DataContract;
import com.myleakyconlondon.dao.EventProvider;
import com.myleakyconlondon.model.Event;
import com.myleakyconlondon.model.EventDao;

import java.util.Calendar;
import java.util.Date;

/**
 * User: Elizabeth Hamlet
 * Date: 15/04/13
 * Time: 15:09
 */
public class EventsFragment extends Fragment implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private OnEventSelectedListener eventSelectedListener;
    private EventCursorAdapter eventCursorAdapter;
    private EventsFragment currentFragment;
    private ListView list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLoaderManager().initLoader(EVENT_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.events, container, false);
        setUpEventsList((ListView) view.findViewById(R.id.events));

        CheckBox backupFilter = (CheckBox)view.findViewById(R.id.chkBackFilter);
        currentFragment = this;

        backupFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Bundle bundle = new Bundle();
                setFilterBundle(isChecked, bundle, "isFilter");
                CheckBox pastFilter = (CheckBox)view.findViewById(R.id.chkPastFilter);
                setFilterBundle(pastFilter.isChecked(), bundle, "isDayFilter");

                getLoaderManager().restartLoader(EVENT_LOADER, bundle, currentFragment);
            }
        });

        CheckBox dateFilter = (CheckBox)view.findViewById(R.id.chkPastFilter);

        dateFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Bundle bundle = new Bundle();
                setFilterBundle(isChecked, bundle, "isDayFilter");
                CheckBox backupFilter = (CheckBox)view.findViewById(R.id.chkBackFilter);
                setFilterBundle(backupFilter.isChecked(), bundle, "isFilter");

                getLoaderManager().restartLoader(EVENT_LOADER, bundle, currentFragment);
            }
        });

        return view;
    }

    private Bundle setFilterBundle(boolean isChecked, Bundle bundle, String bundleName) {

        if(isChecked) {
            bundle.putString(bundleName, "1");
        }  else {
            bundle.putString(bundleName, "0");
        }
        return bundle;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        eventSelectedListener = (OnEventSelectedListener) activity;
    }

    private void setUpEventsList(ListView eventsList) {

        list = eventsList;
        eventCursorAdapter = new EventCursorAdapter(getActivity(), null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        eventsList.setAdapter(eventCursorAdapter);
        eventsList.setOnItemClickListener(this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {

        Bundle dayArgs = getArguments();

        CursorLoader cursorLoader = null;

        switch (loaderId) {
            case EVENT_LOADER:

               cursorLoader = populateListView(dayArgs, bundle);
                break;
        }

        return cursorLoader;
    }

    private CursorLoader populateListView(Bundle dayArgs, Bundle filterArgs) {

        int dayId = 0;
        String dayFilter = "AND " + DataContract.Event.END_DATE + " >= ?";
        String backUpFilter = "0";

        if(dayArgs !=  null  && dayArgs.containsKey("dayId")) {
            dayId = dayArgs.getInt("dayId") ;
        }

        if (filterArgs != null && filterArgs.containsKey("isFilter")) {
            backUpFilter = filterArgs.getString("isFilter");
        }

        if(filterArgs != null && filterArgs.containsKey("isDayFilter")) {
            String filter = filterArgs.getString("isDayFilter");
            if(filter.equals("1")) {
                dayFilter = " AND " + DataContract.Event.END_DATE + " < ?" ;
            }
        }

        String selection = DataContract.Event.DAY_ID + " = ? AND " + DataContract.Event.IS_BACKUP_EVENT + " = ? " + dayFilter;
        String selectionArgs[] = new String[] {dayId + "", backUpFilter, new Date().getTime() + ""};

        return new CursorLoader(getActivity(), EventProvider.CONTENT_URI, DataContract.Event.COLUMNS, selection, selectionArgs, DataContract.Event.START_DATE + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

        switch (cursorLoader.getId()) {
            case EVENT_LOADER:
                eventCursorAdapter.changeCursor(cursor);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

        switch (cursorLoader.getId()) {
            case EVENT_LOADER:
                eventCursorAdapter.changeCursor(null);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        final Event selectedEvent = EventDao.cursorToEvent((Cursor) adapterView.getItemAtPosition(position));
        eventSelectedListener.onEventSelected(selectedEvent);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            //do nothing
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public interface OnEventSelectedListener {
        public void onEventSelected(Event selectedEvent);
    }

    private static final int EVENT_LOADER = 1;
}
