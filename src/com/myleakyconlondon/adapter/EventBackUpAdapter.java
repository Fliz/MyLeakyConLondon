package com.myleakyconlondon.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.myleakyconlondon.model.Day;
import com.myleakyconlondon.model.Event;
import com.myleakyconlondon.ui.R;

import java.util.List;

/**
 * User: Elizabeth
 * Date: 23/07/13
 * Time: 18:18
 */
public class EventBackUpAdapter extends ArrayAdapter<Event> {

    private Activity context;
    List<Event> data = null;

    public EventBackUpAdapter(Activity context, int textViewResourceId, List<Event> data) {

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

        Event item = data.get(position);
        if(item != null)
        {
            TextView title = (TextView) row.findViewById(R.id.drop_down_date);

            if(title != null){
                title.setText(item.getTitle());
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

        Event item = data.get(position);

        if(item != null)
        {

            TextView title = (TextView) row.findViewById(R.id.drop_down_date);

            if(title != null){
                title.setText(item.getTitle());
            }
        }

        return row;
    }
}
