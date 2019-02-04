package com.navismart.navismart.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.navismart.navismart.livedata.FirebaseQueryLiveData;

public class MarinaMsgViewModel extends ViewModel implements FirebaseAuth.AuthStateListener {

    private static FirebaseAuth auth = FirebaseAuth.getInstance();
    private static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(auth.getCurrentUser().getUid()).child("chats");
    private FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(databaseReference);


    @NonNull
    public LiveData<DataSnapshot> getDataSnapshotLiveData() {
        return liveData;
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (auth.getCurrentUser() != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(auth.getCurrentUser().getUid()).child("chats");
            Log.d("currentUser UID", auth.getCurrentUser().getUid());
            liveData = new FirebaseQueryLiveData(databaseReference);
        }
    }
}
