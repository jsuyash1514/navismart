package com.navismart.navismart.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.navismart.navismart.R;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class CreateAccountLandingFragment extends Fragment {

    private ImageView backArrow;

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

        final View view = inflater.inflate(R.layout.fragment_create_account_landing, container, false);

        final Button nextButton = view.findViewById(R.id.next_button);
        final RadioGroup userRadioGroup = view.findViewById(R.id.radio_grp);
        final NavController navController = Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment);

        backArrow = view.findViewById(R.id.back_arrow);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigateUp();
            }
        });

        userRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                nextButton.setEnabled(true);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (userRadioGroup.getCheckedRadioButtonId()) {
                    case R.id.marina_manager_radio_button: {
                        navController.navigate(R.id.action_createAccountLandingFragment_to_marinaManagerRegisterFragment);
                        break;
                    }
                    case R.id.boater_radio_button: {
                        navController.navigate(R.id.action_createAccountLandingFragment_to_boaterRegisterFragment);
                        break;
                    }
                    case R.id.third_party_radio_button: {
                        break;
                    }
                }
            }
        });

        return view;
    }

}
