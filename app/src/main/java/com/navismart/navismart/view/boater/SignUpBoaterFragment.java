package com.navismart.navismart.view.boater;


import android.app.Activity;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.navismart.navismart.R;
import com.navismart.navismart.model.BoatModel;
import com.navismart.navismart.utils.PreferencesHelper;
import com.navismart.navismart.viewmodels.SignUpViewModel;

import java.io.FileNotFoundException;
import java.io.IOException;

import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import static com.navismart.navismart.utils.EmailAndPasswordChecker.isEmailValid;
import static com.navismart.navismart.utils.EmailAndPasswordChecker.isPasswordValid;

public class SignUpBoaterFragment extends Fragment {

    private ImageView profilePic;
    private SignUpViewModel signUpViewModel;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private ProgressDialog progressDialog, uploadProgress;
    private NavController navController;
    private Button registerButton, uploadProfilePic;
    private Uri profilePicUri = null;
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText boatNameEditText;
    private EditText boatLengthEditText;
    private EditText boatIDEditText;
    private EditText boatBeamEditText;
    private EditText boatTypeEditText;
    private boolean nameFilled = false;
    private boolean emailValid = false;
    private boolean passwordValid = false;
    private boolean enabler = false;
    private PreferencesHelper preferencesHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_signup_boater, container, false);

        preferencesHelper = new PreferencesHelper(getActivity());

        signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(getContext());
        uploadProgress = new ProgressDialog(getContext());
        navController = Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment);
        passwordEditText = view.findViewById(R.id.password_edit_text);
        registerButton = view.findViewById(R.id.boater_register_button);
        nameEditText = view.findViewById(R.id.person_name_edit_text);
        emailEditText = view.findViewById(R.id.email_edit_text);
        boatBeamEditText = view.findViewById(R.id.boat_beam_edit_text);
        boatNameEditText = view.findViewById(R.id.boat_name_edit_text);
        boatIDEditText = view.findViewById(R.id.boat_id_edit_text);
        boatLengthEditText = view.findViewById(R.id.boat_length_edit_text);
        boatTypeEditText = view.findViewById(R.id.boat_type_edit_text);
        uploadProfilePic = view.findViewById(R.id.upload_button);
        profilePic = view.findViewById(R.id.upload_boater_picture);

        registerButton.setEnabled(enabler);
        if (enabler) registerButton.setTextColor(getResources().getColor(R.color.white));
        else registerButton.setTextColor(Color.GRAY);

        uploadProfilePic.setOnClickListener((View v) ->
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), 200)

        );

        final Observer<Uri> profilePicObserver = new Observer<Uri>() {
            @Override
            public void onChanged(@Nullable Uri uri) {
                profilePic.setImageURI(uri);
                profilePicUri = uri;
            }

        };
        signUpViewModel.getBoaterProfilePic().observe(this, profilePicObserver);

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


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);
                if (bitmap != null) {
                    signUpViewModel.getBoaterProfilePic().setValue(selectedImage);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void checkUserLoggedIn() {
        if (firebaseAuth.getCurrentUser() != null) {
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.startFragment, true)
                    .build();
            navController.navigate(R.id.boater_register_successful_action, null, navOptions);
        }
    }

    public void registerUser() {
        final String name = nameEditText.getText().toString().trim();
        final String email = emailEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();
        final String boatName = boatNameEditText.getText().toString().trim();
        final String boatID = boatIDEditText.getText().toString().trim();
        final String boatLength = boatLengthEditText.getText().toString().trim();
        final String boatBeam = boatBeamEditText.getText().toString().trim();
        final String boatType = boatTypeEditText.getText().toString().trim();

        progressDialog.setMessage("Registering Please wait...");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            DatabaseReference currentUser = databaseReference.child("users").child(firebaseAuth.getCurrentUser().getUid());
                            currentUser.child("profile").child("name").setValue(name);
                            currentUser.child("profile").child("email").setValue(email);
                            currentUser.child("profile").child("category").setValue("boater");
                            if (!preferencesHelper.getToken().isEmpty()) {
                                currentUser.child("profile").child("fcm_token").setValue(preferencesHelper.getToken());
                            }

                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                            user.updateProfile(profileUpdates);
                            if (boatLength != null && !boatLength.isEmpty()) {
                                currentUser.child("boats").child("ID " + boatID).setValue(new BoatModel(boatName, boatID, Float.parseFloat(boatLength), Float.parseFloat(boatBeam), boatType));
                            }
                            if (profilePicUri != null) {
                                StorageReference profilePicRef = storageReference.child("users").child(firebaseAuth.getCurrentUser().getUid()).child("profile");

                                uploadProgress.setMax(100);
                                uploadProgress.setMessage("Uploading image...");
                                uploadProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                uploadProgress.show();
                                uploadProgress.setCancelable(false);

                                UploadTask uploadTask = profilePicRef.putFile(profilePicUri);
                                uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                        uploadProgress.incrementProgressBy((int) progress);
                                    }
                                });
                                uploadTask.addOnFailureListener(new OnFailureListener() {

                                    @Override
                                    public void onFailure(@NonNull Exception exception) {

                                        Toast.makeText(getContext(), "Error in uploading profile pic!", Toast.LENGTH_SHORT).show();
                                        uploadProgress.dismiss();

                                    }
                                });

                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        Toast.makeText(getContext(), "Registration successful!", Toast.LENGTH_SHORT).show();
                                        uploadProgress.dismiss();

                                        NavOptions navOptions = new NavOptions.Builder()
                                                .setPopUpTo(R.id.startFragment, true)
                                                .build();
                                        navController.navigate(R.id.boater_register_successful_action, null, navOptions);
                                    }
                                });
                            } else {
                                Toast.makeText(getContext(), "Registration successful!", Toast.LENGTH_SHORT).show();

                                NavOptions navOptions = new NavOptions.Builder()
                                        .setPopUpTo(R.id.startFragment, true)
                                        .build();
                                navController.navigate(R.id.boater_register_successful_action, null, navOptions);
                            }

                        } else {
                            Toast.makeText(getContext(), "Could not register, Please try again...", Toast.LENGTH_SHORT).show();
                        }
                    }

                });

    }

}

