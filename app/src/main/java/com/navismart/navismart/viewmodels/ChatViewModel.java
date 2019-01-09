package com.navismart.navismart.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.navismart.navismart.livedata.FirebaseQueryLiveData;

public class ChatViewModel extends ViewModel {

    private static DatabaseReference databaseReference;
    private FirebaseQueryLiveData liveData;

    public ChatViewModel(String marina_id, String boater_id) {

        super();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("chats").child(marina_id).child(boater_id);
        liveData = new FirebaseQueryLiveData(databaseReference);

    }


    @NonNull
    public LiveData<DataSnapshot> getDataSnapshotLiveData() {
        return liveData;
    }


}
