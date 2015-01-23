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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatFragment extends Fragment implements
        View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    static ArrayList<Message> chatMsgList = new ArrayList<>();
    static ArrayList<String> msgKeyValues = new ArrayList<>();
    ChatAdapter chatAdapter = null;
    ListView lstViewChat;

    private String groupId;

    Firebase firebaserootRef = new Firebase("https://luminous-heat-420.firebaseio.com");
    static String username = "Me";
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

        ReadChatMessages(firebaserootRef);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        editMsg = (EditText)  view.findViewById(R.id.txt_message_input);
        btnSend = (Button) view.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        String txtMsg = editMsg.getText().toString();
        if (txtMsg != "") {
            System.out.println("GOTO CreateNewMessage!");
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
        if (groupId != "") {
            Firebase firebaseParentMsg = firebaserootRef.child(GetGroupId()).child("messages");
            Firebase firebaseMsg = firebaseParentMsg.push();

            Message cm = new Message(id, from, msg, time);

            id = firebaseMsg.getKey();
            firebaseMsg.child("from").setValue(cm.GetFrom());
            firebaseMsg.child("message").setValue(cm.GetMsg());
            firebaseMsg.child("time").setValue(cm.GetTime());

            chatMessages.put(id,cm);
            System.out.println("Succesfuly created a new message.");
        }

        ReadChatMessages(firebaserootRef);
    }

    public void ReadChatMessages(Firebase firebaseRootRef) {
        firebaseRootRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String s) {
                if (snapshot.child(GetGroupId()).child("messages").getChildren() != null) {
                    for (DataSnapshot c : snapshot.child("messages").getChildren()) {
                        String key = c.getKey();

                        Message newMessage = new Message();
                        newMessage.SetFrom((String) c.child("from").getValue());
                        newMessage.SetMsg((String) c.child("message").getValue());
                        newMessage.SetTime((String) c.child("time").getValue());
                        newMessage.SetId((String) c.child("id").getValue());

                        if (!msgKeyValues.contains(key)) {
                            msgKeyValues.add(key);

                            AddToLstViewChat(newMessage);

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

    public boolean IsMsgFromMe(Message message) {
        boolean isSenderMe = username.equals((CharSequence) message.GetFrom());
        return isSenderMe;
    }

    public void AddToLstViewChat(Message newMessage) {
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

    public String GetGroupId() {
        System.out.println(groupId);
        return groupId;
    }

    public void SetGroupID(String groupId) {
        this.groupId = groupId;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}
