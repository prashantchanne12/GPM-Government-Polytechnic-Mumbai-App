package com.prashantchanne.chatbox;

public class NotesModel {

    private String title;
    private String url;
    private String name;

    public NotesModel(){
        //need public constructor
    }

    public NotesModel(String title, String url, String name) {
        this.title = title;
        this.url = url;
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }
}
