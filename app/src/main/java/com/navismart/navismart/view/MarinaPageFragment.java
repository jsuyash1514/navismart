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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import com.navismart.navismart.adapters.ReviewListAdapter;
import com.navismart.navismart.model.MarinaModel;
import com.navismart.navismart.model.ReviewModel;
import com.navismart.navismart.viewmodelfactory.ReviewListViewModelFactory;
import com.navismart.navismart.viewmodels.ReviewListViewModel;

import java.util.ArrayList;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import static com.navismart.navismart.view.BoaterSearchResultsFragment.fromDate;
import static com.navismart.navismart.view.BoaterSearchResultsFragment.toDate;

public class MarinaPageFragment extends Fragment {

    private TextView nameTextView, fromDateTextView, distSearchTextView, locationTextView, toDateTextView, descriptionTextView, facilitiesTextView, tNcTextView;
    private TextView seeMoreReviewsTextView;
    private RatingBar ratingBar;
    private Button bookButton;
    private Button sendMsgButton;
    private ImageView marinaImageView, nextImage, prevImage;
    private NavController navController;
    private RecyclerView reviewListView;
    private ArrayList<ReviewModel> reviewList;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseAuth auth;
    private View reviewTab, descriptionView, facilitiesView, termsNConditionsView;
    //    private RecyclerView imagesRecyclerView;
    private ImageSwitcher imageSwitcher;
    private Bitmap[] images;
    private int noImages = 0;
    private int iLoop = 0;
    private int imageIndex = -1;
    private int noImageLoaded = 0;

    public MarinaPageFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_marina_page, container, false);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        nameTextView = view.findViewById(R.id.marina_name_textView);
        fromDateTextView = view.findViewById(R.id.from_date_display_textView);
        toDateTextView = view.findViewById(R.id.to_date_display_textView);
        distSearchTextView = view.findViewById(R.id.dist_from_search_display);
        locationTextView = view.findViewById(R.id.location_display_textView);
        descriptionTextView = view.findViewById(R.id.description_display_textView);
        facilitiesTextView = view.findViewById(R.id.facilities_display_textView);
        tNcTextView = view.findViewById(R.id.tnc_display_textView);
        ratingBar = view.findViewById(R.id.marina_rating_bar);
        marinaImageView = view.findViewById(R.id.marina_imageView);
        bookButton = view.findViewById(R.id.book_button);
        reviewListView = view.findViewById(R.id.review_list);
        reviewTab = view.findViewById(R.id.review_tab);
        seeMoreReviewsTextView = view.findViewById(R.id.see_more_reviews_text);
        sendMsgButton = view.findViewById(R.id.send_msg_button);
