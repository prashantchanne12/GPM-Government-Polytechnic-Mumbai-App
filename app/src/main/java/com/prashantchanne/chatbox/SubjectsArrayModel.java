package com.prashantchanne.chatbox;

import java.util.List;

public class SubjectsArrayModel {

    List<String> sub;

    public SubjectsArrayModel(){
        //needed empty constructor
    }

    public SubjectsArrayModel(List<String> sub) {
        this.sub = sub;
    }

    public List<String> getSub() {
        return sub;
    }
}
