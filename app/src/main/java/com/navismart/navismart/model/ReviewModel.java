package com.navismart.navismart.model;

public class ReviewModel {

    private String review;
    private String reviewerName;
    private String reviewDate;
    private int starRating;
    private String bookingID;
    private String reviewerID;

    public ReviewModel() {
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

    public int getStarRating() {
        return starRating;
    }

    public void setStarRating(int starRating) {
        this.starRating = starRating;
    }
}
