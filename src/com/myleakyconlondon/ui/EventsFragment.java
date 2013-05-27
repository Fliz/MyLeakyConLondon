package com.myleakyconlondon.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;
import com.myleakyconlondon.adapter.EventCursorAdapter;
import com.myleakyconlondon.dao.DataContract;
import com.myleakyconlondon.dao.EventProvider;
import com.myleakyconlondon.model.Event;
import com.myleakyconlondon.model.EventDao;

/**
 * User: Elizabeth Hamlet
 * Date: 15/04/13
 * Time: 15:09
 */
public class EventsFragment extends Fragment implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private OnEventSelectedListener eventSelectedListener;
    private OnAddSelectedListener addSelectedListener;
    private EventCursorAdapter eventCursorAdapter;
    private Activity context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = getActivity();
        getLoaderManager().initLoader(EVENT_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.events, container, false);
        setUpEventsList((ListView) view.findViewById(R.id.events));

        Button add = (Button) view.findViewById(R.id.addEvent);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                addSelectedListener.onAddSelected();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        eventSelectedListener = (OnEventSelectedListener) activity;
        addSelectedListener = (OnAddSelectedListener) activity;
    }

    private void setUpEventsList(ListView eventsList) {

        eventCursorAdapter = new EventCursorAdapter(getActivity(), null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        eventsList.setAdapter(eventCursorAdapter);
        eventsList.setOnItemClickListener(this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {

        CursorLoader cursorLoader = null;

        switch (loaderId) {
            case EVENT_LOADER:
                cursorLoader = new CursorLoader(getActivity(), EventProvider.CONTENT_URI, DataContract.Event.COLUMNS, null, null, null);
                break;
        }

        return cursorLoader;
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
        Log.i("TEST", "I have reached selected but not click yet");
        Toast.makeText(context, "this is my Toast message!!! =) selected", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
         //todo implement
    }

    public interface OnEventSelectedListener {
        public void onEventSelected(Event selectedEvent);
    }

    public interface OnAddSelectedListener {
        public void onAddSelected();
    }

    private static final int EVENT_LOADER = 1;
}
