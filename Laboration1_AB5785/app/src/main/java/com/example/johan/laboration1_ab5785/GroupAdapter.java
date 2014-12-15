package com.example.johan.laboration1_ab5785;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.core.utilities.Tree;
import com.example.johan.laboration1_ab5785.Group;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

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
