package com.navismart.navismart.viewmodelfactory;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.navismart.navismart.viewmodels.ReviewedViewModel;

public class BookingViewModelFactory implements ViewModelProvider.Factory {

    private String bookingID;

    public BookingViewModelFactory(String bookingID) {
        this.bookingID = bookingID;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {

        return (T) new ReviewedViewModel(bookingID);

    }

}
