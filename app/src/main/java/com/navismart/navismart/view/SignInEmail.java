package com.navismart.navismart.view;


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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class SignInEmail extends Fragment {

    public SignInEmail() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in_email, container, false);

        ImageView backArrowButton = view.findViewById(R.id.back_arrow);
        backArrowButton.setOnClickListener(new View.OnClickListener() {//navigate up
            @Override
            public void onClick(View v) {
                final NavController navController = Navigation.findNavController(getActivity(),R.id.my_nav_host_fragment);
                navController.navigateUp();
            }
        });

        final Button nextButton = view.findViewById(R.id.next_button);

        final EditText emailEditText = view.findViewById(R.id.email_edit_text);

        final Bundle bundle = new Bundle();

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isEmailValid(s.toString())){
                    nextButton.setEnabled(true);
                }
                else {
                    nextButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        View.OnClickListener actionNextListener = Navigation.createNavigateOnClickListener(R.id.sign_in_password_action);//navigate to sign in email page using action
//        nextButton.setOnClickListener(actionNextListener);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(),R.id.my_nav_host_fragment);
                bundle.putString("email_id", emailEditText.getText().toString());
                navController.navigate(R.id.sign_in_password_action,bundle);
            }
        });

        return view;
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
