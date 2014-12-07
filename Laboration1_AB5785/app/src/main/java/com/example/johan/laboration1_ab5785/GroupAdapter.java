package com.example.johan.laboration1_ab5785;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by johan on 2014-12-07.
 */
public class GroupAdapter extends ArrayAdapter<Group> {

    private LayoutInflater mLayoutInflater;

    public GroupAdapter(Context context, ArrayList<Group> objects) {
        super(context, R.layout.fragment_item_grid, objects);

        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.fragment_item_grid, parent, false);

            holder = new ViewHolder();

            holder.name = (TextView) convertView.findViewById(R.id.user_name);
            holder.id = (TextView) convertView.findViewById(R.id.user_id);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(getItem(position).name);
        holder.id.setText(getItem(position).id);

        return convertView;
    }

    private class ViewHolder {
        TextView name;
        TextView id;
    }
}
