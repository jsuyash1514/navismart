package com.navismart.navismart.view.boater;


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

import java.util.Calendar;
import java.util.Date;

import androidx.navigation.Navigation;

import static com.navismart.navismart.MainActivity.getDateFromString;

public class ViewBookingFragment extends Fragment {

    private ImageView marinaImage;
    private TextView marinaName;
    private TextView boaterName;
    private TextView dateRange;
    private TextView boatName;
    private TextView boatID;
    private TextView price;
    private Button reviewButton, cancelBookingButton;
    private StorageReference picReference;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private Date date;
    private Long noOfDocksAvailable;
    private View cancelledBookingLayout;

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
        cancelBookingButton = view.findViewById(R.id.cancel_booking_button);
        cancelledBookingLayout = view.findViewById(R.id.cancelled_booking_layout);
        reviewButton.setVisibility(View.GONE);

        BookingModel bookingModel = getArguments().getParcelable("booking_model");

        picReference = FirebaseStorage.getInstance().getReference().child("users").child(bookingModel.getMarinaUID()).child("marina1");
        picReference.getDownloadUrl().addOnSuccessListener(uri -> {

            Log.d("URI", uri.toString());
            Glide.with(getContext())
                    .load(uri)
                    .into(marinaImage);

        });

        databaseReference.child("users").child(auth.getCurrentUser().getUid()).child("bookings").child(bookingModel.getBookingID()).child("reviewed").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.getValue(Boolean.class)) {
                    reviewButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child("users").child(auth.getCurrentUser().getUid()).child("bookings").child(bookingModel.getBookingID()).child("status").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(String.class).equals("cancelled")) {
                    cancelBookingButton.setVisibility(View.GONE);
                    cancelledBookingLayout.setVisibility(View.VISIBLE);
                } else {
                    cancelBookingButton.setVisibility(View.VISIBLE);
                    cancelledBookingLayout.setVisibility(View.GONE);
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
        if ((bookingModel.getBookingTense() == BookingModel.PAST || bookingModel.getBookingTense() == BookingModel.CURRENT))
            cancelBookingButton.setVisibility(View.GONE);
        else
            cancelBookingButton.setVisibility(View.VISIBLE);

        reviewButton.setOnClickListener((View v) -> {
            Bundle bundle = new Bundle();
            bundle.putString("reviewer_name", bookingModel.getBoaterName());
            bundle.putString("marina_id", bookingModel.getMarinaUID());
            bundle.putString("bookingID", bookingModel.getBookingID());
            Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_viewBookingFragment_to_writeReviewFragment, bundle);

        });

        cancelBookingButton.setOnClickListener(v -> {
            String marinaUID = bookingModel.getMarinaUID();
            String boaterUID = bookingModel.getBoaterUID();
            Long noOfDocks = bookingModel.getNoOfDocks();
            String fromDate = bookingModel.getFromDate();
            String toDate = bookingModel.getToDate();
            String bookingID = bookingModel.getBookingID();

            Date startDate = getDateFromString(fromDate);
            Date endDate = getDateFromString(toDate);

            Calendar start = Calendar.getInstance();
            start.setTime(startDate);
            Calendar end = Calendar.getInstance();
            end.setTime(endDate);

            DatabaseReference reference = databaseReference.child("bookings").child(marinaUID);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
                        noOfDocksAvailable = dataSnapshot.child(String.valueOf(date.getYear() + 1900)).child(String.valueOf(date.getMonth() + 1)).child(String.valueOf(date.getDate())).child("noOfDocksAvailable").getValue(Long.class);
                        reference.child(String.valueOf(date.getYear() + 1900)).child(String.valueOf(date.getMonth() + 1)).child(String.valueOf(date.getDate())).child("noOfDocksAvailable").setValue(noOfDocks + noOfDocksAvailable);
                        reference.child(String.valueOf(date.getYear() + 1900)).child(String.valueOf(date.getMonth() + 1)).child(String.valueOf(date.getDate())).child(bookingID).setValue(null);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            reference.child(String.valueOf(end.getTime().getYear() + 1900)).child(String.valueOf(end.getTime().getMonth() + 1)).child(String.valueOf(end.getTime().getDate())).child(bookingID).setValue(null);

            DatabaseReference dref = databaseReference.child("users").child(marinaUID).child("bookings").child(bookingID);
            dref.child("status").setValue("cancelled");
            dref.child("reviewed").setValue(true);

            DatabaseReference dataRef = databaseReference.child("users").child(boaterUID).child("bookings").child(bookingID);
            dataRef.child("status").setValue("cancelled");
            dataRef.child("reviewed").setValue(true);
            Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigateUp();
        });

        return view;
    }

}
