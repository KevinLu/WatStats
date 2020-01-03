package com.kevinlu.watstats.models;

//Class for date (category) for listing transactions under appropriate headings
public class Date {
    int id;
    String date;

    public Date(int id, String date) {
        this.id = id;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }
}
