package johan.raderas;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ChatActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Firebase.setAndroidContext(this);

        String FIREBASE_URL = "https://luminous-heat-420.firebaseio.com/";
        Firebase firebaseRef = new Firebase(FIREBASE_URL);

        ReadData(firebaseRef);
        CreateNewMessage(firebaseRef);
    }

    public void CreateNewMessage(Firebase firebaseRef) {
        String from = "";
        String message = "";
        String time = "";
        String id = "";

        ChatMessage cm = new ChatMessage(from, message, time, id);

        time = cm.getTime();
        id = firebaseRef.push().getKey();
        message = findViewById(R.id.newmsg).toString();
        from = cm.getFrom();

        Map<String, Object> chatMessage = new HashMap<String, Object>();
        chatMessage.put(id, cm);
        //firebaseRef.updateChildren(chatMessage);

        firebaseRef.push().setValue(chatMessage, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    System.out.println("Data could not be saved. " + firebaseError.getMessage());
                } else {
                    System.out.println("Data saved successfully.");
                }
            }
        });
    }

    public void ReadData(final Firebase firebase) {

        firebase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> newPost = (Map<String, Object>) dataSnapshot.getValue();
                System.out.println("Author: " + newPost.get("alan"));
                System.out.println("Title: " + newPost.get("gracehop"));

                AddDataToListView(newPost);

                //TextView view = (TextView) findViewById(R.id.txt1);
                //view.setText(newPost.get("alan").toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String title = (String) dataSnapshot.child("title").getValue();
                System.out.println("The updated post title is " + title);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String title = (String) dataSnapshot.child("title").getValue();
                System.out.println("The blog post titled " + title + " has been deleted");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.print("The read failed" + firebaseError.getMessage());
            }
        });
    }

    public void AddDataToListView(Map<String, Object> newPost) {

        ListView view = (ListView) findViewById(R.id.lstView);
        ArrayList<String> arrList = new ArrayList<>();

        /*for (String key : newPost.keySet()) {
            arrList.add(key);
        }*/

        arrList.add(newPost.get("message").toString());

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
