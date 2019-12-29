package com.example.android.chalkpad;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ayush on 15-09-2017.
 */

public class CoursesAdapter extends ArrayAdapter<Course> {
    public CoursesAdapter(Activity context, ArrayList<Course> object) {
        super(context, 0, object);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.courses_item, parent, false);
        }
        Course current=getItem(position);
        TextView temp;
        temp=(TextView) listItemView.findViewById(R.id.code_courses);
        temp.setText(current.getCode());
        temp=(TextView) listItemView.findViewById(R.id.subject_courses);
        temp.setText(current.getName());
        temp=(TextView) listItemView.findViewById(R.id.teacher_courses);
        temp.setText(current.getTeacher());
        temp=(TextView) listItemView.findViewById(R.id.type_courses);
        temp.setText(current.getType());
        return listItemView;
    }
}
