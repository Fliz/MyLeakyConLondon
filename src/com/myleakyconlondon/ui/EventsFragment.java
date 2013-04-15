package com.myleakyconlondon.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.myleakyconlondon.adapter.EventCursorAdapter;
import com.myleakyconlondon.dao.DataContract;
import com.myleakyconlondon.dao.EventProvider;

/**
 * User: Elizabeth Hamlet
 * Date: 15/04/13
 * Time: 15:09
 */
public class EventsFragment extends Fragment  implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor>{

    EventCursorAdapter eventCursorAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLoaderManager().initLoader(EVENT_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.events, container, false);
        setUpEventsList((ListView) view.findViewById(R.id.events));
        return view;
    }

    private void setUpEventsList(ListView eventsList) {

        eventCursorAdapter = new EventCursorAdapter(getActivity(), null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        eventsList.setAdapter(eventCursorAdapter);
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //todo implement
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //todo implement
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
         //todo implement
    }

    private static final int EVENT_LOADER = 1;
}
