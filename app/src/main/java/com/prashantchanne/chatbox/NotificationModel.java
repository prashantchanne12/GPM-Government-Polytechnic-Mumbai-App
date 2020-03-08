package com.prashantchanne.chatbox;


public class NotificationModel {

    private String from,title;


    public NotificationModel(){
        //need empty constructor
    }

    public NotificationModel(String from, String title) {
        this.from = from;
        this.title = title;

    }

    public String getFrom() {
        return from;
    }

    public String getTitle() {
        return title;
    }
}
