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
public class GroupAdapter extends ArrayList<Group> {

    private static final String FIREBASE_URL ="https://luminous-heat-420.firebaseio.com";
    private Firebase firebaseRef = new Firebase(FIREBASE_URL);

    private ArrayList<Group> arrayList;

    private LayoutInflater mLayoutInflater;


    public GroupAdapter(Context context, ArrayList<Group> arrayList) {
        this.arrayList = arrayList;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        firebaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String s) {

                Map<String, Object> groupMap = new TreeMap<String, Object>();
                groupMap = (TreeMap<String, Object>) snapshot.getValue();
                add((Group) groupMap);

                s = (String)groupMap.get("group äppel");


              /*  for (DataSnapshot child : snapshot.getChildren()) {
                    groupMap.put(child.getKey(), child.getValue());
                }*/


                Iterator iterator = groupMap.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next().toString();
                    String value = groupMap.get(key).toString();

                    System.out.println(key + " " + value);
                }

            }
            @Override
            public void onChildChanged(DataSnapshot snapshot, String s) {

            }
            @Override
            public void onChildRemoved(DataSnapshot snapshot) {

            }
            @Override
            public void onChildMoved(DataSnapshot snapshot, String s) {

            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

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

        holder.name.setText(arrayList.get(position).getName());
        holder.id.setText(arrayList.get(position).getId());

        return convertView;
    }

    private class ViewHolder {
        TextView name;
        TextView id;
    }
}
