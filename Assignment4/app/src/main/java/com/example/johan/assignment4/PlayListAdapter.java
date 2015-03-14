package com.example.johan.assignment4;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PlayListAdapter extends ArrayAdapter<Song> {
    private ArrayList<Song> arrListSong;
    private LayoutInflater layoutInflater;

    public PlayListAdapter(Context context, ArrayList<Song> songs) {
        super(context, 0, songs);
        this.arrListSong = songs;
        layoutInflater = LayoutInflater.from(getContext());
    }

    @Override
    public Song getItem(int position) {
        return arrListSong.get(position);
    }

    @Override
    public int getCount() {
        return arrListSong.size();
    }

    //Number of layouts
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Resources res = parent.getResources();

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.play_list_row, parent, false);

            holder.songArtist = (TextView) convertView.findViewById(R.id.txt_play_list_artist);
            holder.songTitle = (TextView) convertView.findViewById(R.id.txt_play_list_title);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the data item for this position
        Song song = arrListSong.get(position);

        // Populate the data into the template view using the data object
        holder.songArtist.setText(song.getArtist());
        holder.songTitle.setText(song.getTitle());

        if (position % 2 == 1) {
            convertView.setBackgroundResource(R.mipmap.rowodd);
        } else {
            convertView.setBackgroundResource(R.mipmap.roweven);
        }

        // Return the completed view to render on screen
        return convertView;
    }

    public static class ViewHolder {
        public TextView songTitle;
        public TextView songArtist;
    }
}
