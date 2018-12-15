package com.navismart.navismart.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

public class MarinaModel {

    private String name = "default";
    private Bitmap image;
    private String price = "0.0";
    private String location = "default";
    private float distFromCity = 0;
    private int rating = 0;
    private boolean freeCancellation = false;

    public MarinaModel() {

        image = Bitmap.createBitmap(150, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawColor(Color.GRAY);

    }

    public MarinaModel(String name, Bitmap image, String price, String location, float distFromCity, int rating, boolean freeCancellation) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.location = location;
        this.distFromCity = distFromCity;
        this.rating = rating;
        this.freeCancellation = freeCancellation;
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
