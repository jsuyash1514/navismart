package com.navismart.navismart.model;

import android.os.Parcel;
import android.os.Parcelable;

public class BookingModel implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public BookingModel createFromParcel(Parcel in) {
            return new BookingModel(in);
        }

        public BookingModel[] newArray(int size) {
            return new BookingModel[size];
        }
    };
    public final static int PAST = 0;
    public final static int CURRENT = 1;
    public final static int UPCOMING = 2;
    private String boatName = "default";
    private String boatID = "default";
    private String bookingID = "default";
    private String marinaUID = "default";
    private String boaterUID = "default";
    private String marinaName = "default";
    private String boaterName = "default";
    private String fromDate = "default";
    private String toDate = "default";
    private String dateTimeStamp = "default";
    private long bookingTimeStamp;
    private int bookingTense = 0;
    private int noOfDocks = 0;
    private float finalPrice = 0;

    public BookingModel(String boatName, String marinaName, String boatID, String fromDate, String toDate, int bookingTense, String boaterName) {
        this.boatName = boatName;
        this.marinaName = marinaName;
        this.boatID = boatID;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.bookingTense = bookingTense;
        this.boaterName = boaterName;

    }

    public BookingModel(String boatName, String marinaName, String boatID, String fromDate, String toDate, int bookingTense, String boaterName, float finalPrice) {
        this.boatName = boatName;
        this.marinaName = marinaName;
        this.boatID = boatID;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.bookingTense = bookingTense;
        this.boaterName = boaterName;
        this.finalPrice = finalPrice;

    }

    public BookingModel(Parcel in) {

    }

    public BookingModel() {

    }

    public int getNoOfDocks() {

        return noOfDocks;
    }

    public void setNoOfDocks(int noOfDocks) {
        this.noOfDocks = noOfDocks;
    }

    public long getBookingDate() {

        return bookingTimeStamp;
    }

    public void setBookingDate(long bookingTimeStamp) {
        this.bookingTimeStamp = bookingTimeStamp;
    }

    public String getBoaterUID() {
        return boaterUID;
    }

    public void setBoaterUID(String boaterUID) {
        this.boaterUID = boaterUID;
    }

    public String getMarinaUID() {

        return marinaUID;
    }

    public void setMarinaUID(String marinaUID) {
        this.marinaUID = marinaUID;
    }

    public String getBookingID() {

        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public String getBoatName() {

        return boatName;
    }

    public void setBoatName(String boatName) {
        this.boatName = boatName;
    }

    public String getBoatID() {
        return boatID;
    }

    public void setBoatID(String boatID) {
        this.boatID = boatID;
    }

    public String getMarinaName() {
        return marinaName;
    }

    public void setMarinaName(String marinaName) {
        this.marinaName = marinaName;
    }

    public float getFinalPrice() {

        return finalPrice;
    }

    public void setFinalPrice(float finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getBoaterName() {

        return boaterName;
    }

    public void setBoaterName(String boaterName) {
        this.boaterName = boaterName;
    }


    public int getBookingTense() {

        return bookingTense;
    }


    public String getDateTimeStamp() {
        return dateTimeStamp;
    }

    public void setDateTimeStamp(String dateTimeStamp) {
        this.dateTimeStamp = dateTimeStamp;
    }

    public void setBookingTense(int bookingTense) {
        this.bookingTense = bookingTense;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}
