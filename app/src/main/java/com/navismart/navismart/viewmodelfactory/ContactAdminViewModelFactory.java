package com.navismart.navismart.viewmodelfactory;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.navismart.navismart.viewmodels.ContactAdminViewModel;

public class ContactAdminViewModelFactory implements ViewModelProvider.Factory {

    private String marina_id, boater_id;

    public ContactAdminViewModelFactory(String marina_id, String boater_id) {
        this.marina_id = marina_id;
        this.boater_id = boater_id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {

        return (T) new ContactAdminViewModel(marina_id, boater_id);

    }

}
