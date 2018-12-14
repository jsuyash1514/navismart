package com.navismart.navismart.view;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.navismart.navismart.R;

import androidx.navigation.Navigation;

import static com.navismart.navismart.EmailAndPasswordChecker.isEmailValid;
import static com.navismart.navismart.EmailAndPasswordChecker.isPasswordValid;


public class LoginFragment extends Fragment {
    boolean emailValid = false, pwValid = false, enabler = false;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
//        checkUserLoggedIn();

        progressDialog = new ProgressDialog(getContext());

        Button createAcctButton = view.findViewById(R.id.startFragment_createAccountButton);

        Button signInButton = view.findViewById(R.id.startFragment_signInButton);
        signInButton.setEnabled(enabler);
        if (enabler) signInButton.setTextColor(getResources().getColor(R.color.white));
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
                if (enabler) signInButton.setTextColor(getResources().getColor(R.color.white));
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
                if (enabler) signInButton.setTextColor(getResources().getColor(R.color.white));
                else signInButton.setTextColor(getResources().getColor(R.color.colorAccent));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        signInButton.setOnClickListener(view12 -> userLogin(email,pw));

        createAcctButton.setOnClickListener(view1 -> Navigation.findNavController(view1).navigate(R.id.create_acct_action));

        return view;


    }

    private void checkUserLoggedIn(){
        if(firebaseAuth.getCurrentUser() != null){
            Toast.makeText(getContext(), "Already logged in", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(getView()).navigate(R.id.sign_in_action, null);
        }
    }

    private void userLogin(EditText email, EditText pw) {

        String e_mail = email.getText().toString().trim();
        String password = pw.getText().toString().trim();

        if (TextUtils.isEmpty(e_mail)) {
            Toast.makeText(getContext(), "Please enter E-mail", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Please enter Password", Toast.LENGTH_LONG).show();
            return;

        }

        progressDialog.setMessage("Logging in Please wait...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(e_mail, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(getView()).navigate(R.id.sign_in_action, null);
                        } else {
                            Toast.makeText(getContext(), "Incorrect Email or Password", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
