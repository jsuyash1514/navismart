package com.navismart.navismart.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.navismart.navismart.livedata.FirebaseQueryLiveData;

public class ContactAdminViewModel extends ViewModel {

    private static DatabaseReference databaseReference;
    private FirebaseQueryLiveData liveData;
    FirebaseAuth auth;

    public ContactAdminViewModel(String rec_id, String send_id) {
        super();
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(send_id).child("contactAdmin").child(rec_id);
        liveData = new FirebaseQueryLiveData(databaseReference);

    }


    @NonNull
    public LiveData<DataSnapshot> getDataSnapshotLiveData() {
        return liveData;
    }


}
