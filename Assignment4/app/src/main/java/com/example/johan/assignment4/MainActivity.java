package com.example.johan.assignment4;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;

import java.lang.reflect.Field;


public class MainActivity extends ActionBarActivity implements
            MediaPlayerFragment.OnFragmentInteractionListener,
            PlayListFragment.OnFragmentInteractionListener {

    private final int MENU_ITEM_MEDIAPLAYER = Menu.FIRST;
    private final int MENU_ITEM_PLAYLIST = MENU_ITEM_MEDIAPLAYER + 1;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getOverflowMenu();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        menu.add(0, MENU_ITEM_MEDIAPLAYER, 0 ,"MediaPlayer");
        menu.add(0, MENU_ITEM_PLAYLIST, 0,"Playlist");

        return super.onCreateOptionsMenu(menu);
    }

    private void getOverflowMenu() {

        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {

            case MENU_ITEM_MEDIAPLAYER:
                MediaPlayerFragment incomeFragment = MediaPlayerFragment.newInstance("", "");
                FragmentManager fMI = getFragmentManager();
                FragmentTransaction fTI = fMI.beginTransaction();
                fTI.replace(R.id.container, incomeFragment, null);
                fTI.addToBackStack("got to Income fragment");
                fTI.commit();
                break;

            case MENU_ITEM_PLAYLIST:
                PlayListFragment expenseFragment = PlayListFragment.newInstance("", "");
                FragmentManager fME = getFragmentManager();
                FragmentTransaction fTE = fME.beginTransaction();
                fTE.replace(R.id.container, expenseFragment, null);
                fTE.addToBackStack("got to Expense fragment");
                fTE.commit();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
