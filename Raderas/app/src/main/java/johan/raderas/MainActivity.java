package johan.raderas;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import johan.raderas.User;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);

        String FIREBASE_URL = "https://luminous-heat-420.firebaseio.com/";
        Firebase firebase = new Firebase(FIREBASE_URL);

        //CreateInfo(firebase);
        //CreateNewPost(firebase);
        //UpdateInfo(firebase);
        ReadData(firebase);
    }

    public void CreateInfo(Firebase firebase) {

        User alan = new User("Alan Turing", 1912);
        User gracehop = new User("Grace Hopper", 1906);

        Firebase userRef = firebase.child("users");

        Map<String, User> users = new HashMap<String, User>();
        users.put("alan", alan);
        users.put("gracehop", gracehop);

        firebase.push().setValue(users, new Firebase.CompletionListener() {
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

    public void AddDataToListView(Map<String, Object> newPost) {

        ListView view = (ListView) findViewById(R.id.lstView);
        ArrayList<Map<String, Object>> arrList = new ArrayList<Map<String, Object>>();
        arrList.add(newPost);

        ListAdapter arrAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, arrList);

        view.setAdapter(arrAdapter);

    }

    public void UpdateInfo(Firebase firebase) {
        Firebase ref = firebase.child("gracehop");
        Map<String, Object> nickname = new HashMap<String, Object>();
        nickname.put("nickname", "Amazing grace");
        ref.updateChildren(nickname);
    }

    public  void CreateNewPost(Firebase firebase) {
        // Generate a reference to a new location and add some data using push()
        Firebase postRef = firebase.child("posts");
        Firebase newPostRef = postRef.push();

        // Add some data to the new location
        Map<String, String> post1 = new HashMap<String, String>();
        post1.put("author", "gracehop");
        post1.put("title", "Announcing COBOL, a New Programming Language");
        newPostRef.setValue(post1);

        // Get the unique ID generated by push()
        String postId = newPostRef.getKey();
        System.out.println(postId);
    }

    public void ReadData(final Firebase firebase) {

        firebase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> newPost = (Map<String, Object>)dataSnapshot.getValue();
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
