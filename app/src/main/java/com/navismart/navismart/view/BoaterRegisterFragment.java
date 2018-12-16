package com.navismart.navismart.view;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.navismart.navismart.R;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import static com.navismart.navismart.EmailAndPasswordChecker.isEmailValid;
import static com.navismart.navismart.EmailAndPasswordChecker.isPasswordValid;
import static com.navismart.navismart.view.CreateAccountLandingFragment.navControllerAcctLanding;

public class BoaterRegisterFragment extends Fragment {

    private ImageView back_arrow;
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText boatNameEditText;
    private EditText boatLengthEditText;
    private EditText boatIDEditText;
    private EditText boatBeamEditText;
    private EditText boatTypeEditText;
    private boolean nameFilled = false;
    private boolean emailValid = false;
    private boolean passwordValid = false;

    public BoaterRegisterFragment() {
        // Required empty public constructor
    }


    private boolean registerValid() {

        return nameFilled && emailValid && passwordValid;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_boater_register, container, false);

        final NavController navController = Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment);
        final EditText passwordEditText = view.findViewById(R.id.password_edit_text);
        final Button registerButton = view.findViewById(R.id.register_button);
        nameEditText = view.findViewById(R.id.person_name_edit_text);
        emailEditText = view.findViewById(R.id.email_edit_text);
        boatBeamEditText = view.findViewById(R.id.boat_beam_edit_text);
        boatNameEditText = view.findViewById(R.id.boat_name_edit_text);
        boatIDEditText = view.findViewById(R.id.boat_id_edit_text);
        boatLengthEditText = view.findViewById(R.id.boat_length_edit_text);
        boatTypeEditText = view.findViewById(R.id.boat_type_edit_text);

        if (registerValid()) {
            registerButton.setEnabled(true);
            registerButton.setTextColor(Color.WHITE);
        } else {
            registerButton.setEnabled(false);
            registerButton.setTextColor(Color.GRAY);
        }

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navControllerAcctLanding.navigate(R.id.boaterSearchResultsFragment);
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isPasswordValid(s.toString())) {
                    passwordValid = true;
                } else {
                    passwordValid = false;
                }
                if (registerValid()) {
                    registerButton.setEnabled(true);
                    registerButton.setTextColor(Color.WHITE);
                } else {
                    registerButton.setEnabled(false);
                    registerButton.setTextColor(Color.GRAY);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    nameFilled = true;
                } else {
                    nameFilled = false;
                }
                if (registerValid()) {
                    registerButton.setEnabled(true);
                    registerButton.setTextColor(Color.WHITE);
                } else {
                    registerButton.setEnabled(false);
                    registerButton.setTextColor(Color.GRAY);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isEmailValid(s.toString())) {
                    emailValid = true;
                } else {
                    emailValid = false;
                }
                if (registerValid()) registerButton.setEnabled(true);
                else registerButton.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return view;
    }

}
