package com.example.cienciassafe;

public class Resource {

    private String mMessage;
    private String date;

    public Resource(String mMessage, String date) {
        this.mMessage = mMessage;
        this.date = date;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
