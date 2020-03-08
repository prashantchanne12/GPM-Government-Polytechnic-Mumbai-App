package com.prashantchanne.chatbox;

import java.util.List;

public class RollModel {

    List<String> roll;


    public RollModel(){

    }

    public RollModel(List<String> roll) {
        this.roll = roll;
    }


    public List<String> getRoll() {
        return roll;
    }

}
