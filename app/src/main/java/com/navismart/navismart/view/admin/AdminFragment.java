package com.navismart.navismart.view.admin;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.navismart.navismart.R;
import com.navismart.navismart.adapters.AdminViewPagerAdapter;
import com.navismart.navismart.adapters.MarinaLandingViewPagerAdapter;

import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

public class AdminFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    FirebaseAuth auth;
    AdminViewPagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin, container, false);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Toast.makeText(getContext(), "No user found", Toast.LENGTH_SHORT).show();
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.landingFragment, true)
                    .build();
            Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.admin_log_out_action, null, navOptions);
        }

        viewPager = view.findViewById(R.id.admin_view_pager);
        adapter = new AdminViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout = view.findViewById(R.id.admin_tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        return view;
    }

    private void setupTabIcons(){
        TextView tabOne = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
        tabOne.setText("Verification");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_verification_black_24dp, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Messages");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_message_black_24dp, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);
    }
}