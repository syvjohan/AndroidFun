package johan.raderas;

import android.app.DownloadManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ChatActivity extends ActionBarActivity {

    private ImageButton btnSend;
    EditText editMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Firebase.setAndroidContext(this);

        String FIREBASE_URL = "https://luminous-heat-420.firebaseio.com/";
        final Firebase firebaseRef = new Firebase(FIREBASE_URL);

        btnSend = (ImageButton)findViewById(R.id.btn_new_msg_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReadData(firebaseRef);
                CreateNewMessage(firebaseRef);
            }
        });
    }

    public void CreateNewMessage(Firebase firebaseRef) {
        ChatMessage cm = new ChatMessage("", "", "");

        editMsg = (EditText)findViewById(R.id.newmsg);

        Firebase userRef = firebaseRef.child("message");
        String from =  cm.getFrom();
        String message = editMsg.getText().toString();
        String time = cm.getTime();
        String id =  firebaseRef.push().getKey();

        cm = new ChatMessage(from, message, time);

        Map<String, Object> chatMessage = new HashMap<>();
        chatMessage.put(id, cm);

        userRef.setValue(chatMessage, new Firebase.CompletionListener() {
                @Override
                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                    if (firebaseError != null) {
                        System.out.println("ChatActivity Data could not be saved. " + firebaseError.getMessage());
                    } else {
                        System.out.println("ChatActivity Data saved successfully.");
                    }
            }
        });
    }

    public void ReadData(final Firebase firebaseRef) {
        firebaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatMessage newChatMessage = new ChatMessage(
                        (String) dataSnapshot.child("from").getValue(),
                        (String) dataSnapshot.child("message").getValue(), (String) dataSnapshot.child("id").getValue());

                Query q = firebaseRef.orderByChild("id");
                dataSnapshot.getValue();

                AddDataToListView(newChatMessage);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String title = (String) dataSnapshot.child("title").getValue();
                //System.out.println("The updated post title is " + title);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String title = (String) dataSnapshot.child("title").getValue();
                //System.out.println("The blog post titled " + title + " has been deleted");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                //System.out.print("The read failed" + firebaseError.getMessage());
            }
        });
    }

    public void AddDataToListView(ChatMessage newChatMessage) {

        ListView view = (ListView) findViewById(R.id.list_item);
        ArrayList<String> arrList = new ArrayList<>();

        String message = newChatMessage.getMessage();
        arrList.add(message);
        System.out.println(message);

        ListAdapter arrAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrList);

        view.setAdapter(arrAdapter);
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
}
