package com.example.johan.firebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import com.example.johan.firebase.ChatFragment;

/**
 * Created by johan on 1/19/2015.
 */
public class ChatAdapter extends ArrayAdapter<Message> {
    public ChatAdapter(Context context, ArrayList<Message> messages) {
        super(context, 0, messages);
    }

    public boolean IsMsgFromMe(Message message) {
        boolean isSenderMe = ChatFragment.username.equals((CharSequence) message.GetFrom());
        return isSenderMe;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Message message = ChatFragment.chatMsgList.get(position);

        //Check who has sent the message me or someone else...
        if (IsMsgFromMe(message)) {
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_chat_me, parent, false);
            }

            TextView chatFrom = (TextView) convertView.findViewById(R.id.txt_message_me_from);
            TextView chatMessage = (TextView) convertView.findViewById(R.id.txt_message_me_message);
            TextView chatTime = (TextView) convertView.findViewById(R.id.txt_message_me_time);

            // Populate the data into the template view using the data object
            chatFrom.setText("From: " + (CharSequence) message.GetFrom());
            chatMessage.setText((CharSequence) message.GetMsg());
            chatTime.setText("Date: " + (CharSequence) message.GetTime());

        } else {
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_chat_others, parent, false);
            }

            TextView chatFrom = (TextView) convertView.findViewById(R.id.txt_message_others_from);
            TextView chatMessage = (TextView) convertView.findViewById(R.id.txt_message_others_message);
            TextView chatTime = (TextView) convertView.findViewById(R.id.txt_message_others_time);

            // Populate the data into the template view using the data object
            chatFrom.setText("From: " + (CharSequence) message.GetFrom());
            chatMessage.setText((CharSequence) message.GetMsg());
            chatTime.setText("Time: " + (CharSequence) message.GetTime());;
        }

        // Return the completed view to render on screen
        return convertView;
    }
}

