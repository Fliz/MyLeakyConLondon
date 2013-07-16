package com.myleakyconlondon.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.myleakyconlondon.model.Day;
import com.myleakyconlondon.ui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Elizabeth
 * Date: 30/06/13
 * Time: 18:29
 */
public class DateAdapter extends ArrayAdapter<Day> {

    private Activity context;
    List<Day> data = null;

    public DateAdapter(Activity context, int textViewResourceId, List<Day> data) {

        super(context, textViewResourceId, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        if(row == null)
        {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.date_drop_down, parent, false);
        }

        Day item = data.get(position);
        if(item != null)
        {

            TextView date = (TextView) row.findViewById(R.id.drop_down_date);

            if(date != null){
                date.setText(item.getDate());
            }
        }

        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {   // This view starts when we click the spinner.
        View row = convertView;
        if(row == null)
        {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.date_drop_down, parent, false);
        }

        Day item = data.get(position);

        if(item != null)
        {

            TextView date = (TextView) row.findViewById(R.id.drop_down_date);

            if(date != null){
                date.setText(item.getDate());
            }
        }

        return row;
    }


}
