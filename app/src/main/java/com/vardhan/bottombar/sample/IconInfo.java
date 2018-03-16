package com.vardhan.bottombar.sample;

import android.graphics.drawable.Drawable;

/**
 * Created by Vardhan on 3/6/2018.
 */

public class IconInfo {
    Drawable logo;
    String name;
    String pack;

    public IconInfo(Drawable logo, String name, String pack) {
        this.logo = logo;
        this.name = name;
        this.pack = pack;
    }

    public Drawable getLogo() {
        return logo;
    }

    public void setLogo(Drawable logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }
}
