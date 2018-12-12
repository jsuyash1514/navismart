package com.navismart.navismart.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.navismart.navismart.R;

import androidx.navigation.Navigation;

import static com.navismart.navismart.EmailAndPasswordChecker.isEmailValid;
import static com.navismart.navismart.EmailAndPasswordChecker.isPasswordValid;


public class LoginFragment extends Fragment {
    boolean emailValid = false, pwValid = false, enabler = false;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button createAcctButton = view.findViewById(R.id.startFragment_createAccountButton);

        Button signInButton = view.findViewById(R.id.startFragment_signInButton);
        signInButton.setEnabled(enabler);
        if(enabler) signInButton.setTextColor(getResources().getColor(R.color.white));
        else signInButton.setTextColor(Color.GRAY);

        EditText email = view.findViewById(R.id.startFragment_emailEditText);
        EditText pw = view.findViewById(R.id.startFragment_passwordEditText);

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isEmailValid(s.toString())) {
                    emailValid = true;
                    if (pwValid) enabler = true;
                    else enabler = false;
                } else {
                    emailValid = false;
                    enabler = false;
                }
                signInButton.setEnabled(enabler);
                if(enabler) signInButton.setTextColor(getResources().getColor(R.color.white));
                else signInButton.setTextColor(getResources().getColor(R.color.colorAccent));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isPasswordValid(s.toString())) {
                    pwValid = true;
                    if (emailValid) enabler = true;
                    else enabler = false;
                } else {
                    pwValid = false;
                    enabler = false;
                }
                signInButton.setEnabled(enabler);
                if(enabler) signInButton.setTextColor(getResources().getColor(R.color.white));
                else signInButton.setTextColor(getResources().getColor(R.color.colorAccent));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        View.OnClickListener actionSignInListener = Navigation.createNavigateOnClickListener(R.id.sign_in_action);//navigate to sign in email page using action
        signInButton.setOnClickListener(actionSignInListener);

        View.OnClickListener actionCreateListener = Navigation.createNavigateOnClickListener(R.id.create_acct_action);//navigate to create acct page using action
        createAcctButton.setOnClickListener(actionCreateListener);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
