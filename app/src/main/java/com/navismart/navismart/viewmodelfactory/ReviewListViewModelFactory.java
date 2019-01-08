package com.navismart.navismart.viewmodelfactory;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.navismart.navismart.viewmodels.ReviewListViewModel;

public class ReviewListViewModelFactory implements ViewModelProvider.Factory {

    private String marina_id;

    public ReviewListViewModelFactory(String param) {
        marina_id = param;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {

        return (T) new ReviewListViewModel(marina_id);

    }

}
