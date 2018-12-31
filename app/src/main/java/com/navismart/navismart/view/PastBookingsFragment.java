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

public class PastBookingsFragment extends Fragment {

    private ArrayList<BookingModel> list;
    private RecyclerView pastRecyclerView;
    private BookingListAdapter bookingListAdapter;
    private TextView noBookingTextView;
    private ImageView reloadIcon;

    public PastBookingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_past_bookings, container, false);

        pastRecyclerView = view.findViewById(R.id.past_bookings_recyclerView);
        noBookingTextView = view.findViewById(R.id.no_booking_display);
        reloadIcon = view.findViewById(R.id.reload_icon);


        prepareList();

        reloadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareList();
                bookingListAdapter.notifyDataSetChanged();
            }
        });

//        bookingListAdapter = new BookingListAdapter(getActivity(), list);
//        pastRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        pastRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        pastRecyclerView.setAdapter(bookingListAdapter);

        return view;
    }

    private void checkVisibility() {

        if (list.size() > 0) {
            pastRecyclerView.setVisibility(View.VISIBLE);
            noBookingTextView.setVisibility(View.GONE);
        } else {
            pastRecyclerView.setVisibility(View.GONE);
            noBookingTextView.setVisibility(View.VISIBLE);
        }

    }

    private void prepareList() {

        Bitmap image = Bitmap.createBitmap(150, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawColor(Color.GRAY);


//        String t = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer consequat, mi a blandit auctor, massa dui sollicitudin lectus, id vestibulum sapien nisl at mi. Pellentesque laoreet dapibus ipsum vel fermentum. ";
//        String d = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer consequat, mi a blandit auctor, massa dui sollicitudin lectus, id vestibulum sapien nisl at mi. Pellentesque laoreet dapibus ipsum vel fermentum. ";

//        list.add(new BookingModel("boatName", "marinaName", "boatID", "12/12/18", "14/12/18", BookingModel.PAST, "Name"));
//        list.add(new BookingModel("boatName", "marinaName", "boatID", "12/12/18", "14/12/18", BookingModel.PAST, "Name"));
//        list.add(new BookingModel("boatName", "marinaName", "boatID", "12/12/18", "14/12/18", BookingModel.PAST, "Name"));
//        list.add(new BookingModel("boatName", "marinaName", "boatID", "12/12/18", "14/12/18", BookingModel.PAST, "Name"));
//        list.add(new BookingModel("boatName", "marinaName", "boatID", "12/12/18", "14/12/18", BookingModel.PAST, "Name"));

        BookingListViewModel bookingListViewModel = ViewModelProviders.of(this).get(BookingListViewModel.class);
        LiveData<DataSnapshot> liveData = bookingListViewModel.getDataSnapshotLiveData();
        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    list = new ArrayList<>();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        BookingModel booking = postSnapshot.getValue(BookingModel.class);
                        if (isPast(booking.getToDate())) {
                            booking.setBookingTense(BookingModel.PAST);
                            list.add(booking);
                        }
                    }
                    BookingListAdapter bookingListAdapter = new BookingListAdapter(getActivity(), list);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                    pastRecyclerView.setLayoutManager(mLayoutManager);
                    pastRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    pastRecyclerView.setAdapter(bookingListAdapter);
                    checkVisibility();
                }
            }
        });

    }

    private boolean isPast(String to) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {

            Date dateTo = dateFormat.parse(to);

            Date currDate = Calendar.getInstance().getTime();

            long diffTo = currDate.getTime() - dateTo.getTime();
            if (diffTo > 0) {
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
