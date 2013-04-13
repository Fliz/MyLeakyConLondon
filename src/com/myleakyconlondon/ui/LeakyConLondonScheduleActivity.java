package com.myleakyconlondon.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.myleakyconlondon.adapter.EventCursorAdapter;
import com.myleakyconlondon.dao.DataContract;
import com.myleakyconlondon.dao.EventProvider;

/**
 * User: Elizabeth Hamlet
 */
public class LeakyConLondonScheduleActivity extends FragmentActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener{

    EventCursorAdapter eventCursorAdapter;

    @Override
        public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.events);
        setUpEventsList((ListView) findViewById(R.id.events));
    }

    private void setUpEventsList(ListView eventsList) {

        String[] projection =
                {
                        DataContract.Event._ID,
                        DataContract.Event.TITLE,
                        DataContract.Event.DESCRIPTION,
                        DataContract.Event.START_DATE,
                        DataContract.Event.END_DATE
                };


        Cursor mCursor = getContentResolver().query(EventProvider.CONTENT_URI, projection, null, null, null);

        if (null == mCursor) {
            //todo handle error
        } else if (mCursor.getCount() < 1) {
        } else {
            eventCursorAdapter = new EventCursorAdapter(this, mCursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            eventsList.setAdapter(eventCursorAdapter);
            //todo change to use CursorLoader
        }
    }

    public void detailAdd(View view) {
        Intent intent = new Intent(this, EventDetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //Do nothing
    }
}
