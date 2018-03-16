package com.vardhan.bottombar.sample;


public class EachAppItem {
    int logo;

    public String getBad() {
        return bad;
    }

    public void setBad(String bad) {
        this.bad = bad;
    }

    public EachAppItem(int logo, String name, String time, String bad) {
        this.logo = logo;
        this.name = name;
        this.time = time;
        this.bad = bad;

    }

    public EachAppItem() {
    }

    String name,time,bad;

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
