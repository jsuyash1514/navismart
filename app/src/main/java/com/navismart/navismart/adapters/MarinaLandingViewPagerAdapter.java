package com.navismart.navismart.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.navismart.navismart.view.MarinaLandingActivityFragment;
import com.navismart.navismart.view.MarinaLandingBookingFragment;
import com.navismart.navismart.view.MarinaLandingMessagesFragment;
import com.navismart.navismart.view.MarinaLandingProfileFragment;

public class MarinaLandingViewPagerAdapter extends FragmentPagerAdapter {

    public MarinaLandingViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new MarinaLandingActivityFragment();
        }
        else if (position == 1)
        {
            fragment = new MarinaLandingBookingFragment();
        }
        else if (position == 2)
        {
            fragment = new MarinaLandingMessagesFragment();

        }
        else if (position == 3)
        {
            fragment = new MarinaLandingProfileFragment();

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0) {
            title = "Activity";
        } else if (position == 1) {
            title = "Bookings";
        } else if (position == 2) {
            title = "Messages";
        } else if (position == 3) {
            title = "Profile";
        }
        return title;
    }
}
