package com.example.johan.assignment4;

import android.app.Activity;
import android.app.Fragment;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
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
    static String url = "R.raw.familiar_taste_of_poison";
    private SeekBar seekBar;

    private ImageButton btnPlay;
    private ImageButton btnPause;
    private ImageButton btnStop;
    private ImageButton btnForward;
    private ImageButton btnRewind;

    public TextView songName;
    public TextView txtEndTime;
    public TextView txtStartTime;

    private Handler mediaHandler = new Handler();

    //Attributes uses for controlling when musis should start after changing seekBar state.
    private boolean isPausePressed = false;
    private boolean isPlayPressed = false;
    private boolean isStopPressed = true;

    private OnFragmentInteractionListener mListener;

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

        songName = (TextView)view.findViewById(R.id.txt_currentsong);
        txtEndTime = (TextView)view.findViewById(R.id.txt_time_left);
        txtStartTime = (TextView)view.findViewById(R.id.txt_time_start);
        seekBar = (SeekBar)view.findViewById(R.id.seek_bar_elapsed_time);

        btnPlay = (ImageButton)view.findViewById(R.id.btn_play);
        btnPause = (ImageButton)view.findViewById(R.id.btn_pause);
        btnStop = (ImageButton)view.findViewById(R.id.btn_stop);
        btnRewind = (ImageButton)view.findViewById(R.id.btn_previous);
        btnForward = (ImageButton)view.findViewById(R.id.btn_next);

        songName.setText("familiar taste of poison.mp3");

        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.familiar_taste_of_poison);

        seekBar.setClickable(false);
        btnPause.setEnabled(false);
        btnStop.setEnabled(false);
        btnForward.setEnabled(false);
        btnRewind.setEnabled(false);

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

        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forward(view);
                isPausePressed = false;
                isPlayPressed = false;
                isStopPressed = false;
            }
        });

        btnRewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rewind(view);
                isPausePressed = false;
                isPlayPressed = false;
                isStopPressed = false;
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

        return view;
    }

    public void play(View view) {
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
                                TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) endTime)))
        );

        txtStartTime.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
        );

        seekBar.setProgress((int) startTime);
        mediaHandler.postDelayed(UpdateSongTime, 100);

        btnPause.setEnabled(true);
        btnStop.setEnabled(true);
        btnRewind.setEnabled(true);
        btnForward.setEnabled(true);

    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            txtStartTime.setText(String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) startTime),
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
            );
            seekBar.setProgress((int) startTime);
            mediaHandler.postDelayed(this, 100);
        }
    };

    public void stop(View view) {
        btnPause.setEnabled(false);
        btnForward.setEnabled(false);
        btnRewind.setEnabled(false);
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