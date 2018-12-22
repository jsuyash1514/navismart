package com.navismart.navismart.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.navismart.navismart.R;
import com.navismart.navismart.model.MarinaBookingsModel;

import java.util.List;

public class MarinaBookingsAdapter extends RecyclerView.Adapter<MarinaBookingsAdapter.MarinaBookingsViewHolder> {
    private Context context;
    private List<MarinaBookingsModel> list;

    public MarinaBookingsAdapter(Context context, List<MarinaBookingsModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MarinaBookingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_marina_bookings, parent, false);
        return new MarinaBookingsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarinaBookingsViewHolder marinaBookingsViewHolder, int position) {
        MarinaBookingsModel bookingsModel = list.get(position);
        marinaBookingsViewHolder.icon.setImageResource(bookingsModel.getBitmap());
        marinaBookingsViewHolder.name.setText(bookingsModel.getGuestName());
        marinaBookingsViewHolder.arrivingDate.setText(bookingsModel.getArrivingOn());
        marinaBookingsViewHolder.departingDate.setText(bookingsModel.getDepartingOn());
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
            e.printStackTrace();
        }
        return arr;
    }

    class MarinaBookingsViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name, arrivingDate, departingDate;

        public MarinaBookingsViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.marina_booking_image);
            name = itemView.findViewById(R.id.guest_name);
            arrivingDate = itemView.findViewById(R.id.guest_arriving_on);
            departingDate = itemView.findViewById(R.id.guest_departing_on);
        }
    }
}
