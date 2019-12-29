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
 * Created by Ayush on 16-09-2017.
 */

public class ResourceAdapter extends ArrayAdapter<Resource> {
    public ResourceAdapter(Activity context, ArrayList<Resource> object) {
        super(context, 0, object);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.resource_item, parent, false);
        }
        Resource current=getItem(position);
        TextView temp;
        temp=(TextView)listItemView.findViewById(R.id.name_resource);
        temp.setText(current.getName());
        temp=(TextView)listItemView.findViewById(R.id.teacher_resource);
        temp.setText(current.getTeacher());
        temp=(TextView)listItemView.findViewById(R.id.date_resource);
        temp.setText(current.getDate());
        return listItemView;
    }
}
