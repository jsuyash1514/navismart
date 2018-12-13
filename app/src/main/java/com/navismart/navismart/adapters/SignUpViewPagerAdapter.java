package com.navismart.navismart.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.navismart.navismart.view.SignUpBoaterFragment;
import com.navismart.navismart.view.SignUpMarinaManagerFragment;
import com.navismart.navismart.view.SignupThirdPartyFragment;

public class SignUpViewPagerAdapter extends FragmentPagerAdapter {
    public SignUpViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new SignUpMarinaManagerFragment();
        }
        else if (position == 1)
        {
            fragment = new SignUpBoaterFragment();
        }
        else if (position == 2)
        {
            fragment = new SignupThirdPartyFragment();
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
        if (position == 0)
        {
            title = "Marina Manager";
        }
        else if (position == 1)
        {
            title = "Boater";
        }
        else if (position == 2)
        {
            title = "Third-Party Service Provider";
        }
        return title;
    }
}



