package com.navismart.navismart.view;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.navismart.navismart.R;
import com.navismart.navismart.adapters.BookingListAdapter;
import com.navismart.navismart.model.BookingModel;
import com.navismart.navismart.viewmodels.BookingListViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UpcomingBookingsFragment extends Fragment {

    private ArrayList<BookingModel> list;
    private RecyclerView upcomingRecyclerView;
    private BookingListAdapter bookingListAdapter;
    private TextView noBookingTextView;
    private ImageView reloadIcon;

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
        reloadIcon = view.findViewById(R.id.reload_icon);

        prepareList();

//        bookingListAdapter = new BookingListAdapter(getActivity(), list);
//        upcomingRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        upcomingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        upcomingRecyclerView.setAdapter(bookingListAdapter);

        reloadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareList();
                bookingListAdapter.notifyDataSetChanged();
            }
        });

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


//        String t = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer consequat, mi a blandit auctor, massa dui sollicitudin lectus, id vestibulum sapien nisl at mi. Pellentesque laoreet dapibus ipsum vel fermentum. ";
//        String d = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer consequat, mi a blandit auctor, massa dui sollicitudin lectus, id vestibulum sapien nisl at mi. Pellentesque laoreet dapibus ipsum vel fermentum. ";
//
//        list = new ArrayList<>();
//
//
//        list.add(new BookingModel("boatName", "marinaName", "boatID", "12/12/18", "14/12/18", BookingModel.UPCOMING, "Name"));
//        list.add(new BookingModel("boatName", "marinaName", "boatID", "12/12/18", "14/12/18", BookingModel.UPCOMING, "Name"));
//        list.add(new BookingModel("boatName", "marinaName", "boatID", "12/12/18", "14/12/18", BookingModel.UPCOMING, "Name"));
//        list.add(new BookingModel("boatName", "marinaName", "boatID", "12/12/18", "14/12/18", BookingModel.UPCOMING, "Name"));
//        list.add(new BookingModel("boatName", "marinaName", "boatID", "12/12/18", "14/12/18", BookingModel.UPCOMING, "Name"));


        BookingListViewModel bookingListViewModel = ViewModelProviders.of(this).get(BookingListViewModel.class);
        LiveData<DataSnapshot> liveData = bookingListViewModel.getDataSnapshotLiveData();
        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
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
            }
        });

    }

    private boolean isUpcoming(String from) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {

            Date dateFrom = dateFormat.parse(from);

            Date currDate = Calendar.getInstance().getTime();

            long diffFrom = currDate.getTime() - dateFrom.getTime();
            if (diffFrom < 0) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
