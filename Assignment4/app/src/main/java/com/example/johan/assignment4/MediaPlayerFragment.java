package com.example.johan.assignment4;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
import android.widget.Toast;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class MediaPlayerFragment extends Fragment implements SensorEventListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MediaPlayer mediaPlayer;
    private double endTime;
    private double curretTime;
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
    private Handler sensorHandler = new Handler();

    //Attributes uses for controlling when music should start after changing seekBar state.
    private boolean isPausePressed = false;
    private boolean isPlayPressed = false;
    private boolean isStopPressed = true;

    private long btnTimeDown;
    private long btnDownDuration;

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    private TextView txtCurrentX;
    private TextView txtCurrentY;
    private TextView txtCurrentZ;

    private long lastKnock = 0;
    private int knocks = 0;
    private static final long COMMAND_DELAY = 4000;
    private static final double KNOCK_FILTER = 0.20;
    private Timer knockTimer;

    private static final float toMilliSec = 1000000.0f;

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

        getActivity().setTitle(R.string.fragment_mediaplayer);

        txtCurrentX = (TextView)view.findViewById(R.id.x_axis_value);
        txtCurrentY = (TextView)view.findViewById(R.id.y_axis_value);
        txtCurrentZ = (TextView)view.findViewById(R.id.z_axis_value);

        senSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

        songInfo = (TextView)view.findViewById(R.id.txt_currentsong);
        txtEndTime = (TextView)view.findViewById(R.id.txt_time_left);
        txtStartTime = (TextView)view.findViewById(R.id.txt_time_start);
        seekBar = (SeekBar)view.findViewById(R.id.seek_bar_elapsed_time);

        btnPlay = (ImageButton)view.findViewById(R.id.btn_play);
        btnPause = (ImageButton)view.findViewById(R.id.btn_pause);
        btnStop = (ImageButton)view.findViewById(R.id.btn_stop);
        btnBackward = (ImageButton)view.findViewById(R.id.btn_previous);
        btnForward = (ImageButton)view.findViewById(R.id.btn_next);

        seekBar.setClickable(false);
        btnPause.setEnabled(false);
        btnStop.setEnabled(false);
        btnForward.setEnabled(false);
        btnBackward.setEnabled(false);

        //Click events!
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    pause();
                    isPausePressed = true;
                    isPlayPressed = false;
                    isStopPressed = false;
                }
            });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
                isPausePressed = false;
                isPlayPressed = true;
                isStopPressed = false;
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
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
                        play();
                    }
                }
            }

        });

        //if a track has been choosen in the playList play the track.
         changeTrack();

        //Change track when current track is finish.
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                seekBar.setProgress(0);
                nextTrack();
            }
        });

        return view;
    }

    public void play() {
        //Check if a track has been choosen from playlist...
        if(currentSong.getId() != null) {
            mediaPlayer.start();
            endTime = mediaPlayer.getDuration();
            curretTime = mediaPlayer.getCurrentPosition();
            if (oneTimeOnly == 0) {
//                seekBar.setMax((int) endTime);
                oneTimeOnly = 1;
            }

//            txtEndTime.setText(String.format("%d min, %d sec",
//                            TimeUnit.MILLISECONDS.toMinutes((long) endTime),
//                            TimeUnit.MILLISECONDS.toSeconds((long) endTime) -
//                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) endTime)))
//            );

//            txtStartTime.setText(String.format("%d min, %d sec",
//                            TimeUnit.MILLISECONDS.toMinutes((long) curretTime),
//                            TimeUnit.MILLISECONDS.toSeconds((long) curretTime) -
//                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) curretTime)))
//            );

//            mediaHandler.postDelayed(updateSongTime, 100);

//            btnPause.setEnabled(true);
//            btnStop.setEnabled(true);
//            btnBackward.setEnabled(true);
//            btnForward.setEnabled(true);
//            btnPlay.setEnabled(false);
        }
    }

    Runnable nextTrack = new Runnable() {
        @Override
        public void run() {
            forward();
            trackHandler.postDelayed(this, 200);
        }
    };

    Runnable prevTrack = new Runnable() {
        @Override
        public void run() {
            rewind();
            trackHandler.postDelayed(this, 200);
        }
    };

    private Runnable updateSongTime = new Runnable() {
        public void run() {
            curretTime = mediaPlayer.getCurrentPosition();
            txtStartTime.setText(String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes((long) curretTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) curretTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) curretTime)))
            );
            seekBar.setProgress((int) curretTime);
            mediaHandler.postDelayed(this, 100);
        }
    };

    public void stop() {
        btnPause.setEnabled(false);
        btnForward.setEnabled(false);
        btnBackward.setEnabled(false);
        btnStop.setEnabled(false);
        btnPlay.setEnabled(true);

        seekBar.setProgress(0);
        mediaPlayer.pause();
        mediaPlayer.seekTo(0);
    }

    public void pause() {
        mediaPlayer.pause();
        btnPause.setEnabled(false);
        btnPlay.setEnabled(true);
    }

    public void forward() {
        int temp = (int)curretTime;
        if ((temp + forwardTime)<= endTime) {
            curretTime = curretTime + forwardTime;
            mediaPlayer.seekTo((int) curretTime);
        }
    }

    public void rewind() {
        int temp = (int) curretTime;
        if ((temp-backwardTime)> 0) {
            curretTime = curretTime - backwardTime;
            mediaPlayer.seekTo((int)curretTime);
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

            seekBar.setProgress(0);
            play();
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
        mediaPlayer.release();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final Sensor sensor = event.sensor;

        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            double c = event.values[0] * event.values[0] + event.values[1] * event.values[1] + event.values[2] * event.values[2];
            c = Math.sqrt(c); // length of acceleration vector.

            boolean test1 = Math.abs(c - 9.5) > KNOCK_FILTER;

            if (test1 && (event.timestamp - lastKnock) / toMilliSec > 250) {
                lastKnock = event.timestamp;
                knocks++;

                System.out.println(String.format("KNOCK: %d -- accel event (%.2f, %.2f, %.2f) | %.2f" + "/n", knocks, event.values[0], event.values[1], event.values[2], c));

                //Display values
                txtCurrentX.setText(Float.toString(event.values[0]));
                txtCurrentY.setText(Float.toString(event.values[1]));
                txtCurrentZ.setText(Float.toString(event.values[2]));

                //Display number of knocks.
                Toast.makeText(getActivity(), "Number of knocks is: " + knocks, Toast.LENGTH_SHORT).show();

                sensorHandler = new Handler();
                sensorHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switch (knocks) {
                            case 1:
                                if (mediaPlayer.isPlaying()) {
                                    pause();
                                } else {
                                    play();
                                }
                                System.out.println("PLAY");
                                break;
                            case 2:
                                nextTrack();
                                System.out.println("NEXT");
                                break;
                            case 3:
                                System.out.println("PREVIOUS");
                                previousTrack();
                                break;
                            case 4:
                                System.out.println("STOP");
                                stop();
                                break;
                        }

                        knocks = 0;
                        knockTimer = null;
                        lastKnock = 0;
                    }
                }, COMMAND_DELAY);
            }
        }
    }

    public void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    public void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}
