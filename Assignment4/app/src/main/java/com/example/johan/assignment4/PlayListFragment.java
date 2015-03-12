package com.example.johan.assignment4;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlayListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlayListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    MediaPlayerFragment mediaPlayerFragment;

    private PlayListAdapter playListAdapter;
    private ListView lstPlayList;
    public static ArrayList<Song> storeSongs = new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlayListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlayListFragment newInstance(String param1, String param2) {
        PlayListFragment fragment = new PlayListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PlayListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play_list, container, false);

        getActivity().setTitle("Play List");

        getSongList();
        sort(storeSongs);
        AddToListView(view);

        lstPlayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get clicked song from listview.
                Song song = (Song) parent.getAdapter().getItem(position);

                ((MainActivity)getActivity()).setSong(song);
                ((MainActivity)getActivity()).changeToMediaPlayerFragment();
            }
        });

        return view;
    }

    private void getSongList() {
        ContentResolver musicResolver = getActivity().getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumns = musicCursor.getColumnIndex( MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int uriColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            //Add songs to container (storeSongs).
            do {
                Long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumns);
                String thisArtist = musicCursor.getString(artistColumn);
                String thisUri = musicCursor.getString(uriColumn);

                storeSongs.add(new Song(thisId, thisTitle, thisArtist, thisUri));

            } while (musicCursor.moveToNext());

        }
    }

    public static Song setDefaultTrack() {
        Song defaultTrack = storeSongs.get(10);
        return defaultTrack;
    }

    public Song changeTrack(Song oldSong, int direction) {
        Song newSong = oldSong;
        //previous track.
        if(direction == -1) {
            for (int i = 0; i != storeSongs.size(); i++) {
                if (storeSongs.get(i).getId() == oldSong.getId()) {
                    //If we try to do -1 on first track.
                    if (i == 0) {
                        newSong = oldSong;
                        return newSong;
                    } else {
                        newSong = storeSongs.get(i -1);
                        return newSong;
                    }
                }
            }
        }
        //next track.
        else if (direction == 1) {
            for (int i = 0; i != storeSongs.size(); i++) {
                if (storeSongs.get(i).getId() == oldSong.getId()) {
                    //If we try to do +1 on last track.
                    if (i >= (storeSongs.size() -1) ) {
                        newSong = oldSong;
                        return newSong;
                    } else {
                        newSong = storeSongs.get(i +1);
                        return newSong;
                    }
                }
            }
        }

        return newSong;
    }

    private void AddToListView(View view) {
        if (playListAdapter == null) {
            playListAdapter = new PlayListAdapter(getActivity(), storeSongs);
        }

        this.lstPlayList = (ListView) view.findViewById(R.id.listView_play_list);

        lstPlayList.setAdapter(playListAdapter);
        playListAdapter.notifyDataSetChanged();
    }

    private void sort(ArrayList<Song> songs) {
        Collections.sort(songs, new Comparator<Song>() {
            @Override
            public int compare(Song lhs, Song rhs) {
                return lhs.getArtist().compareTo(rhs.getArtist());
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
