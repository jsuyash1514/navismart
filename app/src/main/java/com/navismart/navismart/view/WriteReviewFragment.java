package com.navismart.navismart.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.navismart.navismart.R;

import androidx.navigation.Navigation;

public class WriteReviewFragment extends Fragment {

    private RatingBar ratingBar;
    private EditText reviewEditText;
    private Button submitReviewButton;

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

        ratingBar = view.findViewById(R.id.reviewRatingBar);
        reviewEditText = view.findViewById(R.id.review_editText);
        submitReviewButton = view.findViewById(R.id.submit_review_button);

        submitReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ratingBar.getRating() == 0) {
                    Toast.makeText(getContext(), "Rating not given!", Toast.LENGTH_SHORT).show();
                } else {
                    if (submitReview((int) ratingBar.getRating(), reviewEditText.getText().toString())) {
                        Toast.makeText(getContext(), "Review Submitted Successfully!", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigateUp();
                    } else {
                        Toast.makeText(getContext(), "Unable to Submit Review. Sorry for the inconvenience.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }

    private boolean submitReview(int rating, String review) {

        return true;

    }

}
