package com.navismart.navismart.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.navismart.navismart.R;
import com.navismart.navismart.adapters.BoatListAdapter;
import com.navismart.navismart.model.BoatModel;

import java.util.ArrayList;
import java.util.List;

import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

public class BoaterProfileFragment extends Fragment {

    List<BoatModel> list;
    private RecyclerView boatListRecyclerView;
    private ImageView logoutIcon;
    FirebaseAuth auth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_boater_profile, container, false);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Toast.makeText(getContext(), "No user found", Toast.LENGTH_SHORT).show();
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.boaterLandingFragment, true)
                    .build();
            Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.boaterLogoutAction, null, navOptions);
        }

        prepareBoatList();

        BoatListAdapter boatListAdapter = new BoatListAdapter(list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());

        boatListRecyclerView = view.findViewById(R.id.boat_recycler_view);
        boatListRecyclerView.setLayoutManager(mLayoutManager);
        boatListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        boatListRecyclerView.setAdapter(boatListAdapter);

        logoutIcon = view.findViewById(R.id.logout_icon);
        logoutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Toast.makeText(getContext(), "Logged out Successful", Toast.LENGTH_SHORT).show();
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.boaterLandingFragment, true)
                        .build();
                Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.boaterLogoutAction, null, navOptions);
            }
        });

        return view;
    }

    private void prepareBoatList() {

        list = new ArrayList<>();
        list.add(new BoatModel());
        list.add(new BoatModel());
        list.add(new BoatModel());
        list.add(new BoatModel());

    }

}
