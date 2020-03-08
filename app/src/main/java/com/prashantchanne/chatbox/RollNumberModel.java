package com.prashantchanne.chatbox;

import java.util.List;

public class RollNumberModel {

    private String dept;
    private String shift;
    private String year;
    private String add_year;
    private String temp_roll;
    private boolean teacher;
    List<String> roll;

    public RollNumberModel(){
        //need public empty constructor
    }

    public RollNumberModel(String dept, String shift, String year, String add_year, String temp_roll, boolean teacher, List<String> roll) {
        this.dept = dept;
        this.shift = shift;
        this.year = year;
        this.add_year = add_year;
        this.temp_roll = temp_roll;
        this.teacher = teacher;
        this.roll = roll;
    }


    public String getDept() {
        return dept;
    }

    public String getShift() {
        return shift;
    }

    public String getYear() {
        return year;
    }

    public String getAdd_year() {
        return add_year;
    }

    public String getTemp_roll() {
        return temp_roll;
    }

    public boolean getTeacher() {
        return teacher;
    }

    public List<String> getRoll() {
        return roll;
    }
}
