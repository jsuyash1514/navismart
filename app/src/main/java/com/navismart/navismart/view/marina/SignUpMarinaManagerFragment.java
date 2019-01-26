package com.navismart.navismart.view.marina;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
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
import com.navismart.navismart.RecyclerItemClickListener;
import com.navismart.navismart.adapters.MarinaPicAdapter;
import com.navismart.navismart.model.MarinaPicModel;
import com.navismart.navismart.utils.PreferencesHelper;
import com.navismart.navismart.viewmodels.SignUpViewModel;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import static android.app.Activity.RESULT_OK;
import static com.navismart.navismart.utils.EmailAndPasswordChecker.isEmailValid;
import static com.navismart.navismart.utils.EmailAndPasswordChecker.isPasswordValid;

public class SignUpMarinaManagerFragment extends Fragment {

    int PLACE_PICKER_REQUEST = 1;
    int postionToChange = 0;
    private ImageView profilePic, addLocationIcon;
    private SignUpViewModel signUpViewModel;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private ProgressDialog progressDialog, uploadProgress;
    private NavController navController;
    private EditText passwordEditText, nameEditText, emailEditText, descriptionEditText, t_cEditText, locationEditText, marinaNameEditText;
    private NumberPicker capacityPicker;
    private Button registerButton, uploadProfilePic;
    private Uri profilePicUri = null;
    private boolean nameFilled = false, marinaNameFilled = false;
    private boolean emailValid = false;
    private boolean locationFilled = false;
    private boolean passwordValid = false;
    private boolean enabler = false;
    private LatLng locationLatLng;
    private String locationAddress = "";
    private ArrayList<String> marinaUIDList;
    private RecyclerView marinaPicRecyclerview;
    private List<MarinaPicModel> marinaPicModelList;
    private MarinaPicAdapter picAdapter;
    private CheckBox drinkingWater, electricity, fuelStation, access, travelLift, security, residualWaterCollection, restaurant, dryPort, maintenence;
    private ArrayList<Integer> f;
    private PreferencesHelper preferencesHelper;


