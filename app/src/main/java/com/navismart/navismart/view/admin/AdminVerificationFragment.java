package com.navismart.navismart.view.admin;

import android.app.ProgressDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.navismart.navismart.R;
import com.navismart.navismart.adapters.AdminVerificationAdapter;
import com.navismart.navismart.model.AdminVerificationModel;
import com.navismart.navismart.viewmodels.AdminVerificationViewModel;

import java.util.ArrayList;
import java.util.List;

public class AdminVerificationFragment extends Fragment {
    private AdminVerificationViewModel viewModel;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_verification, container, false);

        viewModel = ViewModelProviders.of(this).get(AdminVerificationViewModel.class);
        progressDialog = new ProgressDialog(getContext());
        recyclerView = view.findViewById(R.id.admin_verification_recycler_view);


        LiveData<DataSnapshot> liveData = viewModel.getDataSnapshotLiveData();
        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    final List<AdminVerificationModel> list = new ArrayList<>();
                    final AdminVerificationAdapter adapter = new AdminVerificationAdapter(getActivity(),list);
                    progressDialog.setMessage("Fetching data...");
                    progressDialog.show();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        if(snapshot!=null) {
                            AdminVerificationModel verificationModel = new AdminVerificationModel();
                            verificationModel.setMarinaName(snapshot.child("marina-description").child("marinaName").getValue(String.class));
                            verificationModel.setMarinaManagerName(snapshot.child("profile").child("name").getValue(String.class));
                            verificationModel.setEmail(snapshot.child("profile").child("email").getValue(String.class));
                            verificationModel.setReceptionCapacity(snapshot.child("marina-description").child("capacity").getValue(String.class));
                            verificationModel.setLocationAddress(snapshot.child("marina-description").child("locationAddress").getValue(String.class));
                            String description = snapshot.child("marina-description").child("description").getValue(String.class);
                            if(description != null && !description.isEmpty()) verificationModel.setDescription(description);
                            else verificationModel.setDescription("Not Uploaded");
                            String tnc = snapshot.child("marina-description").child("terms-and-condition").getValue(String.class);
                            if(tnc != null && !tnc.isEmpty()) verificationModel.setTermsAndCondition(tnc);
                            else verificationModel.setTermsAndCondition("Not Uploaded");
                            list.add(verificationModel);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();

                    RecyclerView.LayoutManager recycler = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(recycler);
                    recyclerView.setAdapter(adapter);
                }
                else{
                    Log.d("AdminVerification","Null datasnapshot");
                }
            }
        });
        return view;
    }
}
