package com.navismart.navismart.view;


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
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.navismart.navismart.R;
import com.navismart.navismart.viewmodels.SignUpViewModel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import static android.app.Activity.RESULT_OK;
import static com.navismart.navismart.EmailAndPasswordChecker.isEmailValid;
import static com.navismart.navismart.EmailAndPasswordChecker.isPasswordValid;

public class SignUpMarinaManagerFragment extends Fragment {

    int PLACE_PICKER_REQUEST = 1;
    private ImageView profilePic, addLocationIcon;
    private SignUpViewModel signUpViewModel;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private ProgressDialog progressDialog, uploadProgress;
    private NavController navController;
    private EditText passwordEditText, nameEditText, emailEditText, descriptionEditText, t_cEditText, locationEditText;
    private NumberPicker capacityPicker;
    private Button registerButton, uploadProfilePic;
    private Uri profilePicUri = null;
    private boolean nameFilled = false;
    private boolean emailValid = false;
    private boolean passwordValid = false;
    private boolean enabler = false;
    private LatLng locationLatLng;
    private String locationAddress;
    private ArrayList<String> marinaList;

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

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(getContext());
        uploadProgress = new ProgressDialog(getContext());
        checkUserLoggedIn();

        marinaList = new ArrayList<>();
        navController = Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment);

        passwordEditText = view.findViewById(R.id.password_edit_text);
        locationEditText = view.findViewById(R.id.location_edit_text);
        registerButton = view.findViewById(R.id.register_button);
        uploadProfilePic = view.findViewById(R.id.upload_button);
        profilePic = view.findViewById(R.id.upload_marina_manager_picture);
        nameEditText = view.findViewById(R.id.name_edit_text);
        emailEditText = view.findViewById(R.id.email_edit_text);
        addLocationIcon = view.findViewById(R.id.add_location);
        capacityPicker = view.findViewById(R.id.reception_capacity_number_picker);
        capacityPicker.setMaxValue(10);
        capacityPicker.setMinValue(1);
        descriptionEditText = view.findViewById(R.id.description_edit_text);
        t_cEditText = view.findViewById(R.id.tnc_edit_text);

        registerButton.setEnabled(enabler);
        if (enabler) registerButton.setTextColor(getResources().getColor(R.color.white));
        else registerButton.setTextColor(Color.GRAY);

        uploadProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), 100);
            }
        });

        final Observer<Uri> profilePicObserver = new Observer<Uri>() {
            @Override
            public void onChanged(@Nullable Uri uri) {
                profilePic.setImageURI(uri);
                profilePicUri = uri;
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

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        addLocationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);
                if (bitmap != null) {
                    signUpViewModel.getMarinaManagerProfilePic().setValue(selectedImage);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(data, getContext());
            locationAddress = place.getAddress().toString();
            locationEditText.setText(locationAddress);
            locationLatLng = place.getLatLng();

        }
    }

    public void checkUserLoggedIn() {
        if (firebaseAuth.getCurrentUser() != null) {
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.startFragment, true)
                    .build();
            navController.navigate(R.id.marina_manager_register_successful_action, null, navOptions);
        }
    }

    public void registerUser() {
        final String name = nameEditText.getText().toString().trim();
        final String email = emailEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();
        final String descr = descriptionEditText.getText().toString().trim();
        final String capacity = String.valueOf(capacityPicker.getValue());
        final String termsAndCond = t_cEditText.getText().toString().trim();

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
                            currentUser.child("profile").child("category").setValue("marina-manager");
                            if (!TextUtils.isEmpty(descr) && !TextUtils.isEmpty(termsAndCond) && !locationAddress.isEmpty()) {
                                currentUser.child("marina-description").child("description").setValue(descr);
                                currentUser.child("marina-description").child("capacity").setValue(capacity);
                                currentUser.child("marina-description").child("terms-and-condition").setValue(termsAndCond);
                                currentUser.child("marina-description").child("locationAddress").setValue(locationAddress);
                                currentUser.child("marina-description").child("latitude").setValue(locationLatLng.latitude);
                                currentUser.child("marina-description").child("longitude").setValue(locationLatLng.longitude);
                                addLocationInFirestore(locationLatLng.latitude, locationLatLng.longitude);
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
                                        navController.navigate(R.id.marina_manager_register_successful_action, null, navOptions);
                                    }
                                });
                            } else {
                                Toast.makeText(getContext(), "Registration successful!", Toast.LENGTH_SHORT).show();

                                NavOptions navOptions = new NavOptions.Builder()
                                        .setPopUpTo(R.id.startFragment, true)
                                        .build();
                                navController.navigate(R.id.marina_manager_register_successful_action, null, navOptions);
                            }

                        } else {
                            Toast.makeText(getContext(), "Could not register, Please try again...", Toast.LENGTH_SHORT).show();
                        }
                    }

                });

    }

    public void addLocationInFirestore(double latitude, double longitude) {

        // i is the greatest multiple of 5 less than the value of latitude.
        int i = (int) (latitude / 10);
        int temp = ((int) latitude) % 10;
        if (temp < 5) i = i * 10;
        else i = (i * 10) + 5;

        int j = (int) (longitude / 10);
        temp = ((int) longitude) % 10;
        if (temp < 5) j = j * 10;
        else j = (j * 10) + 5;


        DocumentReference location = firestore.collection("Location").document(i + "," + j);
        location.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    marinaList = (ArrayList<String>) doc.get("Marina List");
                    Log.d("Firestore: ", "Recieved marina list with size: " + marinaList.size());
                    marinaList.add(firebaseAuth.getCurrentUser().getUid());
                    Map<String, ArrayList<String>> map = new HashMap<>();
                    map.put("Marina List", marinaList);
                    location.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Firestore: ", "Successfully added new marina manager in firestore.");
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(),"Can't add your location.",Toast.LENGTH_LONG).show();
                                    Log.d("Firestore: ", "Failed to add new user location in firestore with error: " + e.toString());
                                }
                            });
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Firestore", "Failed to recieve marina list.");
                        Toast.makeText(getContext(),"Can't add your location.",Toast.LENGTH_LONG).show();
                    }
                });


    }

}
