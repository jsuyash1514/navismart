package com.navismart.navismart.model;

import android.graphics.Bitmap;

public class MarinaBookingsModel {
    int icon;
    String name, arrivingOn, departingOn;
    String bookingID;

    public MarinaBookingsModel(int icon, String name, String arrivingOn, String departingOn) {
        this.icon = icon;
        this.name = name;
        this.arrivingOn = arrivingOn;
        this.departingOn = departingOn;
    }

    public MarinaBookingsModel() {
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

    public void setBitmap(int icon) {
        this.icon = icon;
    }

    public void setGuestName(String name) {
        this.name = name;
    }

    public void setArrivingOn(String arrivingOn) {
        this.arrivingOn = arrivingOn;
    }

    public void setDepartingOn(String departingOn) {
        this.departingOn = departingOn;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }
}
