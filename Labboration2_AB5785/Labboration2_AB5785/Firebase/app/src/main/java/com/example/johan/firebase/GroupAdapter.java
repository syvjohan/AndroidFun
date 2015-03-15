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
        convertView = null; //Make it possible to scroll without loading data over an already existing view.

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
        notifyDataSetChanged();

        return convertView;
    }

    public static class ViewHolderGr {
        public TextView groupName;

    }

}
