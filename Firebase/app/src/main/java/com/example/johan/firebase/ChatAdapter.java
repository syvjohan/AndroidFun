package com.example.johan.firebase;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


import org.w3c.dom.Text;

public class ChatAdapter extends ArrayAdapter<Message> {

    private ArrayList<Message> lstMessages;
    private LayoutInflater layoutInflater;

    public ChatAdapter(Context context, ArrayList<Message> messages) {
        super(context, 0, messages);
        this.lstMessages = messages;
        layoutInflater = LayoutInflater.from(getContext());
    }

    public int IsMsgFromMe(Message message) {
        boolean isSenderMe = ChatFragment.username.equals(message.GetFrom());
        if (isSenderMe) {
            return 0;
        }
        return 1;
    }

    @Override
    public Message getItem(int position) {
        return lstMessages.get(position);
    }

    @Override
    public int getCount() {
        return lstMessages.size();
    }

    //Number of layouts
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = lstMessages.get(position);
        int sender = IsMsgFromMe(message);
        return sender;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        int sender = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();

            //Check who has sent the message me or someone else...
            switch(sender) {
                case 0:
                    convertView = layoutInflater.inflate(R.layout.row_chat_me, parent, false);

                    holder.chatFrom = (TextView) convertView.findViewById(R.id.txt_message_me_from);
                    holder.chatMessage = (TextView) convertView.findViewById(R.id.txt_message_me_message);
                    holder.chatTime = (TextView) convertView.findViewById(R.id.txt_message_me_time);
                    break;
                case 1:
                    convertView = layoutInflater.inflate(R.layout.row_chat_others, parent, false);

                    holder.chatFrom = (TextView) convertView.findViewById(R.id.txt_message_others_from);
                    holder.chatMessage = (TextView) convertView.findViewById(R.id.txt_message_others_message);
                    holder.chatTime = (TextView) convertView.findViewById(R.id.txt_message_others_time);
                    break;
            }
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the data item for this position
        Message message = lstMessages.get(position);

        // Populate the data into the template view using the data object
        holder.chatFrom.setText("From: " + message.GetFrom());
        holder.chatMessage.setText(message.GetMsg());
        holder.chatTime.setText("Date: " + message.GetTime());

        // Return the completed view to render on screen
        return convertView;
    }

    public static class ViewHolder {
        public TextView chatFrom;
        public TextView chatMessage;
        public TextView chatTime;
    }
}
