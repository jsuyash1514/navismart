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
    private MarinaModel marinaModel;
    private String fromDate = "default";
    private String toDate = "default";
    private int bookingTense = 0;

    public BookingModel(MarinaModel marinaModel, String fromDate, String toDate, int bookingTense) {
        this.marinaModel = marinaModel;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.bookingTense = bookingTense;

    }

    public BookingModel(Parcel in) {

    }

    public BookingModel() {

    }

    public int getBookingTense() {

        return bookingTense;
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

    public MarinaModel getMarinaModel() {

        return marinaModel;
    }

    public void setMarinaModel(MarinaModel marinaModel) {
        this.marinaModel = marinaModel;
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
