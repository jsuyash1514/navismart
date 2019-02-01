package com.navismart.navismart.view.admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.navismart.navismart.R;
import com.navismart.navismart.adapters.AdminViewPagerAdapter;

import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

public class AdminFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    FirebaseAuth auth;
    AdminViewPagerAdapter adapter;
    Button logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin, container, false);

        logout = view.findViewById(R.id.admin_logout_button);

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

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Logout");
                alert.setMessage("Are you sure you want to logout?");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        auth.signOut();
                        Toast.makeText(getContext(), "Logged out successfully!", Toast.LENGTH_SHORT).show();
                        NavOptions navOptions = new NavOptions.Builder()
                                .setPopUpTo(R.id.landingFragment, true)
                                .build();
                        Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.admin_log_out_action, null, navOptions);
                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.show();
            }
        });

        return view;
    }

    private void setupTabIcons() {
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
