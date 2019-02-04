package com.navismart.navismart.model;

public class MarinaActivityNewBookingsCardModel {
    String guestName, timeStamp, boatName, boatID, arrivalTime, dapartureTime, bookingNumber, noOfDocksBooked, status = "";

    public MarinaActivityNewBookingsCardModel(String guestName, String timeStamp, String boatName, String boatID, String arrivalTime, String dapartureTime, String bookingNumber, String noOfDocksBooked, String status) {
        this.guestName = guestName;
        this.timeStamp = timeStamp;
        this.boatName = boatName;
        this.boatID = boatID;
        this.arrivalTime = arrivalTime;
        this.dapartureTime = dapartureTime;
        this.bookingNumber = bookingNumber;
        this.noOfDocksBooked = noOfDocksBooked;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getBoatName() {
        return boatName;
    }

    public String getBoatID() {
        return boatID;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getDapartureTime() {
        return dapartureTime;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public String getNoOfDocksBooked() {
        return noOfDocksBooked;
    }
}
