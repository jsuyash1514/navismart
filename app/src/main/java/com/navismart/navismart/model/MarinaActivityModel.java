package com.navismart.navismart.model;

public class MarinaActivityModel {
    String date;
    int type;
    MarinaActivityNewBookingsCardModel bookingsCardModel;
    MarinaActivityNewReviewsCardModel reviewsCardModel;
    public static final int TYPE_DATE = 0;
    public static final int TYPE_BOOKING = 1;
    public static final int TYPE_REVIEW = 2;

    public MarinaActivityModel(int type,String date) {
        this.date = date;
        this.type = type;
    }

    public MarinaActivityModel(int type, MarinaActivityNewBookingsCardModel bookingsCardModel) {
        this.type = type;
        this.bookingsCardModel = bookingsCardModel;
    }

    public MarinaActivityModel(int type, MarinaActivityNewReviewsCardModel reviewsCardModel) {
        this.type = type;
        this.reviewsCardModel = reviewsCardModel;
    }

    public String getDate() {
        return date;
    }

    public int getType() {
        return type;
    }

    public MarinaActivityNewBookingsCardModel getBookingsCardModel() {
        return bookingsCardModel;
    }

    public MarinaActivityNewReviewsCardModel getReviewsCardModel() {
        return reviewsCardModel;
    }
}
