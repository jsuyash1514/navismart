package com.navismart.navismart.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;

public class SignUpViewModel extends ViewModel {
    MutableLiveData<Bitmap> marinaManagerProfilePic;

    public MutableLiveData<Bitmap> getMarinaManagerProfilePic() {
        if(marinaManagerProfilePic == null){
            marinaManagerProfilePic = new MutableLiveData<Bitmap>();
        }
        return marinaManagerProfilePic;
    }

}
