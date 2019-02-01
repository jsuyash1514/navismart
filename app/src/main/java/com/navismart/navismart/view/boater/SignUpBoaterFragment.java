package com.navismart.navismart.view.boater;


import android.app.Activity;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

    private int READ_REQUEST_CODE = 300;
    private ImageView profilePic;
    private SignUpViewModel signUpViewModel;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private ProgressDialog progressDialog, uploadProgress;
    private NavController navController;
    private Button registerButton, uploadProfilePic, uploadRegistration;
    private Uri profilePicUri = null, boatRegistrationUri = null;
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText boatNameEditText;
    private EditText boatLengthEditText;
    private EditText boatIDEditText;
    private EditText boatBeamEditText;
    private EditText boatTypeEditText;
    private EditText boatRegistrationEditText;
    private boolean nameFilled = false;
    private boolean emailValid = false;
    private boolean passwordValid = false;
    private boolean registrationFilled = false;
    private boolean enabler = false;
    private PreferencesHelper preferencesHelper;

    public SignUpBoaterFragment() {
        // Required empty public constructor
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

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
        uploadRegistration = view.findViewById(R.id.upload_registration_button);
        boatRegistrationEditText = view.findViewById(R.id.boat_registration_edit_text);

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

        uploadRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/pdf");
                startActivityForResult(intent, READ_REQUEST_CODE);

            }
        });

        final Observer<Uri> boatRegistrationObserver = new Observer<Uri>() {
            @Override
            public void onChanged(@Nullable Uri uri) {
                boatRegistrationUri = uri;
                String result = getPath(getContext(), uri);
                int cut = result.lastIndexOf('/');
                if (cut != -1) {
                    result = result.substring(cut + 1);
                }
                boatRegistrationEditText.setText(result);
                Log.d("registration", uri.getPath());
            }
        };
        signUpViewModel.getBoatRegistration().observe(this, boatRegistrationObserver);

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isEmailValid(s.toString())) {
                    emailValid = true;
                    if (passwordValid && nameFilled && registrationFilled) enabler = true;
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
                    if (emailValid && nameFilled && registrationFilled) enabler = true;
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
                    if (emailValid && passwordValid && registrationFilled) enabler = true;
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

        boatRegistrationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    registrationFilled = true;
                    if (emailValid && passwordValid && nameFilled) enabler = true;
                    else enabler = false;
                } else {
                    registrationFilled = false;
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
        } else if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri selectedFile = data.getData();
            signUpViewModel.getBoatRegistration().setValue(selectedFile);
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

    public String getPath(final Context context, final Uri uri) {

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


}
