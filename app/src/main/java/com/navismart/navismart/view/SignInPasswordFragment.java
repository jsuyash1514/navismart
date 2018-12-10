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
import android.widget.TextView;

import com.navismart.navismart.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class SignInPasswordFragment extends Fragment {

    public SignInPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_in_password, container, false);

        final Button registerButton = view.findViewById(R.id.register_button);

        final EditText passwordEditText = view.findViewById(R.id.password_edit_text);

        TextView toEmailTextView = view.findViewById(R.id.to_email);
        toEmailTextView.setText("to "+ getArguments().getString("email_id"));

        CheckBox passwordCheckBox = view.findViewById(R.id.show_password_checkbox);
        passwordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else {
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
                if(isValidPassword(s.toString())){
                    registerButton.setEnabled(true);
                }
                else{
                    registerButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ImageView backArrowButton = view.findViewById(R.id.back_arrow);
        backArrowButton.setOnClickListener(new View.OnClickListener() {//navigate up
            @Override
            public void onClick(View v) {
                final NavController navController = Navigation.findNavController(getActivity(),R.id.my_nav_host_fragment);
                navController.navigateUp();
            }
        });

        return view;
    }


    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
}
