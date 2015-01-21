package com.example.johan.firebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by johan on 1/20/2015.
 */
public class GroupAdapter extends ArrayAdapter<Group> {
    public GroupAdapter(Context context, ArrayList<Group> groups) {
        super(context, 0, groups);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Group group = GroupFragment.groupNameList.get(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_group, parent, false);
        }

        TextView groupName = (TextView) convertView.findViewById(R.id.txt_group_name);

        // Populate the data into the template view using the data object
        groupName.setText(group.GetName());

        // Return the completed view to render on screen
        return convertView;
    }

}
