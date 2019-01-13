package com.navismart.navismart.view;


import android.app.Dialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.navismart.navismart.EmailAndPasswordChecker;
import com.navismart.navismart.R;
import com.navismart.navismart.adapters.BoatListAdapter;
import com.navismart.navismart.model.BoatModel;
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
    private ImageView logoutIcon, addBoatIcon, profileImageView, editProfileIcon;
    private TextView nameTextView;
    private TextView emailTextView;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Dialog credentialVerifyDialog;
    private String verifyEmail, verifyPass;
    private Button verifyButton;

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
        editProfileIcon = view.findViewById(R.id.edit_profile_icon);

        boatListRecyclerView = view.findViewById(R.id.boat_recycler_view);

        credentialVerifyDialog = new Dialog(getContext());
        credentialVerifyDialog.setContentView(R.layout.credentials_dialog);
        credentialVerifyDialog.setTitle("Verify your EmailID and password");
        emailEditText = credentialVerifyDialog.findViewById(R.id.email_edit_text);
        passwordEditText = credentialVerifyDialog.findViewById(R.id.password_edit_text);

        prepareBoatList();

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

        addBoatIcon = view.findViewById(R.id.add_boat_icon);
        addBoatIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_boaterLandingFragment_to_addBoatFragment);
            }
        });

        editProfileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                credentialVerifyDialog.show();
            }
        });

        verifyButton = credentialVerifyDialog.findViewById(R.id.verify_button);
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyEmail = emailEditText.getText().toString();
                verifyPass = passwordEditText.getText().toString();
                if (verifyEmail != null && verifyPass != null && !verifyEmail.trim().isEmpty() && !verifyPass.trim().isEmpty()) {
                    AuthCredential credential = EmailAuthProvider.getCredential(verifyEmail, verifyPass);
                    auth.getCurrentUser().reauthenticate(credential)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Dialog newCredDialog = new Dialog(getContext());
                                    newCredDialog.setContentView(R.layout.new_credentials_dialog);
                                    Button changeButton = newCredDialog.findViewById(R.id.change_button);
                                    changeButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String newEmail = ((EditText) newCredDialog.findViewById(R.id.email_edit_text)).getText().toString();
                                            String newPass = ((EditText) newCredDialog.findViewById(R.id.password_edit_text)).getText().toString();
                                            if (newEmail != null && newPass != null && !newEmail.trim().isEmpty() && !newPass.trim().isEmpty() && EmailAndPasswordChecker.isEmailValid(newEmail) && EmailAndPasswordChecker.isPasswordValid(newPass)) {

                                                auth.getCurrentUser().updateEmail(newEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getContext(), "Email updated successfully", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                auth.getCurrentUser().updatePassword(newPass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                                                        newCredDialog.dismiss();
                                                        credentialVerifyDialog.dismiss();
                                                    }
                                                });

                                            } else {
                                                Toast.makeText(getContext(), "Unable to update. Enter valid Email and Password.", Toast.LENGTH_SHORT).show();
                                                emailEditText.setText("");
                                                passwordEditText.setText("");
                                                emailEditText.requestFocus();
                                            }
                                        }
                                    });
                                    newCredDialog.show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Wrong credentials entered! Sign in again.", Toast.LENGTH_SHORT).show();
                                    credentialVerifyDialog.dismiss();
                                    auth.signOut();
                                    Toast.makeText(getContext(), "Logged out Successful", Toast.LENGTH_SHORT).show();
                                    NavOptions navOptions = new NavOptions.Builder()
                                            .setPopUpTo(R.id.boaterLandingFragment, true)
                                            .build();
                                    Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.boaterLogoutAction, null, navOptions);
                                }
                            });
                }
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
        profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Log.d("URI", uri.toString());
                Glide.with(getContext())
                        .load(uri)
                        .into(profileImageView);

            }
        });


    }

    private void prepareBoatList() {

        BoatListViewModel boatListViewModel = ViewModelProviders.of(this).get(BoatListViewModel.class);
        LiveData<DataSnapshot> liveData = boatListViewModel.getDataSnapshotLiveData();
        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    list = new ArrayList<>();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        BoatModel boat = postSnapshot.getValue(BoatModel.class);
                        list.add(boat);
                    }
                    BoatListAdapter boatListAdapter = new BoatListAdapter(list, getContext());
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                    boatListRecyclerView.setLayoutManager(mLayoutManager);
                    boatListRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    boatListRecyclerView.setAdapter(boatListAdapter);
                }
            }
        });

    }

}
