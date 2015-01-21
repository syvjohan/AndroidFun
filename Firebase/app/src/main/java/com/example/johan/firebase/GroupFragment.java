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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GroupFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    static ArrayList<Group> groupNameList = new ArrayList<Group>();
    static ArrayList<String> groupKeyValues = new ArrayList<String>();
    GroupAdapter groupAdapter;
    ListView lstViewGroup;
    String groupId;

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

        Firebase.setAndroidContext(getActivity());
        Firebase firebaserootRef = new Firebase("https://luminous-heat-420.firebaseio.com");

        groupId = CreateNewGroup(firebaserootRef);
        ReadData(firebaserootRef);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public String CreateNewGroup(Firebase firebaseRootRef) {
        Map<String, Group> newGroup = new HashMap<>();

        Firebase firebaseGroup = firebaseRootRef.child("").push();

        Group group = new Group();

        group.SetID(firebaseGroup.getKey());
        group.SetName("Pelles grupp");

        String id = firebaseGroup.getKey();
        firebaseGroup.child("id").setValue(group.GetId());
        firebaseGroup.child("name").setValue(group.GetName());

        newGroup.put(id, group);
        Log.d(group.GetId().toString(), group.GetName().toString());
        //Firebase firebaseParentMsg = firebaseGroup.child("messages");

        return group.GetId();
    }

    public void ReadData(final Firebase firebaseRootRef) {
        firebaseRootRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String s) {
                if (snapshot.getValue() != null) {
                    for (DataSnapshot c : snapshot.getChildren()) {
                        Group newGroup = new Group();
                        newGroup.SetName((String) c.child("name").getValue());
                        newGroup.SetID((String) c.getKey());
                        //System.out.println(c);
                        if(!groupKeyValues.contains(newGroup.GetId())) {
                            groupKeyValues.add(newGroup.GetId());

                            AddToLstViewGroup(newGroup);
                        }
                    }
                }
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
    }

    public void AddToLstViewGroup(Group newGroup) {
        groupNameList.add(newGroup);

        if(groupAdapter == null) {
            groupAdapter = new GroupAdapter(getActivity(), groupNameList);
        }

        lstViewGroup = (ListView) getView().findViewById(R.id.listView_group);
        lstViewGroup.setOnItemClickListener(onItemClickListener);
        lstViewGroup.setOnItemLongClickListener(onItemLongClickListener);

        lstViewGroup.setAdapter(groupAdapter);
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

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getActivity().getBaseContext(), ChatActivity.class);
            intent.putExtra("groupID",groupId);
            startActivity(intent);
        }
    };

    private AdapterView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getActivity().getBaseContext(), ChatActivity.class);
            intent.putExtra("groupID",groupId);
            startActivity(intent);
            return true;
        }
    };
}
