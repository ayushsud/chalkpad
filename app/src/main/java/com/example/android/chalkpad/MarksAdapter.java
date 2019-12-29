package com.example.android.chalkpad;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ayush on 29-09-2017.
 */

public class MarksAdapter extends ArrayAdapter<Marks> {
    public MarksAdapter(Activity context, ArrayList<Marks> object) {
        super(context, 0, object);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.attendance_item, parent, false);
        }
        Marks current = getItem(position);
        TextView temp;
        return listItemView;
    }
}
