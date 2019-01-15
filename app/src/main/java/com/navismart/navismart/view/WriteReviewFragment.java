package com.navismart.navismart.view;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.navismart.navismart.R;
import com.navismart.navismart.model.ReviewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import androidx.navigation.Navigation;

public class WriteReviewFragment extends Fragment {

    private RatingBar ratingBar;
    private EditText reviewEditText;
    private Button submitReviewButton;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private String reviewMarinaUID, bookingID;

    public WriteReviewFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write_review, container, false);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        reviewMarinaUID = getArguments().getString("marina_id");
        bookingID = getArguments().getString("bookingID");

        ratingBar = view.findViewById(R.id.reviewRatingBar);
        reviewEditText = view.findViewById(R.id.review_editText);
        submitReviewButton = view.findViewById(R.id.submit_review_button);

        submitReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ratingBar.getRating() == 0) {
                    Toast.makeText(getContext(), "Rating not given!", Toast.LENGTH_SHORT).show();
                } else {
                    submitReview((int) ratingBar.getRating(), reviewEditText.getText().toString());
                }
            }
        });

        return view;
    }

    private void submitReview(int rating, String review) {

        DatabaseReference marinaReviewReference = databaseReference.child("users").child(reviewMarinaUID).child("review");

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        long time = cal.getTimeInMillis();
        SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String gmtTime = df.format(new Date());

        ReviewModel reviewModel = new ReviewModel();
        reviewModel.setReview(review);
        reviewModel.setStarRating(rating);
        reviewModel.setReviewDate(gmtTime);
        reviewModel.setTimeStamp(time);
        reviewModel.setReviewerName(auth.getCurrentUser().getDisplayName());
        reviewModel.setBookingID(bookingID);
        reviewModel.setReviewerID(auth.getCurrentUser().getUid());

        marinaReviewReference.push().setValue(reviewModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Review Submitted Successfully!", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigateUp();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Unable to Submit Review. Sorry for the inconvenience.", Toast.LENGTH_SHORT).show();
                    }
                });

    }

}
