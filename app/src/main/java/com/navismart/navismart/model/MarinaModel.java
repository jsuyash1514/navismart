package com.navismart.navismart.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

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
    private ArrayList<String> facilities;
    private int f[];
    private String fromDate = "default";
    private String toDate = "default";

    public MarinaModel() {

        image = Bitmap.createBitmap(150, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawColor(Color.GRAY);
        initializeFacilities();
        f = new int[10];

    }

    public MarinaModel(String name, Bitmap image, String price, String location, float distFromCity, int rating, boolean freeCancellation, String description, String tnc, int f[]) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.location = location;
        this.distFromCity = distFromCity;
        this.rating = rating;
        this.freeCancellation = freeCancellation;
        this.description = description;
        this.tnc = tnc;
        this.f = f;
        initializeFacilities();
    }

    public MarinaModel(String name, Bitmap image, String price, String location, float distFromCity, int rating, boolean freeCancellation, String description, String tnc, int f[], String fromDate, String toDate) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.location = location;
        this.distFromCity = distFromCity;
        this.rating = rating;
        this.freeCancellation = freeCancellation;
        this.description = description;
        this.tnc = tnc;
        this.f = f;
        this.fromDate = fromDate;
        this.toDate = toDate;
        initializeFacilities();
    }

    public MarinaModel(Parcel in) {

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

    public ArrayList<String> getFacilities() {
        return facilities;
    }

    public void setFacilities(int[] f) {
        this.f = f;
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

    private void initializeFacilities() {

        facilities = new ArrayList<>();
        facilities.add("Drinking Water");
        facilities.add("Electricity");
        facilities.add("Fuel Station");
        facilities.add("24/7 Access");
        facilities.add("Travel Lift");
        facilities.add("Security");
        facilities.add("Residual Water Collection");
        facilities.add("Restaurant");
        facilities.add("Dry Port");
        facilities.add("Maintenance");


    }

    public int[] getFacilitiesAvlbl() {
        return f;
    }

    public String getFacilityString() {
        String s = "";
        for (int a : f) {
            s = s.concat(facilities.get(a) + ", ");
        }
        return s.substring(0, s.length() - 2);
    }
}
