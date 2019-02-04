package com.navismart.navismart.view.marina;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.navismart.navismart.R;
import com.navismart.navismart.viewmodels.AdminIDViewModel;

import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

public class MarinaLandingMoreFragment extends Fragment {

    private View descriptionBrick;
    private View profileBrick;
    private View reviewsBrick;
    private View contactUsBrick;
    private Button logoutButton;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    public MarinaLandingMoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_marina_landing_more, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        descriptionBrick = view.findViewById(R.id.marina_more_marina_desc_layout);
        profileBrick = view.findViewById(R.id.marina_more_profile_layout);
        reviewsBrick = view.findViewById(R.id.marina_more_review_layout);
        contactUsBrick = view.findViewById(R.id.marina_more_contact_layout);

        auth = FirebaseAuth.getInstance();

        logoutButton = view.findViewById(R.id.marina_logout_button);
        logoutButton.setOnClickListener((View v) -> {

            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle("Logout");
            alert.setMessage("Are you sure you want to logout?");
            alert.setPositiveButton(android.R.string.yes, (dialog, which) -> {
                auth.signOut();
                Toast.makeText(getContext(), "Logged out Successful", Toast.LENGTH_SHORT).show();

                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.landingFragment, true)
                        .build();
                Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.log_out_action, null, navOptions);
            });
            alert.setNegativeButton(android.R.string.no, (dialog, which) -> {
                // close dialog
                dialog.cancel();
            });
            alert.show();


        });

        descriptionBrick.setOnClickListener((View v) -> {

            Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_landingFragment_to_viewMarinaDescriptionFragment);

        });

        profileBrick.setOnClickListener((View v) -> {

            Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_landingFragment_to_marinaProfileFragment);

        });

        reviewsBrick.setOnClickListener((View v) -> {

            Bundle bundle = new Bundle();
            bundle.putString("marinaName", auth.getCurrentUser().getDisplayName());
            bundle.putString("marinaID", auth.getCurrentUser().getUid());
            Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_landingFragment_to_viewReviewFragment, bundle);

        });

        contactUsBrick.setOnClickListener(v -> {

                    AdminIDViewModel adminIDViewModel = ViewModelProviders.of(this).get(AdminIDViewModel.class);
                    adminIDViewModel.getDataSnapshotLiveData().observe(this, dataSnapshot -> {
                        if (dataSnapshot != null) {
                            Bundle bundle = new Bundle();
                            bundle.putString("userName", auth.getCurrentUser().getDisplayName());
                            bundle.putString("userID", auth.getCurrentUser().getUid());
                            bundle.putString("adminID", dataSnapshot.getValue().toString());
                            Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_landingFragment_to_contactUsFragment, bundle);
                        }
                    });

                }


        );

        return view;
    }

}
