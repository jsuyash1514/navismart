package com.navismart.navismart.view.boater;


import android.app.AlertDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.navismart.navismart.R;
import com.navismart.navismart.RecyclerItemClickListener;
import com.navismart.navismart.adapters.MsgNameAdapter;
import com.navismart.navismart.model.MsgNameModel;
import com.navismart.navismart.viewmodels.BoaterMsgViewModel;

import java.util.ArrayList;

import androidx.navigation.Navigation;

public class BoaterMessageFragment extends Fragment {

    private RecyclerView msgRecyclerView;
    private ArrayList<MsgNameModel> msgNameModelArrayList;
    private TextView noMsgTextView;
    private Button contactUsButton;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    public BoaterMessageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_boater_message, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        msgRecyclerView = view.findViewById(R.id.marinaMessageRecyclerView);
        noMsgTextView = view.findViewById(R.id.no_msg_text);
        contactUsButton = view.findViewById(R.id.contact_us_button);

        BoaterMsgViewModel marinaMsgViewModel = ViewModelProviders.of(this).get(BoaterMsgViewModel.class);
        LiveData<DataSnapshot> liveData = marinaMsgViewModel.getDataSnapshotLiveData();
        liveData.observe(this, dataSnapshot -> {
            Log.d("TAGTAGTAG", dataSnapshot.toString());
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

        msgRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), msgRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

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
                    databaseReference.child("users").child(auth.getCurrentUser().getUid()).child("chats").child(id).setValue(null);
                });
                alert.setNegativeButton(android.R.string.no, (dialog, which) -> {
                    // close dialog
                    dialog.cancel();
                });
                alert.show();
            }
        }));

        contactUsButton.setOnClickListener(v -> databaseReference.child("admin").child("uid").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("userName", auth.getCurrentUser().getDisplayName());
                    bundle.putString("userID", auth.getCurrentUser().getUid());
                    bundle.putString("adminID", dataSnapshot.getValue().toString());
                    Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_boaterLandingFragment_to_contactUsFragment, bundle);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }));

        return view;
    }

}
