package com.example.johan.firebase;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Firebase firebaseRef = new Firebase("https://luminous-heat-420.firebaseio.com");

    private Button RegisterBtnClick;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public RegisterFragment() {
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
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        RegisterBtnClick = (Button)view.findViewById(R.id.btn_register);
        RegisterBtnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editPwd = (EditText) getView().findViewById(R.id.reg_txtPassword);
                final EditText editUser = (EditText) getView().findViewById(R.id.reg_txtUsername);
                Log.d(editUser.getText().toString(), editPwd.getText().toString());
                //Create a new user.
                firebaseRef.createUser(editUser.getText().toString(), editPwd.getText().toString(), new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {
                        //Authenticate the user
                        firebaseRef.authWithPassword(editUser.getText().toString(), editPwd.getText().toString(), new Firebase.AuthResultHandler() {
                            @Override
                            public void onAuthenticated(AuthData authData) {
                                //Succeded to create and authenticate the new user.
                                Intent intent = new Intent(getActivity(), ChatActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onAuthenticationError(FirebaseError firebaseError) {
                                TextView errMsg = (TextView) getView().findViewById(R.id.err_reg);

                                switch(firebaseError.getCode()) {
                                    case FirebaseError.EMAIL_TAKEN:
                                        errMsg.setText(R.string.EMAIL_TAKEN);
                                        break;
                                    case FirebaseError.NETWORK_ERROR:
                                        errMsg.setText(R.string.NETWORK_ERROR);
                                        break;
                                    case FirebaseError.UNKNOWN_ERROR:
                                        errMsg.setText(R.string.UNKNOWN_ERROR);
                                        break;
                                    default:
                                        errMsg.setText(R.string.DEFAULT_ERROR);
                                        break;

                                }
                                errMsg.setVisibility(View.VISIBLE);
                                errMsg.getText().toString();

                            }
                        });
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        TextView errMsg = (TextView) getView().findViewById(R.id.err_login);

                        switch(firebaseError.getCode()) {
                            case FirebaseError.EMAIL_TAKEN:
                                errMsg.setText(R.string.EMAIL_TAKEN);
                                break;
                            case FirebaseError.NETWORK_ERROR:
                                errMsg.setText(R.string.NETWORK_ERROR);
                                break;
                            case FirebaseError.UNKNOWN_ERROR:
                                errMsg.setText(R.string.UNKNOWN_ERROR);
                                break;
                            default:
                                errMsg.setText(R.string.DEFAULT_ERROR);
                                break;

                        }
                        errMsg.setVisibility(View.VISIBLE);
                        errMsg.getText().toString();
                    }
                });
            }
        });

        return view;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
