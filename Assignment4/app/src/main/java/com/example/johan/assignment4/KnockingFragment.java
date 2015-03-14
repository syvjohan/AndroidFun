package com.example.johan.assignment4;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class KnockingFragment extends Fragment implements SensorEventListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    private float lastX;
    private float lastY;
    private float lastZ;

    private TextView txtCurrentX;
    private TextView txtCurrentY;
    private TextView txtCurrentZ;

    private float dt;
    private int knockCount;
    private long oldTimestamp;

    private static final float toMilliSec = 1000000.0f;

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types and number of parameters
    public static KnockingFragment newInstance(String param1, String param2) {
        KnockingFragment fragment = new KnockingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public KnockingFragment() {
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
        View view = inflater.inflate(R.layout.fragment_knocking, container, false);

        txtCurrentX = (TextView)view.findViewById(R.id.x_axis_value);
        txtCurrentY = (TextView)view.findViewById(R.id.y_axis_value);
        txtCurrentZ = (TextView)view.findViewById(R.id.z_axis_value);

        senSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
        return view;
    }

    private void displayCleanValues() {
        txtCurrentX.setText("0.00");
        txtCurrentZ.setText("0.00");
        txtCurrentY.setText("0.00");
    }

    private void displayCurrentValues() {
        txtCurrentX.setText(Float.toString(lastX));
        txtCurrentY.setText(Float.toString(lastY));
        txtCurrentZ.setText(Float.toString(lastZ));
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

    public void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    public void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;

        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

                if (lastY > 0.32) {
                    dt = (event.timestamp - oldTimestamp) / toMilliSec;
                    oldTimestamp = event.timestamp;

                    knockCount++;

                    //If the time between the knocks is higher than 2 sec.
                    if (dt >= 1500) {
                        switch (knockCount) {
                            case 1:
                                //Play/Pause track
                                System.out.println("Play");
                                ((MainActivity)getActivity()).setmediaPlayerState(1);
                            break;

                            case 2:
                                //Next track.
                                System.out.println("Next");
                                ((MainActivity)getActivity()).setmediaPlayerState(2);
                            break;

                            case 3:
                                //Previous track.
                                System.out.println("Previous");
                                ((MainActivity)getActivity()).setmediaPlayerState(3);
                             break;

                            case 4:
                                //Stop track
                                System.out.println("Stop");
                                ((MainActivity)getActivity()).setmediaPlayerState(4);
                            break;

                            default:
                                knockCount = 0;
                            break;
                        }
                        knockCount = 0;
                        System.out.println("Setting knockout to 0");
                    }

                    displayCurrentValues();
                }

                lastY = y;
                lastX = x;
                lastZ = z;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
