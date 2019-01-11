package com.navismart.navismart.viewmodelfactory;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.navismart.navismart.viewmodels.ChatViewModel;

public class ChatViewModelFactory implements ViewModelProvider.Factory {

    private String marina_id, boater_id;

    public ChatViewModelFactory(String marina_id, String boater_id) {
        this.marina_id = marina_id;
        this.boater_id = boater_id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {

        return (T) new ChatViewModel(marina_id, boater_id);

    }

}
