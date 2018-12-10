package com.navismart.navismart.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.navismart.navismart.R;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import static com.navismart.navismart.view.MainActivity.toolbar;

public class CreateAccountLandingFragment extends Fragment {


    public CreateAccountLandingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_create_account_landing, container, false);

        ImageView backArrow = view.findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NavController navController = Navigation.findNavController(getActivity(),R.id.my_nav_host_fragment);
                navController.navigateUp();
            }
        });

        return view;
    }

}
