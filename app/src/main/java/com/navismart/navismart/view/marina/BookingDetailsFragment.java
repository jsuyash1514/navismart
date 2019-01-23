package com.navismart.navismart.view.marina;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.navismart.navismart.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import androidx.navigation.Navigation;

public class BookingDetailsFragment extends Fragment {
    private String bookingID, marinaName, marinaID, boaterName, boaterID;
    private TextView boaterNameView, boatName, boatID, marinaNameView, docksCount, arrival, departure, price, bookingDate, bookingid;
    private Button sendMsgButton;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking_details, container, false);

        bookingID = getArguments().getString("Booking_id");

        boaterNameView = view.findViewById(R.id.bookings_details_boater_name);
        boatName = view.findViewById(R.id.bookings_details_boat_name);
        boatID = view.findViewById(R.id.bookings_details_boat_id);
        marinaNameView = view.findViewById(R.id.booking_details_marina_name);
        docksCount = view.findViewById(R.id.booking_details_number_of_docks);
        arrival = view.findViewById(R.id.booking_details_arrival_date);
        departure = view.findViewById(R.id.booking_details_departure_date);
        price = view.findViewById(R.id.booking_details_price);
        bookingDate = view.findViewById(R.id.booking_details_booking_date);
        bookingid = view.findViewById(R.id.booking_details_booking_id);
        sendMsgButton = view.findViewById(R.id.send_msg_button);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        sendMsgButton.setOnClickListener((View v) -> {
            Bundle bundle = new Bundle();
            bundle.putString("marinaName", marinaName);
            bundle.putString("marinaID", marinaID);
            bundle.putString("boaterName", boaterName);
            bundle.putString("boaterID", boaterID);
            Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_bookingDetailsFragment_to_chatFragment, bundle);
        });

        DatabaseReference ref = databaseReference.child("users").child(auth.getCurrentUser().getUid()).child("bookings").child(bookingID);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                marinaID = dataSnapshot.child("marinaUID").getValue(String.class);
                boaterID = dataSnapshot.child("boaterUID").getValue(String.class);
                boaterName = dataSnapshot.child("boaterName").getValue(String.class);
                boaterNameView.setText(boaterName);
                boatName.setText(dataSnapshot.child("boatName").getValue(String.class));
                boatID.setText(dataSnapshot.child("boatID").getValue(String.class));
                marinaName = dataSnapshot.child("marinaName").getValue(String.class);
                marinaNameView.setText(marinaName);
                docksCount.setText(String.valueOf(dataSnapshot.child("noOfDocks").getValue()));
                arrival.setText(dataSnapshot.child("fromDate").getValue(String.class));
                departure.setText(dataSnapshot.child("toDate").getValue(String.class));
                price.setText(String.valueOf(dataSnapshot.child("finalPrice").getValue()));
                String dateStr = dataSnapshot.child("dateTimeStamp").getValue(String.class);
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
                bookingDate.setText(formattedDate);
                bookingid.setText(bookingID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

}
