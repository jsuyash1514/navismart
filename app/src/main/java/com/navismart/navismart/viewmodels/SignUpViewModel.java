package com.navismart.navismart.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;
import android.net.Uri;

public class SignUpViewModel extends ViewModel {
    MutableLiveData<Uri> marinaManagerProfilePic;

    public MutableLiveData<Uri> getMarinaManagerProfilePic() {
        if(marinaManagerProfilePic == null){
            marinaManagerProfilePic = new MutableLiveData<Uri>();
        }
        return marinaManagerProfilePic;
    }

}
