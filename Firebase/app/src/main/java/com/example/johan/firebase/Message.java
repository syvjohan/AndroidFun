package com.example.johan.firebase;

import com.firebase.client.Firebase;

/**
 * Created by johan on 1/3/2015.
 */
public class Message {
    String from = "";
    String message = "";
    String time = "";

    public Message(String from, String message, String time) {
        this.from = from;
        this.time = time;
        this.message = message;
    }
}
