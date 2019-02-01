package com.navismart.navismart.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ReviewModel implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ReviewModel createFromParcel(Parcel in) {
            return new ReviewModel(in);
        }

        public ReviewModel[] newArray(int size) {
            return new ReviewModel[size];
        }
    };

    private String review = "";
    private String reply = "";
    private String reviewerName = "";
    private String reviewDate = "";
    private String starRating = "";
    private String bookingID = "";
    private String reviewerID = "";
    private String marinaID = "";
    private String reviewID = "";
    private long timeStamp;

    public ReviewModel() {
    }

    public ReviewModel(Parcel in) {
    }

    public String getReviewID() {
        return reviewID;
    }

    public void setReviewID(String reviewID) {
        this.reviewID = reviewID;
    }

    public String getMarinaID() {
        return marinaID;
    }

    public void setMarinaID(String marinaID) {
        this.marinaID = marinaID;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getReviewerID() {

        return reviewerID;
    }

    public void setReviewerID(String reviewerID) {
        this.reviewerID = reviewerID;
    }

    public String getBookingID() {

        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public String getReview() {

        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getStarRating() {
        return starRating;
    }

    public void setStarRating(String starRating) {
        this.starRating = starRating;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    @Override
    public int describeContents() {
        return 0;
    }
}
