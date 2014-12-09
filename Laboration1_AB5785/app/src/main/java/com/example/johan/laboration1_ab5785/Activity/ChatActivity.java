package com.example.johan.laboration1_ab5785.Activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.johan.laboration1_ab5785.Fragment.AboutFragment;
import com.example.johan.laboration1_ab5785.Fragment.ChatFragment;
import com.example.johan.laboration1_ab5785.Fragment.GroupFragment;
import com.example.johan.laboration1_ab5785.Group;
import com.example.johan.laboration1_ab5785.GroupAdapter;
import com.example.johan.laboration1_ab5785.R;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by johan on 11/24/2014.
 */
public class ChatActivity extends Activity implements
        ChatFragment.OnFragmentInteractionListener,
        GroupFragment.OnFragmentInteractionListener

{
    private static final String FIREBASE_URL ="https://luminous-heat-420.firebaseio.com";
    private Firebase firebaseRef;

    ArrayList<Group> group = new ArrayList<Group>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Firebase.setAndroidContext(this); //Initialize Firebase library.
        firebaseRef = new Firebase(FIREBASE_URL);

        GroupFragment fragment = GroupFragment.newInstance("", "");
        FragmentManager fM = getFragmentManager();
        FragmentTransaction fT = fM.beginTransaction();
        fT.add(R.id.chatcontainer, fragment, null);
        fT.commit();
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    //Save a new contact to firebase.
    public void AddChatGroupBtnClick(View v) {
        //GroupAdapter groupAdapter = new GroupAdapter(getActivity(), group);

        EditText editGroupName = (EditText)findViewById(R.id.txtgroup_name);
        Firebase usersRef = firebaseRef.child(editGroupName.getText().toString());

        group.add(new Group(GetCurrTimeStamp(), editGroupName.getText().toString()));
        usersRef.setValue(group);

        //After adding chat group enter chat!
        ChatFragment fragment = ChatFragment.newInstance("", "");
        FragmentManager fM = getFragmentManager();
        FragmentTransaction fT = fM.beginTransaction();
        fT.replace(R.id.chatcontainer, fragment, null);
        fT.addToBackStack("got to chat");
        fT.commit();

    }

    //Returns the current timestamp this is the id in Group.java
    public String GetCurrTimeStamp() {
        java.util.Date date = new java.util.Date();
        return date.toString();
    }
}


