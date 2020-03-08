package com.prashantchanne.chatbox;

import java.util.Date;

public class NotificationUsers {

    public String doc,from,message;
    public Date timeStamp;



    public NotificationUsers(){

    }

    public NotificationUsers(String doc, String from, String message) {
        this.doc = doc;
        this.from = from;
        this.message = message;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }



    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public String getDoc() {
        return doc;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public NotificationUsers(Date timeStamp) {

        this.timeStamp = timeStamp;
    }


}
