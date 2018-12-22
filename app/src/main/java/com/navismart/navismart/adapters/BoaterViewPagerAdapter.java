package com.navismart.navismart.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.navismart.navismart.view.BoaterProfileFragment;
import com.navismart.navismart.view.BoaterSearchFragment;
import com.navismart.navismart.view.BookingsFragment;

public class BoaterViewPagerAdapter extends FragmentPagerAdapter {
    public BoaterViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new BoaterSearchFragment();
        }
        else if (position == 1)
        {
            fragment = new BookingsFragment();
        }
        else if (position == 2)
        {
            fragment = new BoaterProfileFragment();

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0) {
            title = "Search";
        } else if (position == 1) {
            title = "Bookings";
        } else if (position == 2) {
            title = "Profile";
        }
        return title;
    }
}
