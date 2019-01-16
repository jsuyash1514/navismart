package com.navismart.navismart.model;

public class MarinaActivityNewReviewsCardModel {
    private String guestName, timeStamp;
    private long ratingGiven;

    public MarinaActivityNewReviewsCardModel(String guestName, long ratingGiven, String timeStamp) {
        this.guestName = guestName;
        this.ratingGiven = ratingGiven;
        this.timeStamp = timeStamp;
    }

    public String getGuestName() {
        return guestName;
    }

    public long getRatingGiven() {
        return ratingGiven;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
}
