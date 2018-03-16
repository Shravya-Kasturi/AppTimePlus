package com.example.bottombar.sample;

import android.graphics.drawable.Drawable;

public class AppList {

    private String name;
    private String time;
    private String Last;
    private String pack;
    Drawable icon;

    public AppList(String name, String time, String last, Drawable icon, String pack) {
        this.name = name;
        this.time = time;
        Last = last;
        this.icon = icon;
        this.pack=pack;
    }

    public String getName() {
        return name;
    }

    public String getPack() {
        return pack;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLast() {
        return Last;
    }

    public void setLast(String last) {
        Last = last;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}