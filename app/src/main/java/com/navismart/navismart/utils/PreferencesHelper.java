package com.navismart.navismart.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper {

    private static SharedPreferences sharedPreferences;

    public PreferencesHelper(Context context) {

        sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);

    }

    public void storeToken(String token) {
        sharedPreferences.edit().putString("token", token).apply();
    }

    public String getToken() {
        return sharedPreferences.getString("token", "");
    }

    public void clearPrefs() {

        sharedPreferences.edit().clear().apply();

    }

}
