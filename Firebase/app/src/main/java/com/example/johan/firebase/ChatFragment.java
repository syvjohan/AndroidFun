package com.example.johan.firebase;

import android.app.Activity;

import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    final Firebase firebaserootRef = new Firebase("https://luminous-heat-420.firebaseio.com");

    static ArrayList<Message> chatMsgList = new ArrayList<>();
    static ArrayList<String> msgKeyValues = new ArrayList<>();
    ChatAdapter chatAdapter = null;
    ListView lstViewChat;
    static String username = "Mig";

    EditText editMsg;
    Button btnSend;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ChatFragment() {
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

    public String GetGroupID() {
        Bundle bundle = this.getArguments();
        String groupID = bundle.getString("groupID");
        if (groupID == null) {
            return "";
        }
        return groupID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        editMsg = (EditText)  view.findViewById(R.id.txt_message_input);
        btnSend = (Button) view.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);

        ReadChatMessages(firebaserootRef);

        return view;
    }

    @Override
    public void onClick(View v) {
        String txtMsg = editMsg.getText().toString();
        if (txtMsg != "") {
            CreateNewMessage(firebaserootRef, txtMsg);
        }
    }

    public void  CreateNewMessage(Firebase firebaserootRef, String textMsg) {
        Map<String, Message> chatMessages = new HashMap<>();

        String msg = textMsg;
        String from = username;
        String time = "14:44";
        String id = "";

        //Message
        String groupID = GetGroupID();

        if (groupID != "") {
            Firebase firebaseParentMsg = firebaserootRef.child(groupID).child("messages");
            Firebase firebaseMsg = firebaseParentMsg.push();

            Message cm = new Message(id, from, msg, time);

            id = firebaseMsg.getKey();
            firebaseMsg.child("from").setValue(cm.from);
            firebaseMsg.child("message").setValue(cm.message);
            firebaseMsg.child("time").setValue(cm.time);

            chatMessages.put(id,cm);
        }

        ReadChatMessages(firebaserootRef);
    }

    public void ReadChatMessages(Firebase firebaseRootRef) {
        firebaseRootRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String s) {
                if (snapshot.child("messages").getValue() != null) {
                    for (DataSnapshot c : snapshot.child("messages").getChildren()) {
                        String key = c.getKey();

                        Message newMessage = new Message();
                        newMessage.SetFrom((String) c.child("from").getValue());
                        newMessage.SetMsg((String) c.child("message").getValue());
                        newMessage.SetTime((String) c.child("time").getValue());
                        newMessage.SetId((String) c.child("id").getValue());

                        if (!msgKeyValues.contains(key)) {
                            msgKeyValues.add(key);

                            AddToLstViewGroup(newMessage);
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

    public boolean IsMsgFromMe(Message message) {
        boolean isSenderMe = username.equals((CharSequence) message.GetFrom());
        return isSenderMe;
    }

    public void AddToLstViewGroup(Message newMessage) {
        chatMsgList.add(newMessage);

        if (chatAdapter == null) {
            chatAdapter = new ChatAdapter(getActivity(), chatMsgList);
        }

        if(IsMsgFromMe(newMessage)) {
            lstViewChat = (ListView) getView().findViewById(R.id.listView_chat_message_me);
        } else {
            lstViewChat = (ListView)getView().findViewById(R.id.listView_chat_message_others);
        }

        lstViewChat.setAdapter(chatAdapter);
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
