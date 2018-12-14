package com.navismart.navismart.view;


import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;

import com.navismart.navismart.R;
import com.navismart.navismart.viewmodels.SignUpViewModel;

import java.io.FileNotFoundException;
import java.io.IOException;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import static com.navismart.navismart.EmailAndPasswordChecker.isEmailValid;
import static com.navismart.navismart.EmailAndPasswordChecker.isPasswordValid;

public class SignUpMarinaManagerFragment extends Fragment {

    private ImageView profilePic;
    private SignUpViewModel signUpViewModel;
    private boolean nameFilled = false;
    private boolean emailValid = false;
    private boolean passwordValid = false;
    private boolean enabler = false;

    public SignUpMarinaManagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup_marina_manager, container, false);

        signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);

        final NavController navController = Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment);
        final EditText passwordEditText = view.findViewById(R.id.password_edit_text);
        final Button registerButton = view.findViewById(R.id.register_button);
        final Button uploadProfilePic = view.findViewById(R.id.upload_button);
        profilePic = view.findViewById(R.id.upload_marina_manager_picture);
        EditText nameEditText = view.findViewById(R.id.name_edit_text);
        EditText emailEditText = view.findViewById(R.id.email_edit_text);
        NumberPicker capacityPicker = view.findViewById(R.id.reception_capacity_number_picker);
        capacityPicker.setMaxValue(10);
        capacityPicker.setMinValue(1);

        registerButton.setEnabled(enabler);
        if (enabler) registerButton.setTextColor(getResources().getColor(R.color.white));
        else registerButton.setTextColor(Color.GRAY);

        uploadProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), 100);
            }
        });

        final Observer<Bitmap> profilePicObserver = new Observer<Bitmap>() {
            @Override
            public void onChanged(@Nullable Bitmap bitmap) {
                profilePic.setImageBitmap(bitmap);
            }

        };
        signUpViewModel.getMarinaManagerProfilePic().observe(this, profilePicObserver);


        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isEmailValid(s.toString())) {
                    emailValid = true;
                    if (passwordValid && nameFilled) enabler = true;
                    else enabler = false;
                } else {
                    emailValid = false;
                    enabler = false;
                }
                registerButton.setEnabled(enabler);
                if (enabler) registerButton.setTextColor(getResources().getColor(R.color.white));
                else registerButton.setTextColor(getResources().getColor(R.color.colorAccent));
            }

            @Override
            public void afterTextChanged(Editable s) {
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
                    if (emailValid && nameFilled) enabler = true;
                    else enabler = false;
                } else {
                    passwordValid = false;
                    enabler = false;
                }
                registerButton.setEnabled(enabler);
                if (enabler) registerButton.setTextColor(getResources().getColor(R.color.white));
                else registerButton.setTextColor(getResources().getColor(R.color.colorAccent));
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
                    if (emailValid && passwordValid) enabler = true;
                    else enabler = false;
                } else {
                    nameFilled = false;
                    enabler = false;
                }
                registerButton.setEnabled(enabler);
                if (enabler) registerButton.setTextColor(getResources().getColor(R.color.white));
                else registerButton.setTextColor(getResources().getColor(R.color.colorAccent));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==100 && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);
                if(bitmap != null){
                    signUpViewModel.getMarinaManagerProfilePic().setValue(bitmap);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
