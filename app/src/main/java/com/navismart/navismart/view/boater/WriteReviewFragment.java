package com.navismart.navismart.view.boater;


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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    private String reviewMarinaUID, bookingID, reviewerName;

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
        reviewerName = getArguments().getString("reviewer_name");

        ratingBar = view.findViewById(R.id.reviewRatingBar);
        reviewEditText = view.findViewById(R.id.review_editText);
        submitReviewButton = view.findViewById(R.id.submit_review_button);

        submitReviewButton.setOnClickListener((View v) -> {

            if (ratingBar.getRating() == 0) {
                Toast.makeText(getContext(), "Rating not given!", Toast.LENGTH_SHORT).show();
            } else {
                submitReview(ratingBar.getRating(), reviewEditText.getText().toString());
            }

        });

        return view;
    }

    private void submitReview(float rating, String review) {

        DatabaseReference marinaReviewReference = databaseReference.child("users").child(reviewMarinaUID).child("review");

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        long time = cal.getTimeInMillis();
        SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String gmtTime = df.format(new Date());

        ReviewModel reviewModel = new ReviewModel();
        reviewModel.setReview(review);
        reviewModel.setStarRating(String.valueOf(rating));
        reviewModel.setReviewDate(gmtTime);
        reviewModel.setTimeStamp(time);
        reviewModel.setReviewerName(reviewerName);
        reviewModel.setBookingID(bookingID);
        reviewModel.setReviewerID(auth.getCurrentUser().getUid());

        marinaReviewReference.push().setValue(reviewModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Review Submitted Successfully!", Toast.LENGTH_SHORT).show();
                        DatabaseReference reference = databaseReference.child("users").child(reviewMarinaUID).child("marina-description");
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                float noOfReviews = Float.parseFloat(dataSnapshot.child("numberOfReviews").getValue(String.class));
                                float starRating = Float.parseFloat(dataSnapshot.child("starRating").getValue(String.class));
                                float newRating = ((noOfReviews * starRating) + rating) / (noOfReviews + 1);
                                reference.child("numberOfReviews").setValue(String.valueOf(noOfReviews + 1));
                                reference.child("starRating").setValue(String.valueOf(newRating));
                                databaseReference.child("users").child(reviewMarinaUID).child("bookings").child(bookingID).child("reviewed").setValue(true);
                                databaseReference.child("users").child(auth.getCurrentUser().getUid()).child("bookings").child(bookingID).child("reviewed").setValue(true);
                                Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigateUp();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getContext(), "Unable to Submit Review. Sorry for the inconvenience.", Toast.LENGTH_SHORT).show();
                            }
                        });
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
