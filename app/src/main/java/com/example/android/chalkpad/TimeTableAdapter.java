package com.example.android.chalkpad;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by Ayush on 16-09-2017.
 */

public class TimeTableAdapter extends ArrayAdapter<TimeTable> {
    public TimeTableAdapter(Activity context, ArrayList<TimeTable> object) {
        super(context, 0, object);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.time_table_item, parent, false);
        }
        TimeTable current=getItem(position);
        TextView temp;
        temp=(TextView)listItemView.findViewById(R.id.day);
        temp.setText(current.getDay());
        temp=(TextView)listItemView.findViewById(R.id.lec1);
        temp.setText(current.getLec1());
        temp=(TextView)listItemView.findViewById(R.id.lec2);
        temp.setText(current.getLec2());
        temp=(TextView)listItemView.findViewById(R.id.lec3);
        temp.setText(current.getLec3());
        temp=(TextView)listItemView.findViewById(R.id.lec4);
        temp.setText(current.getLec4());
        temp=(TextView)listItemView.findViewById(R.id.lec5);
        temp.setText(current.getLec5());
        temp=(TextView)listItemView.findViewById(R.id.lec6);
        temp.setText(current.getLec6());
        temp=(TextView)listItemView.findViewById(R.id.lec7);
        temp.setText(current.getLec7());
        temp=(TextView)listItemView.findViewById(R.id.lec8);
        temp.setText(current.getLec8());
        return listItemView;
    }
}
