package com.prashantchanne.chatbox;

import java.util.List;

public class StudentUsers {

    String name, image, roll, year, userId;

    public StudentUsers(){

    }

    public StudentUsers(String name, String image, String roll,String year, String userId) {
        this.name = name;
        this.image = image;
        this.roll = roll;
        this.year = year;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRoll() {
        return roll;
    }

    public String getYear() {
        return year;
    }

    public String getUserId() {
        return userId;
    }
}
