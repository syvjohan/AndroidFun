package com.example.johan.firebase;

import android.app.Activity;

import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;

import android.util.Log;
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

import org.apache.http.impl.client.DefaultTargetAuthenticationHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    static ArrayList<Message> chatMsgList = new ArrayList<>();
    ChatAdapter chatAdapter;
    ListView lstViewChat;
    private String temp;
    private String groupId;
    static String username;

    EditText editMsg;


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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        editMsg = (EditText)  view.findViewById(R.id.txt_message_input);
        Button  btnSend = (Button) view.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtMsg = editMsg.getText().toString();
                if (!txtMsg.isEmpty()) {
                    System.out.println("GOTO CreateNewMessage!");
                    CreateNewMessage(txtMsg);
                    editMsg.setText("");
                }
            }
        });

        ReadChatMessages();

        return view;
    }



    public void  CreateNewMessage(String textMsg) {
        Firebase firebaserootRef = new Firebase("https://luminous-heat-420.firebaseio.com");

        Map<String, Message> chatMessages = new HashMap<>();

        String msg = textMsg;
        String from = username;
        String time = TimeStamp();
        String id = "";

        //Message
        if (groupId != "") {
            Firebase firebaseParentMsg = firebaserootRef.child(GetGroupId()).child("messages");
            Firebase firebaseMsg = firebaseParentMsg.push();

            Message cm = new Message(id, from, msg, time);

            id = firebaseMsg.getKey();
            firebaseMsg.child("from").setValue(cm.GetFrom());
            firebaseMsg.child("message").setValue(cm.GetMsg());
            firebaseMsg.child("time").setValue(cm.GetTime());

            chatMessages.put(id,cm);
            System.out.println("Succesfully created a new message.");

            ReadChatMessages();
        }
    }

    public void ReadChatMessages() {
        Firebase firebaserootRef = new Firebase("https://luminous-heat-420.firebaseio.com");
        firebaserootRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String s) {
                if (!(snapshot.child(GetGroupId()).child("messages").exists())) {
                    for (DataSnapshot c : snapshot.child("messages").getChildren()) {
                        if (snapshot.getKey().equals(GetGroupId())) {

                            Message newMessage = new Message();
                            newMessage.SetFrom((String) c.child("from").getValue());
                            newMessage.SetMsg((String) c.child("message").getValue());
                            newMessage.SetTime((String) c.child("time").getValue());
                            newMessage.SetId((String) c.getKey());

                            //Check if GetGroupId value has been changed.
                            if (temp == GetGroupId()) {
                                AddToLstViewChat(newMessage);
                            } else {
                                chatMsgList.clear();
                                temp = GetGroupId();
                                AddToLstViewChat(newMessage);
                            }

                            //Automatic scrolls to last line in listView.
                            lstViewChat.setSelection(chatAdapter.getCount() -1);
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

    //Check if ArrayList contains the message.
    public boolean ComapareId(Message msg, ArrayList<Message> lst) {
        for (Message m : lst) {
            if (msg.GetId() == m.GetId()) {
                return true;
            }
        }
        return false;
    }

    //Check who has sent the received message.
    public boolean IsMsgFromMe(Message message) {
        boolean isSenderMe = username.equals((CharSequence) message.GetFrom());
        return isSenderMe;
    }

    public void AddToLstViewChat(Message newMessage) {
        if (newMessage != null) {
            if (!ComapareId(newMessage, chatMsgList)) {
                  chatMsgList.add(newMessage);
            }

            if (chatAdapter == null) {
                chatAdapter = new ChatAdapter(getActivity(), chatMsgList);
            }

            try {
                  lstViewChat = (ListView) getView().findViewById(R.id.listView_chat);
            } catch (NullPointerException e) {
                System.out.println("lstView_chat_message_me/others is null in method AddtoLstViewChat in ChatFragment class." + e.getMessage());
            }

            lstViewChat.setAdapter(chatAdapter);
            chatAdapter.notifyDataSetChanged();
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

    public String GetGroupId() {
        return groupId;
    }

    //Receives the group id from ChatFragment (ChangeToChatFragment(String groupId)).
    public void SetGroupID(String groupId) {
        this.groupId = groupId;
    }

    public void GetUsername(String username) {
        this.username = username;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public String TimeStamp() {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        return timeStamp;
    }
}
