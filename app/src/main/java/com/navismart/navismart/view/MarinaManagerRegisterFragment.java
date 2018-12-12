package com.navismart.navismart.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;

import com.navismart.navismart.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class MarinaManagerRegisterFragment extends Fragment {

    private EditText nameEditText;
    private EditText emailEditText;
    private CheckBox passwordCheckBox;
    private NumberPicker capacityPicker;
    private boolean nameFilled = false;
    private boolean emailValid = false;
    private boolean passwordValid = false;

    public MarinaManagerRegisterFragment() {
        // Required empty public constructor
    }

    private boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    private boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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
        View view = inflater.inflate(R.layout.fragment_marina_manager_register, container, false);

        ImageView backArrow = view.findViewById(R.id.back_arrow);
        final NavController navController = Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment);
        final EditText passwordEditText = view.findViewById(R.id.password_edit_text);
        final Button registerButton = view.findViewById(R.id.register_button);
        nameEditText = view.findViewById(R.id.name_edit_text);
        emailEditText = view.findViewById(R.id.email_edit_text);
        passwordCheckBox = view.findViewById(R.id.show_password_checkbox);
        capacityPicker = view.findViewById(R.id.reception_capacity_number_picker);
        capacityPicker.setMaxValue(10);
        capacityPicker.setMinValue(1);

        passwordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isValidPassword(s.toString())) {
                    passwordValid = true;
                } else {
                    passwordValid = false;
                }
                if (registerValid()) registerButton.setEnabled(true);
                else registerButton.setEnabled(false);
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
                if (registerValid()) registerButton.setEnabled(true);
                else registerButton.setEnabled(false);
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

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigateUp();
            }
        });

        return view;
    }

}