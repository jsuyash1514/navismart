package com.navismart.navismart.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.navismart.navismart.R;
import com.navismart.navismart.model.MarinaModel;

public class MarinaPageFragment extends Fragment {

    private TextView nameTextView, fromDateTextView, distCityTextView, locationTextView, toDateTextView, descriptionTextView, facilitiesTextView, tNcTextView;
    private RatingBar ratingBar;
    private Button bookButton;
    private ImageView marinaImageView;

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

        nameTextView = view.findViewById(R.id.marina_name_textView);
        fromDateTextView = view.findViewById(R.id.from_date_display_textView);
        toDateTextView = view.findViewById(R.id.to_date_display_textView);
        distCityTextView = view.findViewById(R.id.dist_from_city_display);
        locationTextView = view.findViewById(R.id.location_display_textView);
        descriptionTextView = view.findViewById(R.id.description_display_textView);
        facilitiesTextView = view.findViewById(R.id.facilities_display_textView);
        tNcTextView = view.findViewById(R.id.tnc_display_textView);
        ratingBar = view.findViewById(R.id.marina_rating_bar);
        marinaImageView = view.findViewById(R.id.marina_imageView);

        MarinaModel marinaModel = getArguments().getParcelable("marina_model");

        nameTextView.setText(marinaModel.getName());
        ratingBar.setRating(marinaModel.getRating());
        locationTextView.setText(marinaModel.getLocation());
        distCityTextView.setText(Float.toString(marinaModel.getDistFromCity()));
        descriptionTextView.setText(marinaModel.getDescription());
        facilitiesTextView.setText(marinaModel.getFacilities());
        tNcTextView.setText(marinaModel.getTnc());
        marinaImageView.setImageBitmap(marinaModel.getImage());

        return view;
    }

}
