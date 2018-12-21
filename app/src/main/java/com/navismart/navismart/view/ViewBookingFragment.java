package com.navismart.navismart.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.navismart.navismart.R;
import com.navismart.navismart.model.BookingModel;

import androidx.navigation.Navigation;

public class ViewBookingFragment extends Fragment {

    private ImageView marinaImage;
    private TextView marinaName;
    private TextView boaterName;
    private TextView dateRange;
    private TextView boatName;
    private TextView boatID;
    private TextView price;
    private Button reviewButton;

    public ViewBookingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_booking, container, false);

        marinaImage = view.findViewById(R.id.marina_imageView);
        marinaName = view.findViewById(R.id.marina_name_textView);
        boaterName = view.findViewById(R.id.boater_name_display);
        dateRange = view.findViewById(R.id.marina_bookingDate_display);
        boatName = view.findViewById(R.id.boatName_display);
        boatID = view.findViewById(R.id.boatID_display);
        price = view.findViewById(R.id.price_display);
        reviewButton = view.findViewById(R.id.review_button);

        BookingModel bookingModel = getArguments().getParcelable("booking_model");

        marinaName.setText(bookingModel.getMarinaModel().getName());
        marinaImage.setImageBitmap(bookingModel.getMarinaModel().getImage());
        dateRange.setText(bookingModel.getFromDate() + " to " + bookingModel.getToDate());
        price.setText(bookingModel.getMarinaModel().getPrice());
        boaterName.setText(bookingModel.getBoaterName());
        boatName.setText(bookingModel.getBoatModel().getName());
        boatID.setText(bookingModel.getBoatModel().getId());

        if (bookingModel.getBookingTense() == BookingModel.PAST || bookingModel.getBookingTense() == BookingModel.CURRENT) {
            reviewButton.setVisibility(View.VISIBLE);
        } else {
            reviewButton.setVisibility(View.GONE);
        }

        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(),R.id.my_nav_host_fragment).navigate(R.id.action_viewBookingFragment_to_writeReviewFragment);
            }
        });

        return view;
    }

}
