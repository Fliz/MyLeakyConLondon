package com.myleakyconlondon.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.myleakyconlondon.dao.DataContract;
import com.myleakyconlondon.dao.EventSQLiteHelper;
import com.myleakyconlondon.ui.R;

/**
 * User: Elizabeth Hamlet
 */

public class EventCursorAdapter extends CursorAdapter {

    public EventCursorAdapter(Context context, Cursor cursor, int flags) {

        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        final LayoutInflater inflater = LayoutInflater.from(context);
        final View row = inflater.inflate(R.layout.event_list_item, parent, false);

        populateRow(row, cursor);

        return row;
    }

    @Override
    public void bindView(View row, Context context, Cursor cursor) {
        populateRow(row, cursor);
    }

    private void populateRow(View row, Cursor cursor) {

        final EventHolder eventHolder = new EventHolder();
        eventHolder.title = (TextView) row.findViewById(R.id.eventTitle);
        eventHolder.description = (TextView) row.findViewById(R.id.eventDescription);
        eventHolder.startDate = (TextView) row.findViewById(R.id.eventStartDate);
        eventHolder.endDate = (TextView) row.findViewById(R.id.eventEndDate);

        eventHolder.title.setText(cursor.getString(cursor.getColumnIndex(DataContract.Event.TITLE)));
        eventHolder.description.setText(cursor.getString(cursor.getColumnIndex(DataContract.Event.DESCRIPTION)));
        eventHolder.startDate.setText(cursor.getString(cursor.getColumnIndex(DataContract.Event.START_DATE)));
        eventHolder.endDate.setText(cursor.getString(cursor.getColumnIndex(DataContract.Event.END_DATE)));
    }

    static class EventHolder {
        TextView title, description, startDate, endDate;
    }
}
