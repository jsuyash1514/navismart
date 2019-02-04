package com.navismart.navismart.model;

public class MarinaBookingsModel {
    int icon;
    String name, arrivingOn, departingOn;
    String bookingID;
    String boaterID;

    public MarinaBookingsModel(int icon, String name, String arrivingOn, String departingOn) {
        this.icon = icon;
        this.name = name;
        this.arrivingOn = arrivingOn;
        this.departingOn = departingOn;
    }

    public MarinaBookingsModel() {
    }

    public String getBoaterID() {

        return boaterID;
    }

    public void setBoaterID(String boaterID) {
        this.boaterID = boaterID;
    }

    public int getBitmap() {
        return icon;
    }

    public void setBitmap(int icon) {
        this.icon = icon;
    }

    public String getGuestName() {
        return name;
    }

    public void setGuestName(String name) {
        this.name = name;
    }

    public String getArrivingOn() {
        return arrivingOn;
    }

    public void setArrivingOn(String arrivingOn) {
        this.arrivingOn = arrivingOn;
    }

    public String getDepartingOn() {
        return departingOn;
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
