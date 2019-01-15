package com.navismart.navismart.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.navismart.navismart.R;
import com.navismart.navismart.model.MarinaActivityModel;

import java.util.List;

import androidx.navigation.Navigation;

public class MarinaActivityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Activity activity;
    Context context;
    List<MarinaActivityModel> list;

    public MarinaActivityAdapter(Activity activity, Context context, List<MarinaActivityModel> list) {
        this.activity = activity;
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case MarinaActivityModel.TYPE_DATE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_marina_activity_date, parent, false);
                return new MarinaActivityDateViewHolder(view);

            case MarinaActivityModel.TYPE_BOOKING:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_marina_activity_new_booking, parent, false);
                return new MarinaActivityBookingsViewHolder(view);

            case MarinaActivityModel.TYPE_REVIEW:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_marina_activity_review, parent, false);
                return new MarinaActivityReviewsViewHolder(view);

            default:
                return null;
        }
    }


    @Override
    public int getItemViewType(int position) {
        switch (list.get(position).getType()) {
            case 0:
                return MarinaActivityModel.TYPE_DATE;
            case 1:
                return MarinaActivityModel.TYPE_BOOKING;
            case 2:
                return MarinaActivityModel.TYPE_REVIEW;
            default:
                return -1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MarinaActivityModel mylist = list.get(position);
        if (mylist != null) {
            if (mylist.getType() == MarinaActivityModel.TYPE_BOOKING) {
                ((MarinaActivityBookingsViewHolder) holder).timeStamp.setText(mylist.getBookingsCardModel().getTimeStamp());
                ((MarinaActivityBookingsViewHolder) holder).guestName.setText(mylist.getBookingsCardModel().getGuestName());
                ((MarinaActivityBookingsViewHolder) holder).boatName.setText(mylist.getBookingsCardModel().getBoatName());
                ((MarinaActivityBookingsViewHolder) holder).boatID.setText(mylist.getBookingsCardModel().getBoatID());
                ((MarinaActivityBookingsViewHolder) holder).arrivalTime.setText(mylist.getBookingsCardModel().getArrivalTime());
                ((MarinaActivityBookingsViewHolder) holder).departureTime.setText(mylist.getBookingsCardModel().getDapartureTime());
                ((MarinaActivityBookingsViewHolder) holder).bookingNumber.setText(mylist.getBookingsCardModel().getBookingNumber());
                ((MarinaActivityBookingsViewHolder) holder).bookingPrice.setText(mylist.getBookingsCardModel().getBookingPrice());
                ((MarinaActivityBookingsViewHolder)holder).newBookingCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("Booking_id",mylist.getBookingsCardModel().getBookingNumber());
                        Navigation.findNavController(activity, R.id.my_nav_host_fragment).navigate(R.id.action_landingFragment_to_bookingDetailsFragment,bundle);
                    }
                });
            } else if (mylist.getType() == MarinaActivityModel.TYPE_REVIEW) {
                ((MarinaActivityReviewsViewHolder) holder).timeStamp.setText(mylist.getReviewsCardModel().getTimeStamp());
                ((MarinaActivityReviewsViewHolder) holder).guestName.setText(mylist.getReviewsCardModel().getGuestName());
                ((MarinaActivityReviewsViewHolder) holder).ratingGiven.setText(mylist.getReviewsCardModel().getRatingGiven());
            } else if (mylist.getType() == MarinaActivityModel.TYPE_DATE) {
                ((MarinaActivityDateViewHolder) holder).dateStamp.setText(mylist.getDate());
            }
        }
    }

    @Override
    public int getItemCount() {
        int arr = 0;
        try {
            if (list.size() == 0) {
                arr = 0;
            } else {
                arr = list.size();
            }
        } catch (Exception e) {

        }
        return arr;
    }

    class MarinaActivityDateViewHolder extends RecyclerView.ViewHolder {
        TextView dateStamp;

        public MarinaActivityDateViewHolder(@NonNull View itemView) {
            super(itemView);
            dateStamp = itemView.findViewById(R.id.marina_activity_date);
        }
    }

    class MarinaActivityBookingsViewHolder extends RecyclerView.ViewHolder {
        TextView timeStamp, guestName, boatName, boatID, arrivalTime, departureTime, bookingNumber, bookingPrice;
        CardView newBookingCardView;

        public MarinaActivityBookingsViewHolder(@NonNull View itemView) {
            super(itemView);
            timeStamp = (itemView).findViewById(R.id.marina_activity_new_booking_card_timestamp);
            guestName = (itemView).findViewById(R.id.marina_activity_new_booking_card_guest_name);
            boatName = (itemView).findViewById(R.id.marina_activity_guest_detail_boat_name);
            boatID = (itemView).findViewById(R.id.marina_activity_guest_detail_boat_id);
            arrivalTime = (itemView).findViewById(R.id.marina_activity_guest_detail_arrival);
            departureTime = (itemView).findViewById(R.id.marina_activity_guest_detail_departure);
            bookingNumber = (itemView).findViewById(R.id.marina_activity_guest_detail_booking);
            bookingPrice = (itemView).findViewById(R.id.marina_activity_guest_detail_price);
            newBookingCardView = (itemView).findViewById(R.id.marina_activity_new_booking_card_view);
        }
    }

    class MarinaActivityReviewsViewHolder extends RecyclerView.ViewHolder {
        TextView timeStamp, guestName, ratingGiven;

        public MarinaActivityReviewsViewHolder(@NonNull View itemView) {
            super(itemView);
            timeStamp = itemView.findViewById(R.id.marina_activity_new_reviews_card_timestamp);
            guestName = itemView.findViewById(R.id.marina_activity_new_review_card_guest_name);
            ratingGiven = itemView.findViewById(R.id.marina_activity_review_rating);
        }
    }
}
