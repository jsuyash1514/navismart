package com.navismart.navismart.view.boater;


import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.navismart.navismart.R;
import com.navismart.navismart.adapters.BoatListAdapter;
import com.navismart.navismart.model.BoatModel;
import com.navismart.navismart.utils.EmailAndPasswordChecker;
import com.navismart.navismart.viewmodels.BoatListViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

public class BoaterProfileFragment extends Fragment {

    private List<BoatModel> list;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private RecyclerView boatListRecyclerView;
    private ImageView addBoatIcon, profileImageView;
    private TextView nameTextView;
    private TextView emailTextView;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Dialog credentialVerifyDialog;
    private String verifyEmail, verifyPass;
    private Button verifyButton, editProfileButton, logoutButton;

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

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();


        nameTextView = view.findViewById(R.id.boater_profile_name);
        emailTextView = view.findViewById(R.id.boater_profile_email);
        profileImageView = view.findViewById(R.id.boater_profile_image);
        editProfileButton = view.findViewById(R.id.edit_profile_icon);

        boatListRecyclerView = view.findViewById(R.id.boat_recycler_view);

        credentialVerifyDialog = new Dialog(getContext());
        credentialVerifyDialog.setContentView(R.layout.credentials_dialog);
        credentialVerifyDialog.setTitle("Verify your EmailID and password");
        emailEditText = credentialVerifyDialog.findViewById(R.id.email_edit_text);
        passwordEditText = credentialVerifyDialog.findViewById(R.id.password_edit_text);

        prepareBoatList();

        logoutButton = view.findViewById(R.id.logout_icon);
        logoutButton.setOnClickListener((View v) -> {

            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle("Logout");
            alert.setMessage("Are you sure you want to logout?");
            alert.setPositiveButton(android.R.string.yes, (dialog, which) -> {
                auth.signOut();
                Toast.makeText(getContext(), "Logged out Successful", Toast.LENGTH_SHORT).show();
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.boaterLandingFragment, true)
                        .build();
                Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.boaterLogoutAction, null, navOptions);
            });
            alert.setNegativeButton(android.R.string.no, (dialog, which) -> {
                // close dialog
                dialog.cancel();
            });
            alert.show();
        });

        addBoatIcon = view.findViewById(R.id.add_boat_icon);
        addBoatIcon.setOnClickListener((View v) -> Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_boaterLandingFragment_to_addBoatFragment));

        editProfileButton.setOnClickListener((View v) -> credentialVerifyDialog.show());

        verifyButton = credentialVerifyDialog.findViewById(R.id.verify_button);
        verifyButton.setOnClickListener((View v) -> {

            verifyEmail = emailEditText.getText().toString();
            verifyPass = passwordEditText.getText().toString();
            if (verifyEmail != null && verifyPass != null && !verifyEmail.trim().isEmpty() && !verifyPass.trim().isEmpty()) {
                AuthCredential credential = EmailAuthProvider.getCredential(verifyEmail, verifyPass);
                auth.getCurrentUser().reauthenticate(credential)
                        .addOnSuccessListener(aVoid -> {
                            Dialog newCredDialog = new Dialog(getContext());
                            newCredDialog.setContentView(R.layout.new_credentials_dialog);
                            Button changeButton = newCredDialog.findViewById(R.id.change_button);
                            changeButton.setOnClickListener((View v1) -> {
                                String newEmail = ((EditText) newCredDialog.findViewById(R.id.email_edit_text)).getText().toString();
                                String newPass = ((EditText) newCredDialog.findViewById(R.id.password_edit_text)).getText().toString();
                                if (newEmail != null && newPass != null && !newEmail.trim().isEmpty() && !newPass.trim().isEmpty() && EmailAndPasswordChecker.isEmailValid(newEmail) && EmailAndPasswordChecker.isPasswordValid(newPass)) {

                                    auth.getCurrentUser().updateEmail(newEmail).addOnSuccessListener(aVoid1 -> Toast.makeText(getContext(), "Email updated successfully", Toast.LENGTH_SHORT).show());
                                    auth.getCurrentUser().updatePassword(newPass).addOnSuccessListener(aVoid12 -> {
                                        Toast.makeText(getContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                                        newCredDialog.dismiss();
                                        credentialVerifyDialog.dismiss();
                                    });

                                } else {
                                    Toast.makeText(getContext(), "Unable to update. Enter valid Email and Password.", Toast.LENGTH_SHORT).show();
                                    emailEditText.setText("");
                                    passwordEditText.setText("");
                                    emailEditText.requestFocus();
                                }

                            });
                            newCredDialog.show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getContext(), "Wrong credentials entered! Sign in again.", Toast.LENGTH_SHORT).show();
                            credentialVerifyDialog.dismiss();
                            auth.signOut();
                            Toast.makeText(getContext(), "Logged out Successful", Toast.LENGTH_SHORT).show();
                            NavOptions navOptions = new NavOptions.Builder()
                                    .setPopUpTo(R.id.boaterLandingFragment, true)
                                    .build();
                            Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.boaterLogoutAction, null, navOptions);
                        });
            }

        });


        loadDataToViews();

        return view;
    }

    private void loadDataToViews() {

        DatabaseReference currentUser = databaseReference.child("users").child(auth.getCurrentUser().getUid());

        currentUser.child("profile").child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nameTextView.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        currentUser.child("profile").child("email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                emailTextView.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        StorageReference profilePicRef = storageReference.child("users").child(auth.getCurrentUser().getUid()).child("profile");
        profilePicRef.getDownloadUrl().addOnSuccessListener(uri -> {

            Log.d("URI", uri.toString());
            Glide.with(getContext())
                    .load(uri)
                    .into(profileImageView);

        });


    }

    private void prepareBoatList() {

        BoatListViewModel boatListViewModel = ViewModelProviders.of(this).get(BoatListViewModel.class);
        LiveData<DataSnapshot> liveData = boatListViewModel.getDataSnapshotLiveData();
        liveData.observe(this, dataSnapshot -> {
            if (dataSnapshot != null) {
                list = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    BoatModel boat = postSnapshot.getValue(BoatModel.class);
                    list.add(boat);
                }
                BoatListAdapter boatListAdapter = new BoatListAdapter(list, getContext());
                boatListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                boatListRecyclerView.setItemAnimator(new DefaultItemAnimator());
                boatListRecyclerView.setAdapter(boatListAdapter);
            }
        });

    }

}
