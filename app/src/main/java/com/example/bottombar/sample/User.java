package com.example.bottombar.sample;

import java.util.ArrayList;
import java.util.HashMap;


public class User {
    public String uid;
    public String fullname;
    public String username;
    HashMap<String,String> friends;
    HashMap<String,String> requests;
    ArrayList<String> times;
    ArrayList<String> badges;
    String sunday;


    public String getSunday() {
        return sunday;
    }

    public void setSunday(String sunday) {
        this.sunday = sunday;
    }

    public User(String uid, String fullname, String username, HashMap<String, String> friends, HashMap<String, String> requests, ArrayList<String> times, ArrayList<String> badges, String sunday) {
        this.uid = uid;
        this.fullname = fullname;
        this.username = username;
        this.friends = friends;
        this.requests = requests;
        this.times = times;
        this.badges=badges;
        this.sunday=sunday;

    }

    public User() {
    }

    public String getUid() {

        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public HashMap<String, String> getFriends() {
        return friends;
    }

    public void setFriends(HashMap<String, String> friends) {
        this.friends = friends;
    }

    public HashMap<String, String> getRequests() {
        return requests;
    }

    public void setRequests(HashMap<String, String> requests) {
        this.requests = requests;
    }

    public ArrayList<String> getTimes() {
        return times;
    }

    public ArrayList<String> getBadges() {
        return badges;
    }

    public void setBadges(ArrayList<String> badges) {
        this.badges = badges;
    }

    public void setTimes(ArrayList<String> times) {
        this.times = times;
    }
}