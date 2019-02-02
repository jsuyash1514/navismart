package com.navismart.navismart.view.marina;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.navismart.navismart.R;
import com.navismart.navismart.viewmodels.MarinaDescriptionViewModel;

import java.util.ArrayList;

import androidx.navigation.Navigation;

public class MarinaDescriptionEditFragment extends Fragment {

    private EditText marinaNameEditText, descriptionEditText, tNcEditText;
    private CheckBox drinkingWater;
    private CheckBox electricity;
    private CheckBox fuelStation;
    private CheckBox access;
    private CheckBox travelLift;
    private CheckBox security;
    private CheckBox residualWaterCollection;
    private CheckBox restaurant;
    private CheckBox dryPort;
    private CheckBox maintenance;
    private NumberPicker capacityPicker;
    private Button editButton;
    private ArrayList<Integer> f;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    public MarinaDescriptionEditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_marina_description_edit, container, false);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        marinaNameEditText = view.findViewById(R.id.marina_name_edit_text);
        descriptionEditText = view.findViewById(R.id.description_edit_text);
        tNcEditText = view.findViewById(R.id.tnc_edit_text);
        drinkingWater = view.findViewById(R.id.check_box_0);
        electricity = view.findViewById(R.id.check_box_1);
        fuelStation = view.findViewById(R.id.check_box_2);
        access = view.findViewById(R.id.check_box_3);
        travelLift = view.findViewById(R.id.check_box_4);
        security = view.findViewById(R.id.check_box_5);
        residualWaterCollection = view.findViewById(R.id.check_box_6);
        restaurant = view.findViewById(R.id.check_box_7);
        dryPort = view.findViewById(R.id.check_box_8);
        maintenance = view.findViewById(R.id.check_box_9);
        capacityPicker = view.findViewById(R.id.reception_capacity_number_picker);
        editButton = view.findViewById(R.id.edit_button);

        capacityPicker.setMinValue(1);
        capacityPicker.setMaxValue(10);

        MarinaDescriptionViewModel marinaDescriptionViewModel = ViewModelProviders.of(this).get(MarinaDescriptionViewModel.class);
        LiveData<DataSnapshot> liveData = marinaDescriptionViewModel.getDataSnapshotLiveData();
        liveData.observe(this, dataSnapshot -> {
            capacityPicker.setValue(Integer.parseInt((String) dataSnapshot.child("capacity").getValue()));
            marinaNameEditText.setText((String) dataSnapshot.child("marinaName").getValue());
            descriptionEditText.setText((String) dataSnapshot.child("description").getValue());
            tNcEditText.setText((String) dataSnapshot.child("terms-and-condition").getValue());

            drinkingWater.setChecked(false);
            electricity.setChecked(false);
            fuelStation.setChecked(false);
            access.setChecked(false);
            travelLift.setChecked(false);
            security.setChecked(false);
            residualWaterCollection.setChecked(false);
            restaurant.setChecked(false);
            dryPort.setChecked(false);
            maintenance.setChecked(false);

            for (DataSnapshot snapshot : dataSnapshot.child("facilities").getChildren()) {
                switch (((Long) snapshot.getValue()).intValue()) {
                    case 0:
                        drinkingWater.setChecked(true);
                        break;
                    case 1:
                        electricity.setChecked(true);
                        break;
                    case 2:
                        fuelStation.setChecked(true);
                        break;
                    case 3:
                        access.setChecked(true);
                        break;
                    case 4:
                        travelLift.setChecked(true);
                        break;
                    case 5:
                        security.setChecked(true);
                        break;
                    case 6:
                        residualWaterCollection.setChecked(true);
                        break;
                    case 7:
                        restaurant.setChecked(true);
                        break;
                    case 8:
                        dryPort.setChecked(true);
                        break;
                    case 9:
                        maintenance.setChecked(true);
                        break;
                }
            }
        });

        editButton.setOnClickListener((View v) -> {

            if (!marinaNameEditText.getText().toString().trim().isEmpty()) {
                editProfile();
            } else {
                marinaNameEditText.requestFocus();
                Toast.makeText(getContext(), "Enter marina name!", Toast.LENGTH_SHORT).show();
            }

        });

        return view;
    }

    private void editProfile() {

        DatabaseReference descriptionReference = databaseReference.child("users").child(auth.getCurrentUser().getUid()).child("marina-description");

        f = new ArrayList<>();
        if (drinkingWater.isChecked()) f.add(0);
        if (electricity.isChecked()) f.add(1);
        if (fuelStation.isChecked()) f.add(2);
        if (access.isChecked()) f.add(3);
        if (travelLift.isChecked()) f.add(4);
        if (security.isChecked()) f.add(5);
        if (residualWaterCollection.isChecked()) f.add(6);
        if (restaurant.isChecked()) f.add(7);
        if (dryPort.isChecked()) f.add(8);
        if (maintenance.isChecked()) f.add(9);

        FirebaseUser user = auth.getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(marinaNameEditText.getText().toString()).build();
        user.updateProfile(profileUpdates);

        descriptionReference.child("facilities").setValue(f);
        descriptionReference.child("marinaName").setValue(marinaNameEditText.getText().toString().trim());
        descriptionReference.child("description").setValue(descriptionEditText.getText().toString().trim());
        descriptionReference.child("terms-and-condition").setValue(tNcEditText.getText().toString().trim())
                .addOnSuccessListener(aVoid -> {
                    Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigateUp();
                    Toast.makeText(getContext(), "Description edited successfully!", Toast.LENGTH_SHORT);
                });


    }

}
