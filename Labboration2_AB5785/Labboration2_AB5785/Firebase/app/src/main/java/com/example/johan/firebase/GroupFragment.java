package com.example.johan.firebase;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GroupFragment extends Fragment implements
        View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static ArrayList<Group> groupList = new ArrayList<Group>();
    private GroupAdapter groupAdapter;
    private ListView lstViewGroup;
    private Button RegisterNewGroup;
    public static String username;

    private Parcelable state;
    private Handler handlerGroups = new Handler();
    private static final long COMMAND_DELAY = 1000;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group, container, false);

        lstViewGroup = (ListView) view.findViewById(R.id.listView_group);
        RegisterNewGroup = (Button)view.findViewById(R.id.btn_reg_new_group);

        RegisterNewGroup.setOnClickListener(this);
        lstViewGroup.setOnItemClickListener(onItemClickListener);
        lstViewGroup.setOnItemLongClickListener(onItemLongClickListener);


        handlerGroups.postDelayed(ReadGroupData, COMMAND_DELAY);

        return view;
    }

    @Override
    public void onClick(View v) {
        EditText editRegNewGr = (EditText) getView().findViewById(R.id.txt_group_name);

        String groupName = editRegNewGr.getText().toString();
        if (!groupName.isEmpty()) {
            CreateNewGroup(groupName);
            editRegNewGr.setText("");
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    // TODO, Skapa ett testmeddelande!

    public void CreateNewGroup(String groupName) {

        Map<String, Group> newGroup = new HashMap<>();

        Firebase  firebaserootRef = new Firebase("https://luminous-heat-420.firebaseio.com");
        Firebase firebaseGroup = firebaserootRef.child("").push();

        Group group = new Group();

        group.SetID(firebaseGroup.getKey());
        group.SetName(groupName);

        String id = firebaseGroup.getKey();
        firebaseGroup.child("id").setValue(group.GetId());
        firebaseGroup.child("name").setValue(group.GetName());

        newGroup.put(id, group);

        ChangeToChatFragment(group.GetId());
    }

    private Runnable ReadGroupData = new Runnable() {
        @Override
        public void run() {
            Firebase  firebaserootRef = new Firebase("https://luminous-heat-420.firebaseio.com");
            firebaserootRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot snapshot, String s) {
                    if (snapshot.exists()) {
                        Group newGroup = new Group((String) snapshot.child("name").getValue(), (String) snapshot.child("id").getValue());
                        AddToLstViewGroup(newGroup);
                    }

                    lstViewGroup.onRestoreInstanceState(state);
                }

                @Override
                public void onChildChanged(DataSnapshot snapshot, String s) {
                }

                @Override
                public void onChildRemoved(DataSnapshot snapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot snapshot, String s) {
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                }
            });
            state = lstViewGroup.onSaveInstanceState();
            handlerGroups.postDelayed(this, COMMAND_DELAY);
        }
    };

    public boolean ComapareId(Group gr1, ArrayList<Group> lst) {
        for (Group g : lst) {
            if (gr1.GetId() == g.GetId()) {
                return true;
            }
        }

        return false;
    }

    public void AddToLstViewGroup(Group newGroup) {
       if (newGroup.GetName() != null) {
            if (ComapareId(newGroup, groupList) == false) {
                groupList.add(newGroup);
            }

            if(groupAdapter == null) {
                groupAdapter = new GroupAdapter(getActivity(), groupList);
            }

            lstViewGroup.setAdapter(groupAdapter);
            groupAdapter.notifyDataSetChanged();
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

    public void ChangeToChatFragment(String groupId) {
        ChatFragment chatFragment = ChatFragment.newInstance("", "");
        chatFragment.SetGroupID(groupId);
        chatFragment.GetUsername(username);
        FragmentManager fM = getFragmentManager();
        FragmentTransaction fT = fM.beginTransaction();
        fT.replace(R.id.container_chat, chatFragment, null);
        fT.addToBackStack("go to chat fragmement");
        fT.commit();
    }

    public void SetUsername(String username) {
        this.username = username;

    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d("(Short click) clicked on group item in listView!", "GOTO Chatfragment");
            Group group = groupAdapter.getItem(position);
            ChangeToChatFragment(group.GetId());
        }
    };

    private AdapterView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d("(Long click) clicked on group item in listView!", "GOTO Chatfragment");
            Group group = groupAdapter.getItem(position);
            ChangeToChatFragment(group.GetId());
            return true;
        }
    };

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
