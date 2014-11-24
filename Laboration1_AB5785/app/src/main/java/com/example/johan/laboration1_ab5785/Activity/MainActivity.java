package com.example.johan.laboration1_ab5785.Activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.net.nsd.NsdManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.johan.laboration1_ab5785.Fragment.AboutFragment;
import com.example.johan.laboration1_ab5785.Fragment.LoginFragment;
import com.example.johan.laboration1_ab5785.Fragment.RegisterFragment;
import com.example.johan.laboration1_ab5785.R;


public class MainActivity extends Activity
    implements LoginFragment.OnFragmentInteractionListener,
        RegisterFragment.OnFragmentInteractionListener,
        AboutFragment.OnFragmentInteractionListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }

    public void RegisterBtnClick(View v) {
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }

    // go to a new fragment
    public void GotoRegisterBtnClick(View v) {
        RegisterFragment fragment = RegisterFragment.newInstance("", "");
        FragmentManager fM = getFragmentManager();
        FragmentTransaction fT = fM.beginTransaction();
        fT.replace(R.id.container, fragment, null);
        fT.commit();
    }

    // go to a new fragment
    public void GotoAboutBtnClick(View v){
        AboutFragment fragment = AboutFragment.newInstance("", "");
        FragmentManager fM = getFragmentManager();
        FragmentTransaction fT = fM.beginTransaction();
        fT.replace(R.id.container, fragment, null);
        fT.commit();
    }

    // return to old fragment
    public void AboutBackBtnClick(View v){
        LoginFragment fragment = LoginFragment.newInstance("", "");
        FragmentManager fM = getFragmentManager();
        FragmentTransaction fT = fM.beginTransaction();
        fT.replace(R.id.container, fragment, null);
        fT.commit();
    }
}
