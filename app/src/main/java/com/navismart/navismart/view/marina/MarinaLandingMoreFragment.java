package com.navismart.navismart.view.marina;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.navismart.navismart.R;

import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

public class MarinaLandingMoreFragment extends Fragment {

    FirebaseAuth auth;
    private View descriptionBrick;
    private View profileBrick;
    private View reviewsBrick;

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

        descriptionBrick = view.findViewById(R.id.marina_more_marina_desc_layout);
        profileBrick = view.findViewById(R.id.marina_more_profile_layout);
        reviewsBrick = view.findViewById(R.id.marina_more_review_layout);

        auth = FirebaseAuth.getInstance();

        Button logoutButton = view.findViewById(R.id.marina_logout_button);
        logoutButton.setOnClickListener((View v) -> {

            auth.signOut();
            Toast.makeText(getContext(), "Logged out Successful", Toast.LENGTH_SHORT).show();
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.landingFragment, true)
                    .build();
            Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.log_out_action, null, navOptions);

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

        return view;
    }


}
