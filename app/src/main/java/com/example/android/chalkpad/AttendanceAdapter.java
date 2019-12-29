package com.example.android.chalkpad;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

/**
 * Created by Ayush on 14-09-2017.
 */

public class AttendanceAdapter extends ArrayAdapter<Attendance> {
    public AttendanceAdapter(Activity context, ArrayList<Attendance> object) {
        super(context, 0, object);
    }
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.attendance_item, parent, false);
        }
        Attendance current=getItem(position);
        TextView temp;
        temp=(TextView) listItemView.findViewById(R.id.subject);
        temp.setText(current.getSubject());
        temp=(TextView) listItemView.findViewById(R.id.section);
        temp.setText(current.getSection());
        temp=(TextView) listItemView.findViewById(R.id.teacher);
        temp.setText(current.getTeacher());
        temp=(TextView) listItemView.findViewById(R.id.delivered);
        temp.setText(current.getDelivered());
        temp=(TextView) listItemView.findViewById(R.id.attended);
        temp.setText(current.getAttended());
        TextView percent=(TextView) listItemView.findViewById(R.id.percent);
        percent.setText(current.getPercent());
        if(Float.parseFloat(current.getPercent())<75.00)
            percent.setTextColor(Color.RED);
        return listItemView;
    }
}
