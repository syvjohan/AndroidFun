package com.example.johan.firebase;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;


public class ChatActivity extends ActionBarActivity {

    public String GetGroupID() {
        Bundle b = getIntent().getExtras();
        String groupID;
        if (b != null) {
            groupID = b.getString("groupID");
            return groupID;
        }

        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chat);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.chat_container, new PlaceholderFragment())
                    .commit();
        }

        Firebase.setAndroidContext(this);
        Firebase firebaserootRef = new Firebase("https://luminous-heat-420.firebaseio.com");

        CreateNewChatMessage(firebaserootRef);
        CreateNewChatMessage2(firebaserootRef);
    }

    public void CreateNewChatMessage(Firebase firebaseRootRef) {
        //Message
        Message message = new Message();
        message.msg = "hej på dig min vän!";
        message.from = "dito";
        message.time = "14:44";

        Firebase firebaseParentMsg = firebaseRootRef.child(GetGroupID()).child("messages");
        Firebase firebaseMsg = firebaseParentMsg.push();

        firebaseMsg.child("from").setValue(message.from);
        firebaseMsg.child("message").setValue(message.msg);
        firebaseMsg.child("time").setValue(message.time);
    }

    public void CreateNewChatMessage2(Firebase firebaseRootRef) {
        //Message
        Message message = new Message();
        message.msg = "hej på dig med!!";
        message.from = "nisse";
        message.time = "14:55";

        Firebase firebaseParentMsg = firebaseRootRef.child(GetGroupID()).child("messages");
        Firebase firebaseMsg = firebaseParentMsg.push();

        firebaseMsg.child("from").setValue(message.from);
        firebaseMsg.child("message").setValue(message.msg);
        firebaseMsg.child("time").setValue(message.time);
    }

    public void ReadChatMessages(final Firebase firebaseRootRef) {
        firebaseRootRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String s) {

                String name = (String) snapshot.child("messages").getValue();

                //AddToLstViewGroup(name);
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

    public void AddToLstViewGroup(String name) {
        groupNameList.add(name);

        lstViewGroup = (ListView)findViewById(R.id.listView_group);

        ArrayAdapter<String> groupAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, groupNameList);

        lstViewGroup.setAdapter(groupAdapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
            return rootView;
        }
    }
}
