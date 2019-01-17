package com.navismart.navismart.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.navismart.navismart.R;

import androidx.navigation.Navigation;

public class BookingsFragment extends Fragment {

    private RelativeLayout pastBookingsLayout;
    private RelativeLayout currentBookingsLayout;
    private RelativeLayout upcomingBookingsLayout;


    public BookingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookings, container, false);

        pastBookingsLayout = view.findViewById(R.id.next_pastBookings);
        currentBookingsLayout = view.findViewById(R.id.next_currentBookings);
        upcomingBookingsLayout = view.findViewById(R.id.next_upcomingBookings);

        pastBookingsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_boaterLandingFragment_to_pastBookingsFragment);
            }
        });

        currentBookingsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_boaterLandingFragment_to_currentBookingsFragment);
            }
        });

        upcomingBookingsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_boaterLandingFragment_to_upcomingBookingsFragment);
            }
        });

        return view;
    }

}
