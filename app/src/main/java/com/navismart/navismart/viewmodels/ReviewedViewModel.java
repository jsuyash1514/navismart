package com.navismart.navismart.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.navismart.navismart.livedata.FirebaseQueryLiveData;

public class ReviewedViewModel extends ViewModel {

    private static FirebaseAuth auth = FirebaseAuth.getInstance();
    private static DatabaseReference databaseReference;
    private FirebaseQueryLiveData liveData;

    public ReviewedViewModel(String bookingID) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(auth.getCurrentUser().getUid()).child("bookings").child(bookingID);
        liveData = new FirebaseQueryLiveData(databaseReference);
    }

    @NonNull
    public LiveData<DataSnapshot> getDataSnapshotLiveData() {
        return liveData;
    }

}
