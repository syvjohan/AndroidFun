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

    MediaPlayerFragment mediaPlayerFragment;
    PlayListFragment playListFragment;

    private Song currentSong; //Fragments change this variable

    private final int MENU_ITEM_MEDIAPLAYER = Menu.FIRST;
    private final int MENU_ITEM_PLAYLIST = MENU_ITEM_MEDIAPLAYER + 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getOverflowMenu();
        changeToPlayListFragment(); //Start view
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
                changeToMediaPlayerFragment();
                break;

            case MENU_ITEM_PLAYLIST:
                changeToPlayListFragment();
                break;

            default:
                changeToPlayListFragment();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setSong(Song song) {
        this.currentSong = song;
    }

    public void requestNewSong(Song oldSong, int direction) {
       currentSong = playListFragment.changeTrack(oldSong, direction);
       changeToMediaPlayerFragment();
    }

    public void changeToMediaPlayerFragment() {
        if (mediaPlayerFragment == null) {
            mediaPlayerFragment = MediaPlayerFragment.newInstance("", "");
        }

        //First time mediaPlayerFragment get called without a song choosen.
        if (currentSong == null) {
            currentSong = playListFragment.setDefaultTrack();
        }

        mediaPlayerFragment.setNewSong(currentSong);
        mediaPlayerFragment.changeTrack();
        FragmentManager fM = getFragmentManager();
        FragmentTransaction fT = fM.beginTransaction();
        fT.replace(R.id.container, mediaPlayerFragment, null);
        fT.addToBackStack("go to mediaPlayer fragmement");
        fT.commit();
    }

    public void changeToPlayListFragment() {
        if (playListFragment == null) {
            playListFragment = PlayListFragment.newInstance("", "");
        }
        FragmentManager fME = getFragmentManager();
        FragmentTransaction fTE = fME.beginTransaction();
        fTE.replace(R.id.container, playListFragment, null);
        fTE.addToBackStack("go to Playlist fragment");
        fTE.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        super.onBackPressed();
    }
}
