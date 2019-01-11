package com.navismart.navismart.view;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.navismart.navismart.R;
import com.navismart.navismart.adapters.MsgNameAdapter;
import com.navismart.navismart.model.MsgNameModel;
import com.navismart.navismart.viewmodels.MsgViewModel;

import java.util.ArrayList;

public class MarinaLandingMessagesFragment extends Fragment {

    private RecyclerView msgRecyclerView;
    private ArrayList<MsgNameModel> msgNameModelArrayList;

    public MarinaLandingMessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_marina_landing_messages, container, false);

        msgRecyclerView = view.findViewById(R.id.boaterMessageRecyclerView);

        MsgViewModel marinaMsgViewModel = ViewModelProviders.of(this).get(MsgViewModel.class);
        LiveData<DataSnapshot> liveData = marinaMsgViewModel.getDataSnapshotLiveData();
        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    msgNameModelArrayList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        MsgNameModel msgNameModel = new MsgNameModel();
                        msgNameModel.setID(snapshot.getKey());
                        msgNameModel.setMsgName(snapshot.child("boaterName").getValue().toString());
                        msgNameModelArrayList.add(msgNameModel);
                    }
                    MsgNameAdapter msgNameListAdapter = new MsgNameAdapter(getActivity(), msgNameModelArrayList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                    msgRecyclerView.setLayoutManager(mLayoutManager);
                    msgRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    msgRecyclerView.setAdapter(msgNameListAdapter);
                }
            }
        });

        return view;
    }
}
