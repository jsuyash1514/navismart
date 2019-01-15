package com.navismart.navismart.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.navismart.navismart.livedata.FirebaseQueryLiveData;

public class ReviewListViewModel extends ViewModel {

    private static DatabaseReference databaseReference;
    private FirebaseQueryLiveData liveData;

    public ReviewListViewModel(String marina_id) {

        super();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(marina_id).child("review");
        liveData = new FirebaseQueryLiveData(databaseReference);

    }


    @NonNull
    public LiveData<DataSnapshot> getDataSnapshotLiveData() {
        return liveData;
    }

}
