package com.navismart.navismart.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.navismart.navismart.view.admin.AdminMessagesFragment;
import com.navismart.navismart.view.admin.AdminVerificationFragment;

public class AdminViewPagerAdapter extends FragmentPagerAdapter {
    public AdminViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new AdminVerificationFragment();
        } else if (position == 1) {
            fragment = new AdminMessagesFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0) {
            title = "Verification";
        } else if (position == 1) {
            title = "Messages";
        }
        return title;
    }
}
