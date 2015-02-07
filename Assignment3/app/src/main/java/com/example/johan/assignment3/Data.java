package com.example.johan.assignment3;

/**
 * Created by johan on 2/7/2015.
 */
public class Data {
    String id = "";
    String date = "";
    String amount = "";
    String title = "";

    public Data() {}

    public Data(String id, String date, String amount, String title) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.title = title;
    }

    public String GetId() {return this.id; }
    public void SetId(String id) {this.id = id; }

    public String GetDate() {return this.date; }
    public void SetDate(String date) {this.date = date; }

    public String GetAmount() {return this.amount; }
    public void SetAmount(String amount) {this.amount = amount; }

    public String GetTitle() {return this.title; }
    public void SetTitle(String title) {this.title = title; }
}
