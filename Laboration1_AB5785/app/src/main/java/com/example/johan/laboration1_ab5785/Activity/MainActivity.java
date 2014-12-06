package com.example.johan.laboration1_ab5785.Activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.net.nsd.NsdManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

import com.example.johan.laboration1_ab5785.Fragment.AboutFragment;
import com.example.johan.laboration1_ab5785.Fragment.LoginFragment;
import com.example.johan.laboration1_ab5785.Fragment.RegisterFragment;
import com.example.johan.laboration1_ab5785.R;
import com.firebase.client.FirebaseError;

import static com.example.johan.laboration1_ab5785.R.id.login_txtUsername;
import static com.example.johan.laboration1_ab5785.R.id.reg_txtPassword;
import static com.example.johan.laboration1_ab5785.R.id.reg_txtUsername;


public class MainActivity extends Activity
    implements LoginFragment.OnFragmentInteractionListener,
        RegisterFragment.OnFragmentInteractionListener,
        AboutFragment.OnFragmentInteractionListener
{
    // firebaseRef.child("message").setValue("nisse");
    private static final String FIREBASE_URL ="https://luminous-heat-420.firebaseio.com";
    private Firebase firebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this); //Initialize Firebase library.
        firebaseRef = new Firebase(FIREBASE_URL);

        LoginFragment fragment = LoginFragment.newInstance("", "");
        FragmentManager fM = getFragmentManager();
        FragmentTransaction fT = fM.beginTransaction();
        fT.add(R.id.container, fragment, null);
        fT.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //Inflates the actionbar
        MenuInflater mif = getMenuInflater();
        mif.inflate(R.menu.menu_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
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

    //Change activity
    public void LoginBtnClick(View v) {
        final EditText editPwd = (EditText)findViewById(R.id.login_txtPassword);
        final EditText editUser = (EditText)findViewById(R.id.login_txtUsername);
        Log.v("användare", editUser.getText().toString());
        Log.v("lösenord", editPwd.getText().toString());
        //Authenticate the user
        firebaseRef.authWithPassword(editUser.getText().toString(), editPwd.getText().toString(), new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                Log.v(firebaseError.getMessage(),"");
                TextView errMsg = (TextView)findViewById(R.id.err_login);
                errMsg.setVisibility(View.VISIBLE);
                errMsg.getText().toString();
                Log.d("", errMsg.getText().toString());
            }
        });
    }

    public void RegisterBtnClick(View v) {
        final EditText editPwd = (EditText)findViewById(R.id.reg_txtPassword);
        final EditText editUser = (EditText)findViewById(R.id.reg_txtUsername);

        //Create a new user.
        firebaseRef.createUser(editUser.getText().toString(), editPwd.getText().toString(), new Firebase.ResultHandler(){
            @Override
            public void onSuccess() {
                //Authenticate the user
                firebaseRef.authWithPassword(editUser.getText().toString(), editPwd.getText().toString(), new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        //Succeded to create and authenticate the new user.
                        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        //Failed to Authenticate the new created user
                        Log.v(firebaseError.getMessage(),"");
                        TextView errMsg = (TextView)findViewById(R.id.err_login);
                        errMsg.setVisibility(View.VISIBLE);
                        errMsg.getText().toString();
                        Log.d("", errMsg.getText().toString());
                    }
                });
            }
            @Override
            public void onError(FirebaseError firebaseError) {
                //Failed to create new user.
                Log.v(firebaseError.getMessage(),"");
                TextView errMsg = (TextView)findViewById(R.id.err_reg);
                errMsg.setVisibility(View.VISIBLE);
                errMsg.getText().toString();
                Log.d("", errMsg.getText().toString());
            }
        });
    }

    // go to a new fragment
    public void GotoRegisterBtnClick(View v) {
        RegisterFragment fragment = RegisterFragment.newInstance("", "");
        FragmentManager fM = getFragmentManager();
        FragmentTransaction fT = fM.beginTransaction();
        fT.replace(R.id.container, fragment, null);
        fT.addToBackStack("goto register");
        fT.commit();
    }

    // go to a new fragment
    public void GotoAboutBtnClick(View v){
        AboutFragment fragment = AboutFragment.newInstance("", "");
        FragmentManager fM = getFragmentManager();
        FragmentTransaction fT = fM.beginTransaction();
        fT.replace(R.id.container, fragment, null);
        fT.addToBackStack("got to about");
        fT.commit();
    }

    // return to old fragment
    public void AboutBackBtnClick(View v){
        LoginFragment fragment = LoginFragment.newInstance("", "");
        FragmentManager fM = getFragmentManager();
        FragmentTransaction fT = fM.beginTransaction();
        fT.replace(R.id.container, fragment, null);
        fT.addToBackStack("about back");
        fT.commit();
    }
}
