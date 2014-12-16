package com.example.johan.laboration1_ab5785.Fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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

import com.example.johan.laboration1_ab5785.Activity.ChatActivity;
import com.example.johan.laboration1_ab5785.Activity.MainActivity;
import com.example.johan.laboration1_ab5785.R;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	private Button GotoRegisterBtnClick;
	private Button GotoAboutBtnClick;
	private Button LoginBtnClick;
    private Button GotoRegister;

	private static final String FIREBASE_URL ="https://luminous-heat-420.firebaseio.com";
	private Firebase firebaseRef;

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment LoginFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static LoginFragment newInstance(String param1, String param2) {
		LoginFragment fragment = new LoginFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public LoginFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}

		Firebase.setAndroidContext(getActivity()); //Initialize Firebase library.
		firebaseRef = new Firebase(FIREBASE_URL);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_login, container, false);



		// go to a new fragment
		GotoAboutBtnClick = (Button) view.findViewById(R.id.btn_about);
		GotoAboutBtnClick.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				AboutFragment fragment = AboutFragment.newInstance("", "");
				FragmentManager fM = getFragmentManager();
				FragmentTransaction fT = fM.beginTransaction();
				fT.replace(R.id.container, fragment, null);
				fT.addToBackStack("got to about");
				fT.commit();
			}
		});

        GotoRegisterBtnClick = (Button) view.findViewById(R.id.btn_goto_register);
        GotoRegisterBtnClick.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment fragment = RegisterFragment.newInstance("", "");
                FragmentManager fM = getFragmentManager();
                FragmentTransaction fT = fM.beginTransaction();
                fT.replace(R.id.container, fragment, null);
                fT.addToBackStack("got to register");
                fT.commit();
            }
        }));

	    LoginBtnClick = (Button)view.findViewById(R.id.btn_login);
		LoginBtnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editPwd = (EditText) getView().findViewById(R.id.login_txtPassword);
                EditText editName = (EditText) getView().findViewById(R.id.login_txtUsername);

                //Authenticate the user
                firebaseRef.authWithPassword(editName.getText().toString(), editPwd.getText().toString(), new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        Intent intent = new Intent(getActivity(), ChatActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        Log.v(firebaseError.getMessage(), "");
                        TextView errMsg = (TextView) getView().findViewById(R.id.err_login);
                        errMsg.setVisibility(View.VISIBLE);
                        errMsg.getText().toString();
                        Log.d("", errMsg.getText().toString());
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
