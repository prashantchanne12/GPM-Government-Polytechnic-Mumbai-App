package com.prashantchanne.chatbox;

import java.util.List;

public class PresentRollNumberModel {

    List<String> rollnum;

    public PresentRollNumberModel(){

    }

    public PresentRollNumberModel(List<String> rollnum) {
        this.rollnum = rollnum;
    }

    public List<String> getRollnum() {
        return rollnum;
    }
}
