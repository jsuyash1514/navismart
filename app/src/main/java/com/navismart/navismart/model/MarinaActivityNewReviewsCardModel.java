package com.navismart.navismart.model;

public class MarinaActivityNewReviewsCardModel {
    private String guestName, ratingGiven, timeStamp;

    public MarinaActivityNewReviewsCardModel(String guestName, String ratingGiven, String timeStamp) {
        this.guestName = guestName;
        this.ratingGiven = ratingGiven;
        this.timeStamp = timeStamp;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRatingGiven() {
        return ratingGiven;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
}
