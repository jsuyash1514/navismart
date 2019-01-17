package com.navismart.navismart.view;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.navismart.navismart.R;
import com.navismart.navismart.model.BoatModel;

import androidx.navigation.Navigation;

public class AddBoatFragment extends Fragment {

    private EditText nameEditText;
    private EditText idEditText;
    private EditText typeEditText;
    private EditText lengthEditText;
    private EditText beamEditText;
    private Button uploadRegistrationButton;
    private Button addBoatButton;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;

    public AddBoatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_boat, container, false);

        nameEditText = view.findViewById(R.id.boat_name_edit_text);
        idEditText = view.findViewById(R.id.boat_id_edit_text);
        lengthEditText = view.findViewById(R.id.boat_length_edit_text);
        typeEditText = view.findViewById(R.id.boat_type_edit_text);
        beamEditText = view.findViewById(R.id.boat_beam_edit_text);
        uploadRegistrationButton = view.findViewById(R.id.upload_registration_button);
        addBoatButton = view.findViewById(R.id.add_boat_button);
        progressDialog = new ProgressDialog(getContext());

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        addBoatButton.setOnClickListener((View v) -> addBoat());

        return view;
    }

    public void addBoat() {

        final String boatName = nameEditText.getText().toString().trim();
        final String boatID = idEditText.getText().toString().trim();
        final String boatLength = lengthEditText.getText().toString().trim();
        final String boatBeam = beamEditText.getText().toString().trim();
        final String boatType = typeEditText.getText().toString().trim();

        progressDialog.setMessage("Adding Boat...");
        progressDialog.show();

        DatabaseReference currentUser = databaseReference.child("users").child(firebaseAuth.getCurrentUser().getUid());

        if (!boatID.isEmpty() && !boatName.isEmpty()) {

//            currentUser.child("boats").child("ID " + boatID).child("boat-name").setValue(boatName).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(getContext(), "Unable to add new Boat.", Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
//                    Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigateUp();
//                }
//            });
//            currentUser.child("boats").child("ID " + boatID).child("boat-ID").setValue(boatID).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(getContext(), "Unable to add new Boat.", Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
//                    Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigateUp();
//                }
//            });
//            if (!TextUtils.isEmpty(boatLength)) {
//                currentUser.child("boats").child("ID " + boatID).child("boat-length").setValue(boatLength).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getContext(), "Unable to add new Boat.", Toast.LENGTH_SHORT).show();
//                        progressDialog.dismiss();
//                        Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigateUp();
//                    }
//                });
//            }
//            if (!TextUtils.isEmpty(boatBeam)) {
//                currentUser.child("boats").child("ID " + boatID).child("boat-beam").setValue(boatBeam).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getContext(), "Unable to add new Boat.", Toast.LENGTH_SHORT).show();
//                        progressDialog.dismiss();
//                        Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigateUp();
//                    }
//                });
//            }
//            if (!TextUtils.isEmpty(boatType)) {
//                currentUser.child("boats").child("ID " + boatID).child("boat-type").setValue(boatType).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getContext(), "Unable to add new Boat.", Toast.LENGTH_SHORT).show();
//                        progressDialog.dismiss();
//                        Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigateUp();
//                    }
//                });
//            }
            currentUser.child("boats").child("ID " + boatID).setValue(new BoatModel(boatName, boatID, Float.parseFloat(boatLength), Float.parseFloat(boatBeam), boatType)).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Unable to add new Boat.", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigateUp();
                }
            });
            progressDialog.dismiss();
            Toast.makeText(getContext(), "Successfully added new Boat!", Toast.LENGTH_LONG).show();
            Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigateUp();
        } else {


            progressDialog.dismiss();
            if (boatName.isEmpty()) {
                Toast.makeText(getContext(), "Enter Boat Name", Toast.LENGTH_SHORT).show();
                nameEditText.setText("");
                nameEditText.requestFocus();
            } else {
                Toast.makeText(getContext(), "Enter Boat ID", Toast.LENGTH_SHORT).show();
                idEditText.setText("");
                idEditText.requestFocus();
            }

        }


    }

}
