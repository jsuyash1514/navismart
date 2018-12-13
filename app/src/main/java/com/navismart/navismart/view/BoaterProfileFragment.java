package com.navismart.navismart.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.navismart.navismart.R;
import com.navismart.navismart.adapters.BoatListAdapter;
import com.navismart.navismart.model.BoatModel;

import java.util.ArrayList;
import java.util.List;

public class BoaterProfileFragment extends Fragment {

    List<BoatModel> list;
    private RecyclerView boatListRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_boater_profile, container, false);

        prepareBoatList();

        BoatListAdapter boatListAdapter = new BoatListAdapter(list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());

        boatListRecyclerView = view.findViewById(R.id.boat_recycler_view);
        boatListRecyclerView.setLayoutManager(mLayoutManager);
        boatListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        boatListRecyclerView.setAdapter(boatListAdapter);

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
