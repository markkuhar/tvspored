package com.kuhar.kos.tvspored;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Mark on 5. 01. 2017.
 */

public class CustomAdapterMain extends ArrayAdapter<MainItem> {
    private final Context context;
    private final ArrayList<MainItem> itemsArrayList;

    public CustomAdapterMain(Context context, ArrayList<MainItem> itemsArrayList) {

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
        View rowView = inflater.inflate(R.layout.listview_row_main, parent, false);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        // 3. Get the two text view from the rowView
        TextView labelView = (TextView) rowView.findViewById(R.id.label);
        TextView valueView = (TextView) rowView.findViewById(R.id.value);
        TextView startTimeView = (TextView) rowView.findViewById(R.id.startTime);
        ProgressBar progressBar = (ProgressBar) rowView.findViewById(R.id.procent);
        //TextView endTimeView = (TextView) rowView.findViewById(R.id.endTime);
        // 4. Set the text for textView



        labelView.setText(itemsArrayList.get(position).getTitle());
        valueView.setText(itemsArrayList.get(position).getDescription());
        startTimeView.setText(itemsArrayList.get(position).getStartTime());

        String startString = "00:00";
        String endString = "00:00";

        try {
            startString = itemsArrayList.get(position).getStartTime();
            endString = itemsArrayList.get(position).getEndTime()
            ;
            if(!startString.equals("N/A") || !endString.equals("N/A")) {
                Date start = sdf.parse(startString);
                Date end = sdf.parse(endString);
                String now = sdf.format(new Date());
                Date nowTime = sdf.parse(now);
                long diff = end.getTime() - start.getTime();

                double perdiff = nowTime.getTime() - start.getTime();
                double per = perdiff / diff * 100;
                progressBar.setProgress((int) per);
            }


        } catch (ParseException e) {
            e.printStackTrace();
            labelView.setText(e.toString());
        }




        //favorites.setChecked(itemsArrayList.get(position).);

        // 5. retrn rowView


        return rowView;
    }
}
