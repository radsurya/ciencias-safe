package com.example.cienciassafe;

public class Resource {

    private String mMessage;
    private String date;
    private String date2;

    public Resource(String mMessage, String date, String date2) {
        this.mMessage = mMessage;
        this.date = date;
        this.date2 = date2;
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

    public void setDate2(String date2) {
        this.date2 = date2;
    }
}
