package com.navismart.navismart.view.boater;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
    private StorageReference picReference;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

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

        databaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        marinaImage = view.findViewById(R.id.marina_imageView);
        marinaName = view.findViewById(R.id.marina_name_textView);
        boaterName = view.findViewById(R.id.boater_name_display);
        dateRange = view.findViewById(R.id.marina_bookingDate_display);
        boatName = view.findViewById(R.id.boatName_display);
        boatID = view.findViewById(R.id.boatID_display);
        price = view.findViewById(R.id.price_display);
        reviewButton = view.findViewById(R.id.review_button);
        reviewButton.setEnabled(false);

        BookingModel bookingModel = getArguments().getParcelable("booking_model");

        picReference = FirebaseStorage.getInstance().getReference().child("users").child(bookingModel.getMarinaUID()).child("marina1");
        picReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Log.d("URI", uri.toString());
                Glide.with(getContext())
                        .load(uri)
                        .into(marinaImage);

            }
        });

        databaseReference.child("users").child(auth.getCurrentUser().getUid()).child("bookings").child(bookingModel.getBookingID()).child("reviewed").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.getValue(Boolean.class)) {
                    reviewButton.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        marinaName.setText(bookingModel.getMarinaName());
        dateRange.setText(bookingModel.getFromDate() + " to " + bookingModel.getToDate());
        price.setText(Float.toString(bookingModel.getFinalPrice()));
        boaterName.setText(bookingModel.getBoaterName());
        boatName.setText(bookingModel.getBoatName());
        boatID.setText(bookingModel.getBoatID());

        if ((bookingModel.getBookingTense() == BookingModel.PAST || bookingModel.getBookingTense() == BookingModel.CURRENT) && !bookingModel.isReviewed()) {
            reviewButton.setVisibility(View.VISIBLE);
        } else {
            reviewButton.setVisibility(View.GONE);
        }

        reviewButton.setOnClickListener((View v) -> {
            Bundle bundle = new Bundle();
            bundle.putString("reviewer_name", bookingModel.getBoaterName());
            bundle.putString("marina_id", bookingModel.getMarinaUID());
            bundle.putString("bookingID", bookingModel.getBookingID());
            Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_viewBookingFragment_to_writeReviewFragment, bundle);

        });

        return view;
    }

}