    public SignUpMarinaManagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup_marina_manager, container, false);

        preferencesHelper = new PreferencesHelper(getActivity());

        signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(getContext());
        uploadProgress = new ProgressDialog(getContext());
        marinaUIDList = new ArrayList<>();
        navController = Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment);
        passwordEditText = view.findViewById(R.id.password_edit_text);
        locationEditText = view.findViewById(R.id.location_edit_text);
        marinaNameEditText = view.findViewById(R.id.marina_name_edit_text);
        registerButton = view.findViewById(R.id.marina_register_button);
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
        marinaPicRecyclerview = view.findViewById(R.id.marina_pics_recycler_view);
        drinkingWater = view.findViewById(R.id.check_box_0);
        electricity = view.findViewById(R.id.check_box_1);
        fuelStation = view.findViewById(R.id.check_box_2);
        access = view.findViewById(R.id.check_box_3);
        travelLift = view.findViewById(R.id.check_box_4);
        security = view.findViewById(R.id.check_box_5);
        residualWaterCollection = view.findViewById(R.id.check_box_6);
        restaurant = view.findViewById(R.id.check_box_7);
        dryPort = view.findViewById(R.id.check_box_8);
        maintenence = view.findViewById(R.id.check_box_9);

        f = new ArrayList<>();

        marinaPicModelList = new ArrayList<>();
        if (signUpViewModel.getMarinaPicList().getValue().size() == 0) {
            Drawable drawable = getContext().getDrawable(R.drawable.marina_pic_add);
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            MarinaPicModel picModel = new MarinaPicModel(bitmap);
            marinaPicModelList.add(picModel);

            signUpViewModel.getMarinaPicList().setValue(marinaPicModelList);
        } else {
            marinaPicModelList = signUpViewModel.getMarinaPicList().getValue();
        }
        picAdapter = new MarinaPicAdapter(getContext(), marinaPicModelList);

        RecyclerView.LayoutManager recycler = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        marinaPicRecyclerview.setLayoutManager(recycler);
        marinaPicRecyclerview.setAdapter(picAdapter);

        registerButton.setEnabled(enabler);
        if (enabler) registerButton.setTextColor(getResources().getColor(R.color.white));
        else registerButton.setTextColor(Color.GRAY);

        uploadProfilePic.setOnClickListener((View v) ->
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), 100)
        );

        final Observer<Uri> profilePicObserver = new Observer<Uri>() {
            @Override
            public void onChanged(@Nullable Uri uri) {
                profilePic.setImageURI(uri);
                profilePicUri = uri;
            }

        };
        signUpViewModel.getMarinaManagerProfilePic().observe(this, profilePicObserver);

        final Observer<List<MarinaPicModel>> marinaPicListObserver = new Observer<List<MarinaPicModel>>() {
            @Override
            public void onChanged(@Nullable List<MarinaPicModel> marinaPicModels) {
                picAdapter = new MarinaPicAdapter(getContext(), marinaPicModels);

                RecyclerView.LayoutManager recycler = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                marinaPicRecyclerview.setLayoutManager(recycler);
                marinaPicRecyclerview.setAdapter(picAdapter);
            }
        };
        signUpViewModel.getMarinaPicList().observe(this, marinaPicListObserver);

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isEmailValid(s.toString())) {
                    emailValid = true;
                    if (passwordValid && nameFilled && marinaNameFilled && locationFilled)
                        enabler = true;
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
                    if (emailValid && nameFilled && marinaNameFilled && locationFilled)
                        enabler = true;
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
                    if (emailValid && passwordValid && marinaNameFilled && locationFilled)
                        enabler = true;
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

        marinaNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    marinaNameFilled = true;
                    if (emailValid && passwordValid && nameFilled && locationFilled) enabler = true;
                    else enabler = false;
                } else {
                    marinaNameFilled = false;
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

        locationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    locationFilled = true;
                    if (emailValid && passwordValid && nameFilled && marinaNameFilled)
                        enabler = true;
                    else enabler = false;
                } else {
                    marinaNameFilled = false;
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


        registerButton.setOnClickListener((View v) -> registerUser());

        addLocationIcon.setOnClickListener((View v) -> {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            try {
                startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });


        marinaPicRecyclerview.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), marinaPicRecyclerview, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (position == 0)
                            startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), 110);
                        else {
                            postionToChange = position;
                            startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), 111);
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        if (position != 0) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                            alert.setTitle("Delete entry");
                            alert.setMessage("Are you sure you want to delete?");
                            alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    marinaPicModelList.remove(position);
                                    signUpViewModel.getMarinaPicList().setValue(marinaPicModelList);
                                }
                            });
                            alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // close dialog
                                    dialog.cancel();
                                }
                            });
                            alert.show();
                        }
                    }
                }));

        return view;

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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

        } else if (requestCode == 110 && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);
                if (bitmap != null) {
                    MarinaPicModel picModel = new MarinaPicModel(bitmap);
                    marinaPicModelList.add(picModel);
                    signUpViewModel.getMarinaPicList().setValue(marinaPicModelList);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 111 && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);
                if (bitmap != null) {
                    MarinaPicModel picModel = new MarinaPicModel(bitmap);
                    marinaPicModelList.set(postionToChange, picModel);
                    signUpViewModel.getMarinaPicList().setValue(marinaPicModelList);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void checkUserLoggedIn() {
        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    NavOptions navOptions = new NavOptions.Builder()
                            .setPopUpTo(R.id.startFragment, true)
                            .build();
                    navController.navigate(R.id.marina_manager_register_successful_action, null, navOptions);
                }
            }
        });
    }

    public void registerUser() {
        final String name = nameEditText.getText().toString().trim();
        final String email = emailEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();
        final String descr = descriptionEditText.getText().toString().trim();
        final String capacity = String.valueOf(capacityPicker.getValue());
        final String termsAndCond = t_cEditText.getText().toString().trim();
        final String marinaName = marinaNameEditText.getText().toString().trim();

        if (drinkingWater.isChecked()) f.add(0);
        if (electricity.isChecked()) f.add(1);
        if (fuelStation.isChecked()) f.add(2);
        if (access.isChecked()) f.add(3);
        if (travelLift.isChecked()) f.add(4);
        if (security.isChecked()) f.add(5);
        if (residualWaterCollection.isChecked()) f.add(6);
        if (restaurant.isChecked()) f.add(7);
        if (dryPort.isChecked()) f.add(8);
        if (maintenence.isChecked()) f.add(9);


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

                            if(!preferencesHelper.getToken().isEmpty()){
                                currentUser.child("profile").child("fcm_token").setValue(preferencesHelper.getToken());
                            }

                            currentUser.child("marina-description").child("capacity").setValue(capacity);
                            currentUser.child("marina-description").child("marinaName").setValue(marinaName);
                            currentUser.child("marina-description").child("numberOfReviews").setValue("0");
                            currentUser.child("marina-description").child("starRating").setValue("0");

                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(marinaName).build();
                            user.updateProfile(profileUpdates);

                            if (!TextUtils.isEmpty(descr))
                                currentUser.child("marina-description").child("description").setValue(descr);
                            if (!TextUtils.isEmpty(termsAndCond))
                                currentUser.child("marina-description").child("terms-and-condition").setValue(termsAndCond);
                            if (!TextUtils.isEmpty(locationAddress)) {
                                currentUser.child("marina-description").child("locationAddress").setValue(locationAddress);
                                currentUser.child("marina-description").child("latitude").setValue(locationLatLng.latitude);
                                currentUser.child("marina-description").child("longitude").setValue(locationLatLng.longitude);
                            }

                            currentUser.child("marina-description").child("facilities").setValue(f);
                            addLocationInFirestore(locationLatLng.latitude, locationLatLng.longitude);


                            if (marinaPicModelList.size() > 1) {
                                currentUser.child("marina-description").child("no-images").setValue(marinaPicModelList.size());
                                for (int i = 1; i < marinaPicModelList.size(); i++) {
                                    MarinaPicModel model = marinaPicModelList.get(i);
                                    Bitmap bitmap = model.getPic();
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                    byte[] data = baos.toByteArray();

                                    StorageReference marinaPicRef = storageReference.child("users").child(firebaseAuth.getCurrentUser().getUid()).child("marina" + i);

                                    uploadProgress.setMax(100);
                                    uploadProgress.setMessage("Uploading image " + i + "/" + (marinaPicModelList.size() - 1) + "...");
                                    uploadProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                    uploadProgress.show();
                                    uploadProgress.setCancelable(false);

                                    UploadTask uploadTask = marinaPicRef.putBytes(data);
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

                                            Toast.makeText(getContext(), "Error in uploading marina pic!", Toast.LENGTH_SHORT).show();
                                            uploadProgress.dismiss();

                                        }
                                    });
                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            uploadProgress.dismiss();
                                        }
                                    });
                                }
                            }

                            if (profilePicUri != null) {
                                StorageReference profilePicRef = storageReference.child("users").child(firebaseAuth.getCurrentUser().getUid()).child("profile");

                                uploadProgress.setMax(100);
                                uploadProgress.setMessage("Uploading images...");
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

                                        uploadProgress.dismiss();

                                        NavOptions navOptions = new NavOptions.Builder()
                                                .setPopUpTo(R.id.startFragment, true)
                                                .build();
                                        navController.navigate(R.id.marina_manager_register_successful_action, null, navOptions);
                                    }
                                });
                            } else {

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
                    marinaUIDList = (ArrayList<String>) doc.get("Marina List");
                    marinaUIDList.add(firebaseAuth.getCurrentUser().getUid());
                    Map<String, ArrayList<String>> map = new HashMap<>();
                    map.put("Marina List", marinaUIDList);
                    location.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Can't add your location.", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Can't add your location.", Toast.LENGTH_LONG).show();
                    }
                });


    }

}
