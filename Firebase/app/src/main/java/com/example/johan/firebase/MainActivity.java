package com.example.johan.firebase;

import android.os.DropBoxManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends ActionBarActivity {

    class Group {
        String groupName = "";
        String groupId = "";
    };

    class Message {
        String from = "";
        String msg = "";
        String time = "";
    }

    static ArrayList<String> groupNameList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        Firebase.setAndroidContext(this);
        Firebase firebaserootRef = new Firebase("https://luminous-heat-420.firebaseio.com");

        CreateNewGroup(firebaserootRef);
        CreateNewGroup2(firebaserootRef);
        ReadData(firebaserootRef);
    }

    public void CreateNewGroup(Firebase firebaseRootRef) {
        Group group = new Group();

        Firebase firebaseGroup = firebaseRootRef.child("").push();
        group.groupId = firebaseGroup.getKey();
        group.groupName = "Pelles grupp";
        String empty = "";


        firebaseGroup.child("id").setValue(group.groupId);
        firebaseGroup.child("name").setValue(group.groupName);

        //Message
        Message message = new Message();

        Firebase firebaseParentMsg = firebaseGroup.child("messages");
        Firebase firebaseMsg = firebaseParentMsg.push();

        firebaseMsg.child("from").setValue(message.msg);
        firebaseMsg.child("message").setValue(message.from);
        firebaseMsg.child("time").setValue(message.time);

    }

    public void CreateNewGroup2(Firebase firebaseRootRef) {

        Group group = new Group();

        Firebase firebaseGroup = firebaseRootRef.child("").push();
        group.groupId = firebaseGroup.getKey();
        group.groupName = "Jakobs grupp";
        String empty = "";


        firebaseGroup.child("id").setValue(group.groupId);
        firebaseGroup.child("name").setValue(group.groupName);

        //Message
        Message message = new Message();

        Firebase firebaseParentMsg = firebaseGroup.child("messages");
        Firebase firebaseMsg = firebaseParentMsg.push();

        firebaseMsg.child("from").setValue(message.msg);
        firebaseMsg.child("message").setValue(message.from);
        firebaseMsg.child("time").setValue(message.time);

    }

    public void AddToLstViewGroup(String name) {
        groupNameList.add(name);

        ListView lstViewGroup = (ListView)findViewById(R.id.listView_group);

        ArrayAdapter<String> groupAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, groupNameList);

        lstViewGroup.setAdapter(groupAdapter);
    }

    public void ReadData(final Firebase firebaseRootRef) {
        firebaseRootRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String s) {

                String name = (String) snapshot.child("name").getValue();

                AddToLstViewGroup(name);
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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
