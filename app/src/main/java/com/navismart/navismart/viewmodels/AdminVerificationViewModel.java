package com.navismart.navismart.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.navismart.navismart.livedata.FirebaseQueryLiveData;

public class AdminVerificationViewModel extends ViewModel {

    private static final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("admin").child("verification");

    private final FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(databaseReference);

    @NonNull
    public LiveData<DataSnapshot> getDataSnapshotLiveData() {
        return liveData;
    }
}
