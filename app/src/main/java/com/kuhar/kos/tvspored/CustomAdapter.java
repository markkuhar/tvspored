package com.kuhar.kos.tvspored;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mark on 5. 01. 2017.
 */

public class CustomAdapter extends ArrayAdapter<MainItem> {
    private final Context context;
    private final ArrayList<MainItem> itemsArrayList;

    public CustomAdapter(Context context, ArrayList<MainItem> itemsArrayList) {

        super(context, R.layout.listview_row, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.listview_row, parent, false);

        // 3. Get the two text view from the rowView
        TextView labelView = (TextView) rowView.findViewById(R.id.label);
        TextView valueView = (TextView) rowView.findViewById(R.id.value);
        TextView startTimeView = (TextView) rowView.findViewById(R.id.startTime);
        TextView endTimeView = (TextView) rowView.findViewById(R.id.endTime);

        // 4. Set the text for textView
        labelView.setText(itemsArrayList.get(position).getTitle());
        valueView.setText(itemsArrayList.get(position).getDescription());
        startTimeView.setText(itemsArrayList.get(position).getStartTime());
        endTimeView.setText(itemsArrayList.get(position).getEndTime());

        // 5. retrn rowView
        return rowView;
    }
}
