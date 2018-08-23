package com.wise.bottombar.sample;

import android.graphics.drawable.Drawable;

public class AppSelectView {
    String name;
    Drawable icon;
    String pck;

    public AppSelectView(String name, Drawable icon, String pck) {
        this.name = name;
        this.icon = icon;
        this.pck = pck;
    }

    public String getPck() {

        return pck;
    }

    public void setPck(String pck) {
        this.pck = pck;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public AppSelectView(String name, Drawable icon) {
        this.name = name;
        this.icon = icon;
    }

    public AppSelectView() {
    }
}
