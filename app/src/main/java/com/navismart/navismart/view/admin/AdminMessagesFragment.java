package com.navismart.navismart.view.admin;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.navismart.navismart.R;
import com.navismart.navismart.adapters.MsgNameAdapter;
import com.navismart.navismart.model.MsgNameModel;
import com.navismart.navismart.viewmodels.AdminMsgViewModel;

import java.util.ArrayList;

public class AdminMessagesFragment extends Fragment {

    private RecyclerView msgRecyclerView;
    private ArrayList<MsgNameModel> msgNameModelArrayList;
    private TextView noMsgTextView;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_messages, container, false);

        msgRecyclerView = view.findViewById(R.id.adminMessageRecyclerView);
        noMsgTextView = view.findViewById(R.id.no_msg_text);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        AdminMsgViewModel adminMsgViewModel = ViewModelProviders.of(this).get(AdminMsgViewModel.class);
        adminMsgViewModel.getDataSnapshotLiveData().observe(this, dataSnapshot -> {

            if (dataSnapshot != null) {
                msgNameModelArrayList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MsgNameModel msgNameModel = new MsgNameModel();
                    msgNameModel.setID(snapshot.getKey());
                    msgNameModel.setMsgName(snapshot.child("marinaName").getValue().toString());
                    msgNameModelArrayList.add(msgNameModel);
                }
                if (msgNameModelArrayList.size() > 0) {
                    MsgNameAdapter msgNameListAdapter = new MsgNameAdapter(getActivity(), msgNameModelArrayList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                    msgRecyclerView.setLayoutManager(mLayoutManager);
                    msgRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    msgRecyclerView.setAdapter(msgNameListAdapter);
                    noMsgTextView.setVisibility(View.GONE);
                    msgRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    noMsgTextView.setVisibility(View.VISIBLE);
                    msgRecyclerView.setVisibility(View.GONE);
                }
            }

        });

        return view;
    }
}
