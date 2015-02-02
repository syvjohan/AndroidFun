package com.example.johan.firebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by johan on 1/20/2015.
 */
public class GroupAdapter extends ArrayAdapter<Group> {

    private ArrayList<Group> lstGroups;

    public GroupAdapter(Context context, ArrayList<Group> groups) {
        super(context, 0, groups);
        this.lstGroups = groups;
    }

    @Override
    public Group getItem(int position) {
        return lstGroups.get(position);
    }

    @Override
    public int getCount() {
        return lstGroups.size();
    }

    //Number of layouts
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolderGr holder = null;

        if (convertView == null) {
            holder = new ViewHolderGr();

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_group, parent, false);
            holder.groupName = (TextView) convertView.findViewById(R.id.txt_group_name);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolderGr) convertView.getTag();
        }

        Group group = lstGroups.get(position);

        holder.groupName.setText(group.GetName());

        return convertView;

       /* // Get the data item for this position
        Group group = GroupFragment.groupList.get(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_group, parent, false);
        }

        TextView groupName = (TextView) convertView.findViewById(R.id.txt_group_name);

        // Populate the data into the template view using the data object
        groupName.setText(group.GetName());

        // Return the completed view to render on screen
        return convertView;*/
    }

    public static class ViewHolderGr {
        public TextView groupName;

    }

}
