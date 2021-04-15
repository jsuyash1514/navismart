package com.navismart.navismart.model;

public class MarinaActivityNewReviewsCardModel {
    private String guestName, timeStamp;
    private float ratingGiven;

    public MarinaActivityNewReviewsCardModel(String guestName, float ratingGiven, String timeStamp) {
        this.guestName = guestName;
        this.ratingGiven = ratingGiven;
        this.timeStamp = timeStamp;
    }

    public String getGuestName() {
        return guestName;
    }

    public float getRatingGiven() {
        return ratingGiven;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
}
