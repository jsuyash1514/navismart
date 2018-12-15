package com.navismart.navismart.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.net.Uri;

public class SignUpViewModel extends ViewModel {
    MutableLiveData<Uri> marinaManagerProfilePic;
    MutableLiveData<Uri> boaterProfilePic;

    public MutableLiveData<Uri> getMarinaManagerProfilePic() {
        if(marinaManagerProfilePic == null){
            marinaManagerProfilePic = new MutableLiveData<Uri>();
        }
        return marinaManagerProfilePic;
    }

    public MutableLiveData<Uri> getBoaterProfilePic() {
        if(boaterProfilePic == null){
            boaterProfilePic = new MutableLiveData<Uri>();
        }
        return boaterProfilePic;
    }


}
