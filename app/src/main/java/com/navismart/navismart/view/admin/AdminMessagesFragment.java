package com.navismart.navismart.view.admin;

import android.app.AlertDialog;
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
import com.navismart.navismart.RecyclerItemClickListener;
import com.navismart.navismart.adapters.MsgNameAdapter;
import com.navismart.navismart.model.MsgNameModel;
import com.navismart.navismart.viewmodels.AdminMsgViewModel;

import java.util.ArrayList;

import androidx.navigation.Navigation;

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
                    msgNameModel.setMsgName(snapshot.child("userName").getValue().toString());
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

        msgRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), msgRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("userName", msgNameModelArrayList.get(position).getMsgName());
                bundle.putString("adminID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                bundle.putString("userID", msgNameModelArrayList.get(position).getID());
                Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_adminFragment_to_contactUsFragment, bundle);

            }

            @Override
            public void onLongItemClick(View view, int position) {
                String id = msgNameModelArrayList.get(position).getID();
                msgNameModelArrayList.remove(position);
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Delete chat");
                alert.setMessage("Are you sure you want to delete?");
                alert.setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    // continue with delete
                    databaseReference.child("users").child(auth.getCurrentUser().getUid()).child("contactAdmin").child(id).setValue(null);
                });
                alert.setNegativeButton(android.R.string.no, (dialog, which) -> {
                    // close dialog
                    dialog.cancel();
                });
                alert.show();
            }
        }));

        return view;
    }
}
