package com.navismart.navismart.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.navismart.navismart.livedata.FirebaseQueryLiveData;

public class BoatListViewModel extends ViewModel {

    private static FirebaseAuth auth = FirebaseAuth.getInstance();
    private static final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(auth.getCurrentUser().getUid()).child("boats");

    private final FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(databaseReference);

    @NonNull
    public LiveData<DataSnapshot> getDataSnapshotLiveData() {
        return liveData;
    }

}
