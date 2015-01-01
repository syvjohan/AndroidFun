package johan.laboration2_ab5785.chat_app.Fragment;

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
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.Firebase;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import johan.laboration2_ab5785.chat_app.Group.*;

import java.security.acl.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import johan.laboration2_ab5785.R;
import johan.laboration2_ab5785.chat_app.Group.Group;

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

    private Firebase firebaseRef;

    ArrayList<Group> groupList = new ArrayList<>();

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
        firebaseRef = new Firebase(getString(R.string.FIREBASE_URL));

        firebaseRef.push().setValue("hej");

        mAdapter = new GroupAdapter(getActivity(), groupList);

        CreateGroupListFromFirebase();
    }


    public void CreateGroupListFromFirebase() {

        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        /*Group gr = null;
        Map<String, Group> map = new HashMap<String, Group>();
        map.put(gr.getId(), new Group("873030", "karl"));
        Firebase ref = new Firebase((getString(R.string.FIREBASE_URL))).child("karl");
        ref.setValue(map);

        Map<String, Object> updates = new HashMap<String, Object>();
        updates.put("first", "Fred");
        updates.put("last", "Swanson");
        firebaseRef.updateChildren(updates);*/



    }

    private void RegisterNewGroup() {
        //Save a new group to firebase.
        AddChatGroupBtnClick = (Button) getActivity().findViewById(R.id.btn_group_createNewGroup);
        AddChatGroupBtnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
