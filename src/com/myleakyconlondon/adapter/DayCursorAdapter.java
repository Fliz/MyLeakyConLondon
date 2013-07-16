package com.myleakyconlondon.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.myleakyconlondon.dao.DataContract;
import com.myleakyconlondon.model.Day;
import com.myleakyconlondon.model.DayHolder;
import com.myleakyconlondon.ui.ConfirmDeleteFragment;
import com.myleakyconlondon.ui.R;
import com.myleakyconlondon.util.DateHelper;

import java.util.ArrayList;
import java.util.Date;

/**
 * User: Elizabeth
 * Date: 23/06/13
 * Time: 17:41
 */
public class DayCursorAdapter extends CursorAdapter {


    public DayCursorAdapter(Context context, Cursor cursor, int flags) {

        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        final LayoutInflater inflater = LayoutInflater.from(context);
        final View row = inflater.inflate(R.layout.day_list_item, parent, false);
        populateRow(row, cursor);

        return row;
    }

    @Override
    public void bindView(View row, Context context, Cursor cursor) {
        populateRow(row, cursor);
    }

    private void populateRow(View row, Cursor cursor) {


        TextView dateView = (TextView) row.findViewById(R.id.day_text);
        Date date = new Date(cursor.getLong(cursor.getColumnIndex(DataContract.Day.DATE)));
        dateView.setText(DateHelper.formatDate(date));
        DayHolder day = new DayHolder(dateView);

        CheckBox box = (CheckBox) row.findViewById(R.id.chkDay);
        int dayId = cursor.getColumnIndex(DataContract.Day.DAY_ID);
        day.setDayId(dayId);
        day.setChecked(box.isChecked());
    }
}
