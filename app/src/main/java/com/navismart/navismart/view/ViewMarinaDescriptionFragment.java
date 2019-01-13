package com.navismart.navismart.view;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.navismart.navismart.R;
import com.navismart.navismart.viewmodels.MarinaDescriptionViewModel;

import androidx.navigation.Navigation;

public class ViewMarinaDescriptionFragment extends Fragment {

    private TextView nameView, locationView, descriptionView, facilitiesView, tNcView;
    private ImageView marinaImageView, editDescriptionIcon;

    public ViewMarinaDescriptionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_marina_description, container, false);

        nameView = view.findViewById(R.id.marina_name_textView);
        locationView = view.findViewById(R.id.location_display_textView);
        descriptionView = view.findViewById(R.id.description_display_textView);
        facilitiesView = view.findViewById(R.id.facilities_display_textView);
        tNcView = view.findViewById(R.id.tnc_display_textView);
        editDescriptionIcon = view.findViewById(R.id.editDescriptionIcon);

        MarinaDescriptionViewModel marinaDescriptionViewModel = ViewModelProviders.of(this).get(MarinaDescriptionViewModel.class);
        LiveData<DataSnapshot> liveData = marinaDescriptionViewModel.getDataSnapshotLiveData();
        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                String facilitiestext = "";
                locationView.setText((String) dataSnapshot.child("locationAddress").getValue());
                descriptionView.setText((String) dataSnapshot.child("description").getValue());
                tNcView.setText((String) dataSnapshot.child("terms-and-condition").getValue());
                nameView.setText((String) dataSnapshot.child("marinaName").getValue());
                for (DataSnapshot snapshot : dataSnapshot.child("facilities").getChildren()) {
                    switch (((Long) snapshot.getValue()).intValue()) {
                        case 0:
                            facilitiestext = facilitiestext + "\n" + "Drinking Water";
                            break;
                        case 1:
                            facilitiestext = facilitiestext + "\n" + "Electricity";
                            break;
                        case 2:
                            facilitiestext = facilitiestext + "\n" + "Fuel Station";
                            break;
                        case 3:
                            facilitiestext = facilitiestext + "\n" + "24/7 Access";
                            break;
                        case 4:
                            facilitiestext = facilitiestext + "\n" + "Travel Lift";
                            break;
                        case 5:
                            facilitiestext = facilitiestext + "\n" + "Security";
                            break;
                        case 6:
                            facilitiestext = facilitiestext + "\n" + "Residual Water Collection";
                            break;
                        case 7:
                            facilitiestext = facilitiestext + "\n" + "Restaurant";
                            break;
                        case 8:
                            facilitiestext = facilitiestext + "\n" + "Dry Port";
                            break;
                        case 9:
                            facilitiestext = facilitiestext + "\n" + "Maintenance";
                            break;
                    }
                }
                facilitiesView.setText(facilitiestext);
            }
        });

        editDescriptionIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_viewMarinaDescriptionFragment_to_marinaDescriptionEditFragment2);
            }
        });

        return view;
    }

}
