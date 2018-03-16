package com.example.bottombar.sample;

/**
 * Created by Vardhan on 1/30/2018.
 */

public class Sett {

    int logo;
    String name;

    public Sett() {
    }

    public Sett(int logo, String name) {
        this.logo = logo;
        this.name = name;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLogo() {
        return logo;
    }

    public String getName() {
        return name;
    }
}
