package com.navismart.navismart.view;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.navismart.navismart.MainActivity.getCountOfDays;

public class CurrentBookingsFragment extends Fragment {

    private ArrayList<BookingModel> list;
    private RecyclerView currentRecyclerView;
    private BookingListAdapter bookingListAdapter;
    private TextView noBookingTextView;
    private ImageView reloadIcon;

    public CurrentBookingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_bookings, container, false);

        currentRecyclerView = view.findViewById(R.id.current_bookings_recyclerView);
        noBookingTextView = view.findViewById(R.id.no_booking_display);
        reloadIcon = view.findViewById(R.id.reload_icon);

        prepareList();

        reloadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareList();
            }
        });

//        bookingListAdapter = new BookingListAdapter(getActivity(), list);
//        currentRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        currentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        currentRecyclerView.setAdapter(bookingListAdapter);

        return view;
    }

    private void checkVisibility() {

        if (list.size() > 0) {
            currentRecyclerView.setVisibility(View.VISIBLE);
            noBookingTextView.setVisibility(View.GONE);
        } else {
            currentRecyclerView.setVisibility(View.GONE);
            noBookingTextView.setVisibility(View.VISIBLE);
        }

    }

    private void prepareList() {

//        Bitmap image = Bitmap.createBitmap(150, 100, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(image);
//        canvas.drawColor(Color.GRAY);


//        String t = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer consequat, mi a blandit auctor, massa dui sollicitudin lectus, id vestibulum sapien nisl at mi. Pellentesque laoreet dapibus ipsum vel fermentum. ";
//        String d = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer consequat, mi a blandit auctor, massa dui sollicitudin lectus, id vestibulum sapien nisl at mi. Pellentesque laoreet dapibus ipsum vel fermentum. ";

        list = new ArrayList<>();

//        list.add(new BookingModel(new MarinaModel("Hello", image, "2.0", "default", 5.0f, 1, true, d, t, new int[]{1, 2, 3}),"12/12/18","14/12/18"));
//        list.add(new BookingModel(new MarinaModel("Hello1", image, "5.0", "default", 2.0f, 2, false, d, t, new int[]{0, 1, 2}),"12/12/18","14/12/18"));
//        list.add(new BookingModel(new MarinaModel("Hello2", image, "3.0", "default", 1.0f, 3, false, d, t, new int[]{1, 3}),"12/12/18","14/12/18"));
//        list.add(new BookingModel(new MarinaModel("Hello3", image, "1.0", "default", 4.0f, 4, true, d, t, new int[]{7, 1, 0}),"12/12/18","14/12/18"));
//        list.add(new BookingModel(new MarinaModel("Hello4", image, "4.0", "default", 3.0f, 5, true, d, t, new int[]{1, 8, 6, 0, 4}),"12/12/18","14/12/18"));

        BookingListViewModel bookingListViewModel = ViewModelProviders.of(this).get(BookingListViewModel.class);
        LiveData<DataSnapshot> liveData = bookingListViewModel.getDataSnapshotLiveData();
        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    list = new ArrayList<>();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        BookingModel booking = postSnapshot.getValue(BookingModel.class);
                        if (isCurrent(booking.getFromDate(), booking.getToDate())) {
                            booking.setBookingTense(BookingModel.CURRENT);
                            list.add(booking);
                        }
                    }
                    BookingListAdapter bookingListAdapter = new BookingListAdapter(getActivity(), list);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                    currentRecyclerView.setLayoutManager(mLayoutManager);
                    currentRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    currentRecyclerView.setAdapter(bookingListAdapter);
                    checkVisibility();
                }
            }
        });

    }

    private boolean isCurrent(String from, String to) {

        Date date = Calendar.getInstance().getTime();

        String curr = date.getDate() + "/" + date.getMonth() + "/" + (date.getYear()+1900);

        int dF = getCountOfDays(curr, from);
        int dT = getCountOfDays(curr, to);

        Log.d("DATE DIFF", "dF = " + dF + "dT = " + dT);
        Log.d("curr date", curr);

        if (dF <= 0 && dT >= 0) {
            return true;
        }

        return false;
    }

}
