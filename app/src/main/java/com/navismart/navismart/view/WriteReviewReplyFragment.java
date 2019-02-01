package com.navismart.navismart.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.navismart.navismart.R;
import com.navismart.navismart.model.ReviewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import androidx.navigation.Navigation;

public class WriteReviewReplyFragment extends Fragment {

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private ReviewModel reviewModel;
    private Button replyButton, cancelButton;
    private EditText replyEditText;
    private TextView nameView, dateView, reviewTextView;
    private RatingBar ratingBar;

    public WriteReviewReplyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write_review_reply, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        replyButton = view.findViewById(R.id.replyButton);
        cancelButton = view.findViewById(R.id.cancel_action);
        replyEditText = view.findViewById(R.id.writeReplyEditText);
        nameView = view.findViewById(R.id.reviewer_name);
        dateView = view.findViewById(R.id.review_date);
        ratingBar = view.findViewById(R.id.rating_display_box);
        reviewTextView = view.findViewById(R.id.reviewTextView);

        reviewModel = getArguments().getParcelable("reviewModel");

        nameView.setText(reviewModel.getReviewerName());
        String dateStr = reviewModel.getReviewDate();
        SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        df.setTimeZone(TimeZone.getDefault());
        String formattedDate = df.format(date);
        dateView.setText(formattedDate);
        ratingBar.setRating(Float.parseFloat(reviewModel.getStarRating()));

        if (!reviewModel.getReply().trim().isEmpty()) {
            replyEditText.setText(reviewModel.getReply());
        }
        reviewTextView.setText(reviewModel.getReview());
        replyButton.setOnClickListener(v -> {
            reviewModel.setReply(replyEditText.getText().toString().trim());

            DatabaseReference marinaReviewReference = databaseReference.child("users").child(reviewModel.getMarinaID()).child("review");
            marinaReviewReference.child(reviewModel.getReviewID()).setValue(reviewModel).addOnSuccessListener(aVoid -> {
                Toast.makeText(getContext(), "Reply updated Successfully.", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigateUp();

            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(), "Unable to update reply. Sorry for the inconvenience.", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigateUp();
            });


        });

        cancelButton.setOnClickListener(v -> {
            Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigateUp();
        });

        return view;
    }

}
