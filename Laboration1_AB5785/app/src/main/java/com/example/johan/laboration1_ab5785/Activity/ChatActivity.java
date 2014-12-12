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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        GroupFragment fragment = GroupFragment.newInstance("", "");
        FragmentManager fM = getFragmentManager();
        FragmentTransaction fT = fM.beginTransaction();
        fT.replace(R.id.chatcontainer, fragment, null);
        fT.addToBackStack("go to group fragmement");
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
}


