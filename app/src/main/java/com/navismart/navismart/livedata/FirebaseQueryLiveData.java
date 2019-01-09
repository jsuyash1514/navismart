package com.navismart.navismart.livedata;

import android.arch.lifecycle.LiveData;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FirebaseQueryLiveData extends LiveData<DataSnapshot> {

    public static final String LOG_TAG = "FirebaseQueryLiveData";
    private final Handler handler = new Handler();
    private final Query query;
    private final MyValueEventListener listener = new MyValueEventListener();
    private boolean listenerRemovalPending = false;
    private final Runnable removeListener = new Runnable() {
        @Override
        public void run() {
            query.removeEventListener(listener);
            listenerRemovalPending = false;
        }
    };

    public FirebaseQueryLiveData(DatabaseReference ref) {

        this.query = ref;

    }

    @Override
    protected void onActive() {
        if (listenerRemovalPending) {
            handler.removeCallbacks(removeListener);
        } else {
            query.addValueEventListener(listener);
        }
        listenerRemovalPending = false;
    }

    @Override
    protected void onInactive() {
        //Listener removal is scheduled on a two second delay
        handler.postDelayed(removeListener, 2000);
        listenerRemovalPending = true;
    }

    private class MyValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(dataSnapshot);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(LOG_TAG, "Can't listen to query " + query, databaseError.toException());
        }
    }

}

