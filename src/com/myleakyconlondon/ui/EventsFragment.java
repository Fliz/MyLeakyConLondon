package com.myleakyconlondon.ui;

import android.app.Activity;
import android.database.Cursor;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.myleakyconlondon.adapter.EventCursorAdapter;
import com.myleakyconlondon.dao.DataContract;
import com.myleakyconlondon.dao.EventProvider;
import com.myleakyconlondon.model.Event;
import com.myleakyconlondon.model.EventDao;

import java.util.Calendar;

/**
 * User: Elizabeth Hamlet
 * Date: 15/04/13
 * Time: 15:09
 */
public class EventsFragment extends Fragment implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private OnEventSelectedListener eventSelectedListener;
    private EventCursorAdapter eventCursorAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLoaderManager().initLoader(EVENT_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.i("fix", "setting up event list ");
        final View view = inflater.inflate(R.layout.events, container, false);
        setUpEventsList((ListView) view.findViewById(R.id.events));

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        eventSelectedListener = (OnEventSelectedListener) activity;
    }

    private void setUpEventsList(ListView eventsList) {

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

                int dayId = 0;

                Log.i("fix", "day args is " + dayArgs);                   //todo sort of empty wrong day id set....
                if(dayArgs !=  null) {
                    Log.i("fix", " hi " +  dayArgs.getInt("dayId"));
                    dayId = dayArgs.getInt("dayId") ;
                }

                cursorLoader = new CursorLoader(getActivity(), EventProvider.CONTENT_URI, DataContract.Event.COLUMNS, DataContract.Event.DAY_ID  + " = " + dayId, null, DataContract.Event.START_DATE + " ASC");
                break;
        }

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        //todo suspect cursor null
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
         //todo implement
    }

    public interface OnEventSelectedListener {
        public void onEventSelected(Event selectedEvent);
    }

    private static final int EVENT_LOADER = 1;
}
