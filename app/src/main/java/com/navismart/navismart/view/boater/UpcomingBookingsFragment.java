package com.navismart.navismart.view.boater;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.navismart.navismart.R;
import com.navismart.navismart.adapters.BookingListAdapter;
import com.navismart.navismart.model.BookingModel;
import com.navismart.navismart.viewmodels.BookingListViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.navismart.navismart.MainActivity.getCountOfDays;

public class UpcomingBookingsFragment extends Fragment {

    private ArrayList<BookingModel> list;
    private RecyclerView upcomingRecyclerView;
    private TextView noBookingTextView;

    public UpcomingBookingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upcoming_bookings, container, false);

        upcomingRecyclerView = view.findViewById(R.id.upcoming_bookings_recyclerView);
        noBookingTextView = view.findViewById(R.id.no_booking_display);

        prepareList();

        return view;
    }

    private void checkVisibility() {

        if (list.size() > 0) {
            upcomingRecyclerView.setVisibility(View.VISIBLE);
            noBookingTextView.setVisibility(View.GONE);
        } else {
            upcomingRecyclerView.setVisibility(View.GONE);
            noBookingTextView.setVisibility(View.VISIBLE);
        }

    }

    private void prepareList() {

        Bitmap image = Bitmap.createBitmap(150, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawColor(Color.GRAY);

        BookingListViewModel bookingListViewModel = ViewModelProviders.of(this).get(BookingListViewModel.class);
        LiveData<DataSnapshot> liveData = bookingListViewModel.getDataSnapshotLiveData();
        liveData.observe(this, dataSnapshot -> {
            if (dataSnapshot != null) {
                list = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    BookingModel booking = postSnapshot.getValue(BookingModel.class);
                    if (isUpcoming(booking.getFromDate())) {
                        booking.setBookingTense(BookingModel.UPCOMING);
                        list.add(booking);
                    }
                }
                BookingListAdapter bookingListAdapter = new BookingListAdapter(getActivity(), list);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                upcomingRecyclerView.setLayoutManager(mLayoutManager);
                upcomingRecyclerView.setItemAnimator(new DefaultItemAnimator());
                upcomingRecyclerView.setAdapter(bookingListAdapter);
                checkVisibility();
            }
        });

    }

    private boolean isUpcoming(String from) {

        Date date = Calendar.getInstance().getTime();

        String curr = date.getDate() + "/" + (date.getMonth() + 1) + "/" + (date.getYear() + 1900);

        int dF = getCountOfDays(curr, from);

        if (dF > 0) {
            return true;
        }

        return false;
    }

}
