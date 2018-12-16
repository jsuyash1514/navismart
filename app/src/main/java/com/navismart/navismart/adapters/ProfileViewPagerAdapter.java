package com.navismart.navismart.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.navismart.navismart.view.BoaterRegisterFragment;
import com.navismart.navismart.view.MarinaManagerRegisterFragment;
import com.navismart.navismart.view.ThirdPartyRegisterFragment;

public class ProfileViewPagerAdapter extends FragmentPagerAdapter {
    public ProfileViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new MarinaManagerRegisterFragment();
        } else if (position == 1) {
            fragment = new BoaterRegisterFragment();
        } else if (position == 2) {
            fragment = new ThirdPartyRegisterFragment();
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
            title = "Marina Manager";
        } else if (position == 1) {
            title = "Boater";
        } else if (position == 2) {
            title = "Third-Party Service Provider";
        }
        return title;
    }
}



