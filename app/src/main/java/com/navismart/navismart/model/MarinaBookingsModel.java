package com.navismart.navismart.model;

import android.graphics.Bitmap;

public class MarinaBookingsModel {
    int icon;
    String name, arrivingOn, departingOn;

    public MarinaBookingsModel(int icon, String name, String arrivingOn, String departingOn) {
        this.icon = icon;
        this.name = name;
        this.arrivingOn = arrivingOn;
        this.departingOn = departingOn;
    }

    public int getBitmap() {
        return icon;
    }

    public String getGuestName() {
        return name;
    }

    public String getArrivingOn() {
        return arrivingOn;
    }

    public String getDepartingOn() {
        return departingOn;
    }
}