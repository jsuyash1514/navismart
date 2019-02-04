package com.navismart.navismart.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.navismart.navismart.livedata.FirebaseQueryLiveData;

public class MarinaLandingBookingViewModel extends ViewModel {
    int y=0,m=0,d=0,capacity=0;
    private static FirebaseAuth auth = FirebaseAuth.getInstance();
    private static final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("bookings").child(auth.getCurrentUser().getUid());

    private final FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(databaseReference);

    @NonNull
    public LiveData<DataSnapshot> getDataSnapshotLiveData() {
        return liveData;
    }

    public int getYear() {
        return y;
    }

    public void setYear(int y) {
        this.y = y;
    }

    public int getMonth() {
        return m;
    }

    public void setMonth(int m) {
        this.m = m;
    }

    public int getDay() {
        return d;
    }

    public void setDay(int d) {
        this.d = d;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
