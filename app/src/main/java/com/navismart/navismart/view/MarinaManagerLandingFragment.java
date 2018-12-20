package com.navismart.navismart.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.navismart.navismart.R;
import com.navismart.navismart.adapters.MarinaLandingViewPagerAdapter;

import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

public class MarinaManagerLandingFragment extends Fragment {
    FirebaseAuth auth;
    TabLayout tabLayout;
    ViewPager viewPager;
    MarinaLandingViewPagerAdapter adapter;

    public MarinaManagerLandingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_marina_manager_landing, container, false);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Toast.makeText(getContext(), "No user found", Toast.LENGTH_SHORT).show();
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.landingFragment, true)
                    .build();
            Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.log_out_action, null, navOptions);
        }

        viewPager = view.findViewById(R.id.marina_manager_view_pager);
        adapter = new MarinaLandingViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout = view.findViewById(R.id.marina_manager_tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();


        /* LOGOUT BUTTON */
//        Button logout = view.findViewById(R.id.logout_button);
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                auth.signOut();
//                Toast.makeText(getContext(), "Logged out Successful", Toast.LENGTH_SHORT).show();
//                NavOptions navOptions = new NavOptions.Builder()
//                        .setPopUpTo(R.id.landingFragment, true)
//                        .build();
//                Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.log_out_action, null, navOptions);
//            }
//        });
        return view;
    }

    private void setupTabIcons(){

        TextView tabOne = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
        tabOne.setText("Activity");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_timeline_black_24dp, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Bookings");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_booking_black_24dp, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
        tabThree.setText("Availability");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_date_range_black_24dp, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabFour = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
        tabFour.setText("Messages");
        tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_message_black_24dp, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabFour);

        TextView tabFive = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
        tabFive.setText("Profile");
        tabFive.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_account_circle_black_24dp, 0, 0);
        tabLayout.getTabAt(4).setCustomView(tabFive);
    }

}
