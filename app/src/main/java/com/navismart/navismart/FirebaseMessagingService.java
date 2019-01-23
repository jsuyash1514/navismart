package com.navismart.navismart;

import com.google.firebase.messaging.RemoteMessage;
import com.navismart.navismart.utils.PreferencesHelper;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private PreferencesHelper preferencesHelper;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        preferencesHelper = new PreferencesHelper(getApplicationContext());
        preferencesHelper.storeToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }

}
