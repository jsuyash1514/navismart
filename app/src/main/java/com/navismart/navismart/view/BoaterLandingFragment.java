package com.navismart.navismart.view;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.navismart.navismart.R;
import com.navismart.navismart.adapters.BoaterViewPagerAdapter;

import static com.navismart.navismart.MainActivity.USER_TYPE;
import static com.navismart.navismart.adapters.ChatAdapter.SENDER_BOATER;

public class BoaterLandingFragment extends Fragment {

    private ViewPager boaterViewPager;
    private TabLayout boaterTabLayout;

    private BoaterViewPagerAdapter boaterViewPagerAdapter;

    public BoaterLandingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_boater_landing, container, false);

        boaterTabLayout = view.findViewById(R.id.boater_tabs);
        boaterViewPager = view.findViewById(R.id.boater_view_pager);
        boaterViewPagerAdapter = new BoaterViewPagerAdapter(getChildFragmentManager());
        boaterViewPager.setAdapter(boaterViewPagerAdapter);
        boaterTabLayout.setupWithViewPager(boaterViewPager);

        boaterTabLayout.getTabAt(0).setIcon(R.drawable.ic_search_gray_24dp);
        boaterTabLayout.getTabAt(1).setIcon(R.drawable.ic_port_gray_24dp);
        boaterTabLayout.getTabAt(2).setIcon(R.drawable.ic_message_gray_24dp);
        boaterTabLayout.getTabAt(3).setIcon(R.drawable.ic_account_circle_gray_24dp);

        USER_TYPE = SENDER_BOATER;

        return view;
    }

}
