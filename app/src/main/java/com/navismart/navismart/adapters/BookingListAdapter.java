package com.navismart.navismart.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.navismart.navismart.R;
import com.navismart.navismart.model.BookingModel;

import java.util.List;

import androidx.navigation.Navigation;

public class BookingListAdapter extends RecyclerView.Adapter<BookingListAdapter.MyViewHolder> {

    private List<BookingModel> bookingList;
    private Activity activity;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    public BookingListAdapter(Activity activity, List<BookingModel> bookingList) {
        this.bookingList = bookingList;
        this.activity = activity;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.booking_card_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        BookingModel bookingModel = bookingList.get(position);
        holder.marinaNameTextView.setText(bookingModel.getMarinaName());
        holder.dateTextView.setText(bookingModel.getFromDate() + " to " + bookingModel.getToDate());

        databaseReference.child("users").child(auth.getCurrentUser().getUid()).child("bookings").child(bookingModel.getBookingID()).child("status").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(String.class).equals("cancelled")) {
                    holder.cancelledMsg.setVisibility(View.VISIBLE);
                } else {
                    holder.cancelledMsg.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.bookingCard.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("booking_model", bookingModel);
            switch (bookingModel.getBookingTense()) {

                case BookingModel.PAST:
                    Navigation.findNavController(activity, R.id.my_nav_host_fragment).navigate(R.id.action_pastBookingsFragment_to_viewBookingFragment, bundle);
                    break;
                case BookingModel.CURRENT:
                    Navigation.findNavController(activity, R.id.my_nav_host_fragment).navigate(R.id.action_currentBookingsFragment_to_viewBookingFragment, bundle);
                    break;
                case BookingModel.UPCOMING:
                    Navigation.findNavController(activity, R.id.my_nav_host_fragment).navigate(R.id.action_upcomingBookingsFragment_to_viewBookingFragment, bundle);
                    break;

            }
        });

    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView marinaNameTextView, cancelledMsg;
        public TextView dateTextView;
        public LinearLayout bookingCard;

        public MyViewHolder(View view) {
            super(view);
            marinaNameTextView = view.findViewById(R.id.booking_marinaName);
            dateTextView = view.findViewById(R.id.booking_marinaDate);
            bookingCard = view.findViewById(R.id.booking_card);
            cancelledMsg = view.findViewById(R.id.cancelled_msg);
        }
    }
}

