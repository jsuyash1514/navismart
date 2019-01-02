package com.navismart.navismart.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.navismart.navismart.model.MarinaPicModel;

import java.util.ArrayList;
import java.util.List;

public class SignUpViewModel extends ViewModel {
    MutableLiveData<Uri> marinaManagerProfilePic;
    MutableLiveData<Uri> boaterProfilePic;
    MutableLiveData<List<MarinaPicModel>> marinaPicList;

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MutableLiveData<List<MarinaPicModel>> getMarinaPicList() {
        if(marinaPicList==null){
            marinaPicList = new MutableLiveData<>();
            List<MarinaPicModel> list = new ArrayList<MarinaPicModel>();
            marinaPicList.setValue(list);
        }
        return marinaPicList;
    }
}
