package com.example.johan.firebase;

import com.firebase.client.Firebase;

/**
 * Created by johan on 1/3/2015.
 */
public class Message {
    String from = "";
    String message = "";
    String time = "";
    String id = "";

    public Message() {}

    public Message(String id, String from, String message, String time) {
        this.from = from;
        this.time = time;
        this.message = message;
        this.id = id;

    }

    public String GetFrom() {
        return this.from;
    }

    public String GetTime() {
        return this.time;
    }

    public String GetId() {
        return id;
    }
    public String SetId(String id) {
        return this.id = id;
    }
}
