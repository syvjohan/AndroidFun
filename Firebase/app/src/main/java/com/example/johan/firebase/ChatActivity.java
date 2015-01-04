package com.example.johan.firebase;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import com.firebase.client.Firebase;


public class ChatActivity extends ActionBarActivity {

    class Message {
        String from = "";
        String msg = "";
        String time = "";
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
    }

    public void CreateNewChatMessage(Firebase firebaseRoot) {
        //Message
        Message message = new Message();

        /*Firebase firebaseParentMsg = firebaseGroup.child("messages");
        Firebase firebaseMsg = firebaseParentMsg.push();

        firebaseMsg.child("from").setValue(message.msg);
        firebaseMsg.child("message").setValue(message.from);
        firebaseMsg.child("time").setValue(message.time);*/
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
