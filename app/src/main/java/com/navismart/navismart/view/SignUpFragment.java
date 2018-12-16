package com.navismart.navismart.view;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.navismart.navismart.R;
import com.navismart.navismart.adapters.SignUpViewPagerAdapter;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class SignUpFragment extends Fragment {

    private SignUpViewPagerAdapter signUpViewPagerAdapter;

    private ViewPager profileViewPager;
    private TabLayout profileTabLayout;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_signup, container, false);

        profileViewPager = view.findViewById(R.id.profile_view_pager);
        signUpViewPagerAdapter = new SignUpViewPagerAdapter(getChildFragmentManager());
        profileViewPager.setAdapter(signUpViewPagerAdapter);
        profileTabLayout = view.findViewById(R.id.profile_tabs);
        profileTabLayout.setupWithViewPager(profileViewPager);



        return view;
    }

}
