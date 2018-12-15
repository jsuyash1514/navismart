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

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class MarinaPageFragment extends Fragment {

    private TextView nameTextView, fromDateTextView, distCityTextView, locationTextView, toDateTextView, descriptionTextView, facilitiesTextView, tNcTextView;
    private RatingBar ratingBar;
    private Button bookButton;
    private ImageView marinaImageView;
    private NavController navController;

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
        bookButton = view.findViewById(R.id.book_button);
        navController = Navigation.findNavController(getActivity(),R.id.my_nav_host_fragment);

        MarinaModel marinaModel = getArguments().getParcelable("marina_model");
        Bundle bundle = new Bundle();
        bundle.putParcelable("marina_model",bundle);

        nameTextView.setText(marinaModel.getName());
        ratingBar.setRating(marinaModel.getRating());
        locationTextView.setText(marinaModel.getLocation());
        distCityTextView.setText(Float.toString(marinaModel.getDistFromCity()));
        descriptionTextView.setText(marinaModel.getDescription());
        facilitiesTextView.setText(marinaModel.getFacilityString());
        tNcTextView.setText(marinaModel.getTnc());
        marinaImageView.setImageBitmap(marinaModel.getImage());

        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_marinaPageFragment_to_checkoutFragment,bundle);
            }
        });

        return view;
    }

}
