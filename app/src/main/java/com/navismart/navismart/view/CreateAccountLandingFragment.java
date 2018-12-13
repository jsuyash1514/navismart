package com.navismart.navismart.view;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.navismart.navismart.R;
import com.navismart.navismart.adapters.ProfileViewPagerAdapter;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class CreateAccountLandingFragment extends Fragment {

    private ProfileViewPagerAdapter profileViewPagerAdapter;
    private ViewPager profileViewPager;
    private TabLayout profileTabLayout;
    static NavController navControllerAcctLanding;

    public CreateAccountLandingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_create_account_landing, container, false);

        profileViewPager = view.findViewById(R.id.profile_view_pager);
        profileViewPagerAdapter = new ProfileViewPagerAdapter(getChildFragmentManager());
        profileViewPager.setAdapter(profileViewPagerAdapter);
        profileTabLayout = view.findViewById(R.id.profile_tabs);
        profileTabLayout.setupWithViewPager(profileViewPager);

        navControllerAcctLanding = Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment);


        return view;
    }

}