//        imagesRecyclerView = view.findViewById(R.id.imagesrecyclerView);
        descriptionView = view.findViewById(R.id.description_brick);
        imageSwitcher = view.findViewById(R.id.imageSwitcher);
        facilitiesView = view.findViewById(R.id.facilities_brick);
        termsNConditionsView = view.findViewById(R.id.terms_n_conditions_brick);
        nextImage = view.findViewById(R.id.nextImage);
        prevImage = view.findViewById(R.id.prevImage);
        navController = Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment);

        MarinaModel marinaModel = getArguments().getParcelable("marina_model");
        Bundle bundle = new Bundle();
        bundle.putParcelable("marina_model", marinaModel);

        loadReviews(marinaModel.getMarinaUID());

        if (marinaModel.getDescription() == null || marinaModel.getDescription().isEmpty()) {
            descriptionView.setVisibility(View.GONE);
        } else {
            descriptionView.setVisibility(View.VISIBLE);
            descriptionTextView.setText(marinaModel.getDescription());
        }

        if (marinaModel.getFacilityString() == null || marinaModel.getFacilityString().isEmpty()) {
            facilitiesView.setVisibility(View.GONE);
        } else {
            facilitiesView.setVisibility(View.VISIBLE);
            facilitiesTextView.setText(marinaModel.getFacilityString());
        }

        if (marinaModel.getTnc() == null || marinaModel.getTnc().isEmpty()) {
            termsNConditionsView.setVisibility(View.GONE);
        } else {
            tNcTextView.setText(marinaModel.getTnc());
            termsNConditionsView.setVisibility(View.VISIBLE);
        }

        nameTextView.setText(marinaModel.getName());
        ratingBar.setRating(marinaModel.getRating());
        locationTextView.setText(marinaModel.getLocation());
        distSearchTextView.setText(Integer.toString((int) Math.round(marinaModel.getDistFromSearch())) + " km from searched location");
        facilitiesTextView.setText(marinaModel.getFacilityString());
        tNcTextView.setText(marinaModel.getTnc());
        marinaImageView.setImageBitmap(marinaModel.getImage());
        fromDateTextView.setText(fromDate);
        toDateTextView.setText(toDate);

        databaseReference.child("users").child(marinaModel.getMarinaUID()).child("marina-description").child("no-images").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("DATASNAPSHOT", dataSnapshot.toString());
                try {
                    noImages = dataSnapshot.getValue(Integer.class) - 1;
                } catch (Exception e) {
                    noImages = 0;
                }
                if (noImages > 0) {
                    loadImages(noImages, marinaModel.getMarinaUID());
                } else {
                    marinaImageView.setVisibility(View.VISIBLE);
                    imageSwitcher.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        bookButton.setOnClickListener((View v) -> {

            navController.navigate(R.id.action_marinaPageFragment_to_checkoutFragment, bundle);

        });

        seeMoreReviewsTextView.setOnClickListener((View v) -> {

            Bundle bundle1 = new Bundle();
            bundle1.putString("marinaID", marinaModel.getMarinaUID());
            bundle1.putString("marinaName", marinaModel.getName());
            Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_marinaPageFragment_to_viewReviewFragment, bundle1);

        });

        sendMsgButton.setOnClickListener((View v) -> {

            Bundle bundle2 = new Bundle();
            bundle2.putString("marinaName", marinaModel.getName());
            bundle2.putString("boaterName", auth.getCurrentUser().getDisplayName());
            bundle2.putString("marinaID", marinaModel.getMarinaUID());
            bundle2.putString("boaterID", auth.getCurrentUser().getUid());
            Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_marinaPageFragment_to_chatFragment, bundle2);

        });

        nextImage.setOnClickListener((View v) -> {

            if (imageIndex < noImageLoaded) {
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
                switcherImageView.setImageBitmap(images[0]);
                return switcherImageView;
            }
        });

        Animation aniOut = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_out_right);
        Animation aniIn = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left);
        imageSwitcher.setOutAnimation(aniOut);
        imageSwitcher.setInAnimation(aniIn);

        marinaImageView.setVisibility(View.GONE);
        imageSwitcher.setVisibility(View.VISIBLE);

    }

    private void setImageIntoSwitcherOnClick(int index) {

        Drawable drawable = new BitmapDrawable(images[index]);
        imageSwitcher.setImageDrawable(drawable);

    }

    private void loadImages(int n, String marinaUID) {

        images = new Bitmap[n];
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
                                    images[iLoop] = resource;
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

    private void loadReviews(String marinaID) {

        ReviewListViewModel reviewListViewModel = ViewModelProviders.of(this, new ReviewListViewModelFactory(marinaID)).get(ReviewListViewModel.class);
        LiveData<DataSnapshot> liveData = reviewListViewModel.getDataSnapshotLiveData();
        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                ArrayList<ReviewModel> initReview = new ArrayList<>();
                Log.d("DataSnapshot", dataSnapshot.toString());
                if (dataSnapshot != null) {
                    reviewList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        reviewList.add(snapshot.getValue(ReviewModel.class));
                    }

                    if (reviewList.size() >= 2) {
                        initReview.add(reviewList.get(0));
                        initReview.add(reviewList.get(1));
                    } else if (reviewList.size() == 1) {
                        initReview.add(reviewList.get(0));
                    } else {
                        reviewTab.setVisibility(View.GONE);
                    }

                    if (initReview.size() > 0) {
                        ReviewListAdapter reviewListAdapter = new ReviewListAdapter(initReview);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                        reviewListView.setLayoutManager(mLayoutManager);
                        reviewListView.setItemAnimator(new DefaultItemAnimator());
                        reviewListView.setAdapter(reviewListAdapter);
                        reviewTab.setVisibility(View.VISIBLE);
                    }

                }
            }
        });

    }

}
