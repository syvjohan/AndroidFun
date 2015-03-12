package com.example.johan.assignment4;

import android.app.Activity;
import android.app.Fragment;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MediaPlayerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MediaPlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MediaPlayerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MediaPlayer mediaPlayer;
    private double endTime = 0;
    private double startTime = 0;
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    public static int oneTimeOnly = 0;

    private SeekBar seekBar;

    private ImageButton btnPlay;
    private ImageButton btnPause;
    private ImageButton btnStop;
    private ImageButton btnForward;
    private ImageButton btnBackward;

    public TextView songInfo;
    public TextView txtEndTime;
    public TextView txtStartTime;

    private Handler mediaHandler = new Handler();
    private Handler trackHandler;

    //Attributes uses for controlling when music should start after changing seekBar state.
    private boolean isPausePressed = false;
    private boolean isPlayPressed = false;
    private boolean isStopPressed = true;

    private long btnTimeDown;
    private long btnDownDuration;

    private OnFragmentInteractionListener mListener;

    private Song currentSong;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MediaPlayerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MediaPlayerFragment newInstance(String param1, String param2) {
        MediaPlayerFragment fragment = new MediaPlayerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MediaPlayerFragment() {
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
        final View view = inflater.inflate(R.layout.fragment_media_player, container, false);

        getActivity().setTitle("Media Player");

        songInfo = (TextView)view.findViewById(R.id.txt_currentsong);
        txtEndTime = (TextView)view.findViewById(R.id.txt_time_left);
        txtStartTime = (TextView)view.findViewById(R.id.txt_time_start);
        seekBar = (SeekBar)view.findViewById(R.id.seek_bar_elapsed_time);

        btnPlay = (ImageButton)view.findViewById(R.id.btn_play);
        btnPause = (ImageButton)view.findViewById(R.id.btn_pause);
        btnStop = (ImageButton)view.findViewById(R.id.btn_stop);
        btnBackward = (ImageButton)view.findViewById(R.id.btn_previous);
        btnForward = (ImageButton)view.findViewById(R.id.btn_next);

        songInfo.setText(currentSong.getArtist() + " - " + currentSong.getTitle());

        mediaPlayer = MediaPlayer.create(getActivity(), Uri.parse(currentSong.getUri()));

        seekBar.setClickable(false);
        btnPause.setEnabled(false);
        btnStop.setEnabled(false);
        btnForward.setEnabled(false);
        btnBackward.setEnabled(false);

        //Click events!
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    pause(view);
                    isPausePressed = true;
                    isPlayPressed = false;
                    isStopPressed = false;
                }
            });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play(view);
                isPausePressed = false;
                isPlayPressed = true;
                isStopPressed = false;
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop(view);
                isPausePressed = false;
                isPlayPressed = false;
                isStopPressed = true;
            }
        });

        btnForward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (trackHandler != null) {
                            return true;
                        }
                        btnTimeDown = System.currentTimeMillis();
                        trackHandler = new Handler();
                        trackHandler.postDelayed(nextTrack, 200);
                        break;
                    case MotionEvent.ACTION_UP:
                        btnDownDuration = System.currentTimeMillis() - btnTimeDown;
                        if (btnDownDuration <= 200) {
                            nextTrack();

                        }
                        trackHandler.removeCallbacks(nextTrack);
                        trackHandler = null;
                        break;
                }
                return false;
            }
        });

        btnBackward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (trackHandler != null) {
                            return true;
                        }
                        btnTimeDown = System.currentTimeMillis();
                        trackHandler = new Handler();
                        trackHandler.postDelayed(prevTrack, 200);
                        break;
                    case MotionEvent.ACTION_UP:
                        btnDownDuration = System.currentTimeMillis() - btnTimeDown;
                        if (btnDownDuration <= 200) {
                            previousTrack();

                        }
                        trackHandler.removeCallbacks(prevTrack);
                        trackHandler = null;
                        break;
                }
                return false;
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar s) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar s) {

            }

            @Override
            public void onProgressChanged(SeekBar s, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress);

                    // Check if pause, play or stop buttons is pressed
                    if(!isPausePressed && !isPlayPressed && !isStopPressed) {
                        play(view);
                    }
                }
            }

        });

        //Change track when current track is finish.
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                nextTrack();
            }
        });


        //if a track has been choosen in the playList play the track.
        if (currentSong != null) {
            preparePlayerForTrackChange(view);
        }

        return view;
    }

    public void play(View view) {
        //Check if a track has been choosen from playlist...
        if(currentSong.getId() != null) {
            mediaPlayer.start();
            endTime = mediaPlayer.getDuration();
            startTime = mediaPlayer.getCurrentPosition();
            if (oneTimeOnly == 0) {
                seekBar.setMax((int) endTime);
                oneTimeOnly = 1;
            }

            txtEndTime.setText(String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes((long) endTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) endTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) endTime)))
            );

            txtStartTime.setText(String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
            );

            seekBar.setProgress((int) startTime);
            mediaHandler.postDelayed(updateSongTime, 100);

            btnPause.setEnabled(true);
            btnStop.setEnabled(true);
            btnBackward.setEnabled(true);
            btnForward.setEnabled(true);
        }
    }

    Runnable nextTrack = new Runnable() {
        @Override
        public void run() {
            forward(getView());
            trackHandler.postDelayed(this, 200);
        }
    };

    Runnable prevTrack = new Runnable() {
        @Override
        public void run() {
            rewind(getView());
            trackHandler.postDelayed(this, 200);
        }
    };

    private Runnable updateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            txtStartTime.setText(String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
            );
            seekBar.setProgress((int) startTime);
            mediaHandler.postDelayed(this, 300);
        }
    };

    public void stop(View view) {
        btnPause.setEnabled(false);
        btnForward.setEnabled(false);
        btnBackward.setEnabled(false);
        btnStop.setEnabled(false);

        mediaPlayer.pause();
        mediaPlayer.seekTo(0);
    }

    public void pause(View view) {
        mediaPlayer.pause();
        btnPause.setEnabled(false);
        btnPlay.setEnabled(true);
    }

    public void forward(View view) {
        int temp = (int)startTime;
        if ((temp + forwardTime)<= endTime) {
            startTime = startTime + forwardTime;
            mediaPlayer.seekTo((int) startTime);
        }
    }

    public void rewind(View view) {
        int temp = (int) startTime;
        if ((temp-backwardTime)> 0) {
            startTime = startTime - backwardTime;
            mediaPlayer.seekTo((int)startTime);
        }
    }

    public void nextTrack() {
        ((MainActivity)getActivity()).requestNewSong(currentSong, 1);
    }

    public void previousTrack() {
        ((MainActivity) getActivity()).requestNewSong(currentSong, -1);
    }

    public void setNewSong(Song song) {
        this.currentSong = song;
    }

    public void changeTrack() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
        }

        if (songInfo != null) {
            songInfo.setText(currentSong.getArtist() + " - " + currentSong.getTitle());
            mediaPlayer = MediaPlayer.create(getActivity(), Uri.parse(currentSong.getUri()));
            play(getView());
        }
    }

    public void preparePlayerForTrackChange(View view) {
        startTime = 0;
        endTime = 0;
        seekBar.setProgress((int) startTime);
        mediaPlayer.seekTo(0);
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
        mediaPlayer.release();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}
