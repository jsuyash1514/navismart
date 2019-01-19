package com.navismart.navismart.view;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.navismart.navismart.R;
import com.navismart.navismart.viewmodels.MarinaDescriptionViewModel;

import java.util.ArrayList;

import androidx.navigation.Navigation;

public class ViewMarinaDescriptionFragment extends Fragment {

    private TextView nameView, locationView, descriptionView, facilitiesView, tNcView;
    private ImageView marinaImageView, editDescriptionIcon, nextImage, prevImage;
    //    private RecyclerView imagesRecyclerView;
    private ImageSwitcher imageSwitcher;
    private ArrayList<Bitmap> images;
    private int noImages = 0;
    private int iLoop = 0;
    private int imageIndex = 0;
    private int noImageLoaded = 0;
    private Animation aniIn, aniOut;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseAuth auth;

    public ViewMarinaDescriptionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_marina_description, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        nameView = view.findViewById(R.id.marina_name_textView);
        locationView = view.findViewById(R.id.location_display_textView);
        descriptionView = view.findViewById(R.id.description_display_textView);
        facilitiesView = view.findViewById(R.id.facilities_display_textView);
        tNcView = view.findViewById(R.id.tnc_display_textView);
        editDescriptionIcon = view.findViewById(R.id.editDescriptionIcon);
        marinaImageView = view.findViewById(R.id.marina_imageView);
        imageSwitcher = view.findViewById(R.id.imageSwitcher);
        nextImage = view.findViewById(R.id.nextImage);
        prevImage = view.findViewById(R.id.prevImage);

        MarinaDescriptionViewModel marinaDescriptionViewModel = ViewModelProviders.of(this).get(MarinaDescriptionViewModel.class);
        LiveData<DataSnapshot> liveData = marinaDescriptionViewModel.getDataSnapshotLiveData();
        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                String facilitiestext = "";
                locationView.setText((String) dataSnapshot.child("locationAddress").getValue());
                descriptionView.setText((String) dataSnapshot.child("description").getValue());
                tNcView.setText((String) dataSnapshot.child("terms-and-condition").getValue());
                nameView.setText((String) dataSnapshot.child("marinaName").getValue());

                databaseReference.child("users").child(auth.getCurrentUser().getUid()).child("marina-description").child("no-images").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("DATASNAPSHOT", dataSnapshot.toString());
                        try {
                            noImages = dataSnapshot.getValue(Integer.class) - 1;
                        } catch (Exception e) {
                            noImages = 0;
                        }
                        if (noImages > 0) {
                            loadImages(noImages, auth.getCurrentUser().getUid());
                        } else {
                            marinaImageView.setVisibility(View.VISIBLE);
                            imageSwitcher.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                for (DataSnapshot snapshot : dataSnapshot.child("facilities").getChildren()) {
                    switch (((Long) snapshot.getValue()).intValue()) {
                        case 0:
                            facilitiestext = facilitiestext + "\n" + "Drinking Water";
                            break;
                        case 1:
                            facilitiestext = facilitiestext + "\n" + "Electricity";
                            break;
                        case 2:
                            facilitiestext = facilitiestext + "\n" + "Fuel Station";
                            break;
                        case 3:
                            facilitiestext = facilitiestext + "\n" + "24/7 Access";
                            break;
                        case 4:
                            facilitiestext = facilitiestext + "\n" + "Travel Lift";
                            break;
                        case 5:
                            facilitiestext = facilitiestext + "\n" + "Security";
                            break;
                        case 6:
                            facilitiestext = facilitiestext + "\n" + "Residual Water Collection";
                            break;
                        case 7:
                            facilitiestext = facilitiestext + "\n" + "Restaurant";
                            break;
                        case 8:
                            facilitiestext = facilitiestext + "\n" + "Dry Port";
                            break;
                        case 9:
                            facilitiestext = facilitiestext + "\n" + "Maintenance";
                            break;
                    }
                }
                facilitiesView.setText(facilitiestext);
            }
        });

        editDescriptionIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_viewMarinaDescriptionFragment_to_marinaDescriptionEditFragment2);
            }
        });


        nextImage.setOnClickListener((View v) -> {

            if (imageIndex < noImageLoaded - 1) {
                imageIndex++;
                setImageIntoSwitcherOnClick(imageIndex);
            }
        });

        prevImage.setOnClickListener((View v) -> {

            if (imageIndex > 0) {
                imageIndex--;
                setImageIntoSwitcherOnClick(imageIndex);
            }
        });

        return view;
    }

    private void setFirstImageIntoImageSwitcher() {

        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView switcherImageView = new ImageView(getActivity());
                switcherImageView.setLayoutParams(new ImageSwitcher.LayoutParams(
                        ImageSwitcher.LayoutParams.MATCH_PARENT, ImageSwitcher.LayoutParams.MATCH_PARENT
                ));
                switcherImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                switcherImageView.setImageBitmap(images.get(0));
                return switcherImageView;
            }
        });

        aniOut = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_out_right);
        aniIn = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left);

        marinaImageView.setVisibility(View.GONE);
        imageSwitcher.setVisibility(View.VISIBLE);

    }

    private void setImageIntoSwitcherOnClick(int index) {

        Drawable drawable = new BitmapDrawable(images.get(index));
        imageSwitcher.setImageDrawable(drawable);

    }

    private void loadImages(int n, String marinaUID) {

        images = new ArrayList<>(n);
        noImageLoaded = 0;

        for (iLoop = 0; iLoop < n; iLoop++) {
            StorageReference picReference = storageReference.child("users").child(marinaUID).child("marina" + (iLoop + 1));
            picReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(getContext())
                            .asBitmap()
                            .load(uri)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    images.add(resource);
                                    noImageLoaded++;
                                    if (noImageLoaded == 1) {
                                        setFirstImageIntoImageSwitcher();
                                    }
                                }
                            });
                }
            });

        }

//        MarinaImagesAdapter marinaImagesAdapter = new MarinaImagesAdapter(n, marinaUID, getContext());
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
//        imagesRecyclerView.setLayoutManager(mLayoutManager);
//        imagesRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        imagesRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
//        imagesRecyclerView.setAdapter(marinaImagesAdapter);

    }

}
