package com.navismart.navismart.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

public class MarinaModel implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MarinaModel createFromParcel(Parcel in) {
            return new MarinaModel(in);
        }

        public MarinaModel[] newArray(int size) {
            return new MarinaModel[size];
        }
    };
    private String name = "default";
    private Bitmap image;
    private String price = "0.0";
    private String location = "default";
    private float distFromCity = 0;
    private int rating = 0;
    private boolean freeCancellation = false;
    private String description = "default";
    private String tnc = "default";
    private String facilities = "default";

    public MarinaModel() {

        image = Bitmap.createBitmap(150, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawColor(Color.GRAY);

    }

    public MarinaModel(String name, Bitmap image, String price, String location, float distFromCity, int rating, boolean freeCancellation, String description, String tnc, String facilities) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.location = location;
        this.distFromCity = distFromCity;
        this.rating = rating;
        this.freeCancellation = freeCancellation;
        this.description = description;
        this.tnc = tnc;
        this.facilities = facilities;
    }

    public MarinaModel(Parcel in) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTnc() {
        return tnc;
    }

    public void setTnc(String tnc) {
        this.tnc = tnc;
    }

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {


    }

    public boolean isFreeCancellation() {

        return freeCancellation;
    }

    public void setFreeCancellation(boolean freeCancellation) {
        this.freeCancellation = freeCancellation;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public float getDistFromCity() {
        return distFromCity;
    }

    public void setDistFromCity(float distFromCity) {
        this.distFromCity = distFromCity;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
