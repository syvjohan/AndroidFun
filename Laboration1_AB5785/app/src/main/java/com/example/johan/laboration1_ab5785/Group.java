package com.example.johan.laboration1_ab5785;

/**
 * Created by johan on 2014-12-07.
 */
public class Group {
    String name;
    String id;
    public Group(){
    }
    public Group(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
}
