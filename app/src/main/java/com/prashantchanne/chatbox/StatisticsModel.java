package com.prashantchanne.chatbox;

public class StatisticsModel {

   private String Attended;
   private String name;
   private String profile;
   private String roll;

    public StatisticsModel(){

        //need empty constructor;

    }

    public StatisticsModel(String attended, String name, String profile, String roll) {
        Attended = attended;
        this.name = name;
        this.profile = profile;
        this.roll = roll;
    }

    public String getAttended() {
        return Attended;
    }

    public String getName() {
        return name;
    }

    public String getProfile() {
        return profile;
    }

    public String getRoll() {
        return roll;
    }
}
