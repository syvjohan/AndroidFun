package com.example.johan.firebase;

/**
 * Created by johan on 1/3/2015.
 */
public class Group {
    String name = "";
    String id = "";

    public Group() {}

    public Group(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String GetName() {
        return this.name;
    }

    public String SetName(String name) {
        return this.name = name;
    }

    public String GetId() {
        return this.id;
    }

    public String SetID(String id) {
        return this.id = id;
    }
}
