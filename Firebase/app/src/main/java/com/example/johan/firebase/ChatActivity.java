package com.example.johan.firebase;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.Map;


public class ChatActivity extends ActionBarActivity {
    static ArrayList<Map<String, Message>> chatMsgList = new ArrayList<>();
    ListView lstViewChat;
    CharSequence username = "Mig";

    Button btnSend;

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
        final Firebase firebaserootRef = new Firebase("https://luminous-heat-420.firebaseio.com");

        ReadChatMessages(firebaserootRef);

        final EditText editMsg = (EditText) findViewById(R.id.txt_message_input);
        btnSend = (Button) findViewById(R.id.btnSend);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textMsg = editMsg.getText().toString();
                CreateNewMessage(firebaserootRef, textMsg);
                }
            };

        //CreateNewChatMessage2(firebaserootRef);
    }

    public void  CreateNewMessage(Firebase firebaserootRef, String textMsg) {
        String msg = textMsg;
        String from = username.toString();
        String time = "14:44";
        String id = "";
        //Message
        Message message = new Message(from, msg, time);
        if (GetGroupID() != "" || GetGroupID() != null) {
            Firebase firebaseParentMsg = firebaserootRef.child(GetGroupID()).child("messages");
            Firebase firebaseMsg = firebaseParentMsg.push();

            id = firebaseParentMsg.getKey();
            firebaseMsg.child("from").setValue(message.from);
            firebaseMsg.child("message").setValue(message.message);
            firebaseMsg.child("time").setValue(message.time);
        }
    }

    public void CreateNewChatMessage2(Firebase firebaseRootRef) {
        String msg = "hej på dig med!!";
        String from = "nisse";
        String time = "14:55";
        String id = "";
        //Message
        Message message = new Message(from, msg, time);

        Firebase firebaseParentMsg = firebaseRootRef.child(GetGroupID()).child("messages");
        Firebase firebaseMsg = firebaseParentMsg.push();

        id = firebaseParentMsg.getKey();
        firebaseMsg.child("from").setValue(message.from);
        firebaseMsg.child("message").setValue(message.message);
        firebaseMsg.child("time").setValue(message.time);
    }

    public void ReadChatMessages(final Firebase firebaseRootRef) {
        firebaseRootRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String s) {
                if (snapshot.child("messages").getValue() != null) {
                    Map<String, Message> newMessage = (Map<String, Message>) snapshot.child("messages").getChildren().iterator().next().getValue();

                    AddToLstViewGroup(newMessage);
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
    }

    public void AddToLstViewGroup(Map<String, Message> newMessage) {

        chatMsgList.add(newMessage);

        ChatAdapter chatAdapter = new ChatAdapter(this, chatMsgList);

        if(chatAdapter.IsMsgFromMe(newMessage)) {
            lstViewChat = (ListView)findViewById(R.id.listView_chat_message_me);
        } else {
            lstViewChat = (ListView)findViewById(R.id.listView_chat_message_others);
        }

        lstViewChat.setAdapter(chatAdapter);

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

    public class ChatAdapter extends ArrayAdapter<Map<String, Message>> {
        public ChatAdapter(Context context, ArrayList<Map<String, Message>> messages) {
            super(context, 0, messages);
        }

        public boolean IsMsgFromMe(Map<String, Message> message) {
            boolean isSenderMe = username.equals((CharSequence) message.get("from"));
            return isSenderMe;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Map<String, Message> message = getItem(position);

            //Check who has sent the message me or someone else...
            if (IsMsgFromMe(message)) {
                System.out.println("I did send the message");
                // Check if an existing view is being reused, otherwise inflate the view
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_chat_me, parent, false);
                }

                TextView chatFrom = (TextView) convertView.findViewById(R.id.txt_message_me_from);
                TextView chatMessage = (TextView) convertView.findViewById(R.id.txt_message_me_message);
                TextView chatTime = (TextView) convertView.findViewById(R.id.txt_message_me_time);

                // Populate the data into the template view using the data object
                chatFrom.setText("From: " + (CharSequence) message.get("from"));
                chatMessage.setText((CharSequence) message.get("message"));
                chatTime.setText("Time: " + (CharSequence) message.get("time"));

            } else {
                System.out.println("Someone else send the message");
                // Check if an existing view is being reused, otherwise inflate the view
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_chat_others, parent, false);
                }

                TextView chatFrom = (TextView) convertView.findViewById(R.id.txt_message_others_from);
                TextView chatMessage = (TextView) convertView.findViewById(R.id.txt_message_others_message);
                TextView chatTime = (TextView) convertView.findViewById(R.id.txt_message_others_time);

                // Populate the data into the template view using the data object
                chatFrom.setText("From: " + (CharSequence) message.get("from"));
                chatMessage.setText((CharSequence) message.get("message"));
                chatTime.setText("Time: " + (CharSequence) message.get("time"));
            }

            // Return the completed view to render on screen
            return convertView;
        }
    }

}
