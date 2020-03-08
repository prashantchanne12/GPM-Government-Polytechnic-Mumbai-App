package com.prashantchanne.chatbox;

import android.support.annotation.NonNull;

public class UserId {

    public String UserId;

    public <T extends UserId> T withId(@NonNull final String id){
        this.UserId = id;
        return (T) this;
    }

}
