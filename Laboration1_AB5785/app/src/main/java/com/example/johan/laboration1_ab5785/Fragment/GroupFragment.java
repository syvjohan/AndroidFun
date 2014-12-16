package com.example.johan.laboration1_ab5785.Fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.example.johan.laboration1_ab5785.GroupAdapter;
import com.example.johan.laboration1_ab5785.R;
import com.example.johan.laboration1_ab5785.Group;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GroupFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupFragment extends ListFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button AddChatGroupBtnClick;

    private static final String FIREBASE_URL ="https://luminous-heat-420.firebaseio.com";
    private Firebase firebaseRef;

    List<Group> groupList;
    ChildEventListener childEventListener;

    private ArrayAdapter<Group> mAdapter;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupFragment newInstance(String param1, String param2) {
        GroupFragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public GroupFragment() {
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
        CreateGroupListFromFirebase();


        //mAdapter = new GroupAdapter(getActivity(), groupList);

    }


    private void CreateGroupListFromFirebase() {
        groupList = new ArrayList<>();
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (mAdapter == null) {
                    mAdapter = new ArrayAdapter<>(
                        getActivity(),
                        R.layout.fragment_group,
                        R.id.group_list,
                        groupList);
                    setListAdapter((android.widget.ListAdapter) groupList);
                }
                Group newGroup = new Group(dataSnapshot.getKey(), (String) dataSnapshot.child("name").getValue());
                groupList.add(newGroup);
            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        firebaseRef.addChildEventListener(childEventListener);
    }


    private void RegisterNewGroup() {
        //Save a new group to firebase.
        AddChatGroupBtnClick = (Button) getActivity().findViewById(R.id.btn_group_createNewGroup);
        AddChatGroupBtnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                groupList.add(new Group("1", "android"));
                groupList.add(new Group("2", "iPhone"));
                groupList.add(new Group("3", "windowsPhone"));

                firebaseRef.setValue(groupList);

                //Firebase fb = firebaseRef.push();
                //String editGroupName = ((EditText)view.findViewById(R.id.txtgroup_name)).getText().toString();
                //group.add(new Group(fb.getKey(), editGroupName));
                //fb.setValue(groupList);

                //After adding chat group enter chat!
                ChatFragment fragment = ChatFragment.newInstance("", "");
                FragmentManager fM = getFragmentManager();
                FragmentTransaction fT = fM.beginTransaction();
                fT.replace(R.id.chatcontainer, fragment, null);
                fT.addToBackStack("got to chat");
                fT.commit();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RegisterNewGroup();

        View view = inflater.inflate(R.layout.fragment_group, container, false);


        ListView list = (ListView) view.findViewById(R.id.chatcontainer);
        list.setAdapter(mAdapter);

        list.setOnItemClickListener(mOnItemClickListener);

        list.setOnItemLongClickListener(mOnItemLongClickListener);

        return view;
    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String groupName = groupList.get(position).getName();
            Toast.makeText(getActivity(), "Short click on " + groupName, Toast.LENGTH_SHORT).show();
        }
    };

    private AdapterView.OnItemLongClickListener mOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            String groupName = groupList.get(position).getName();
            Toast.makeText(getActivity(), "Long click on " + groupName, Toast.LENGTH_SHORT).show();
            return false;
        }
    };


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
