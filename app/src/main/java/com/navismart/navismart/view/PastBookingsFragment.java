package com.navismart.navismart.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.navismart.navismart.R;
import com.navismart.navismart.adapters.BookingListAdapter;
import com.navismart.navismart.model.BookingModel;
import com.navismart.navismart.model.MarinaModel;

import java.util.ArrayList;

public class PastBookingsFragment extends Fragment {

    private ArrayList<BookingModel> list;
    private RecyclerView pastRecyclerView;
    private BookingListAdapter bookingListAdapter;
    private TextView noBookingTextView;
    private ImageView reloadIcon;
    private int a = 0;

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
        checkVisibility();

        reloadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareList();
                checkVisibility();
                bookingListAdapter.notifyDataSetChanged();
            }
        });

        bookingListAdapter = new BookingListAdapter(getActivity(), list);
        pastRecyclerView.setItemAnimator(new DefaultItemAnimator());
        pastRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        pastRecyclerView.setAdapter(bookingListAdapter);

        return view;
    }

    private void checkVisibility(){

        if(list.size() > 0){
            pastRecyclerView.setVisibility(View.VISIBLE);
            noBookingTextView.setVisibility(View.GONE);
        }
        else {
            pastRecyclerView.setVisibility(View.GONE);
            noBookingTextView.setVisibility(View.VISIBLE);
        }

    }

    private void prepareList(){

        Bitmap image = Bitmap.createBitmap(150, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawColor(Color.GRAY);


        String t = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer consequat, mi a blandit auctor, massa dui sollicitudin lectus, id vestibulum sapien nisl at mi. Pellentesque laoreet dapibus ipsum vel fermentum. ";
        String d = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer consequat, mi a blandit auctor, massa dui sollicitudin lectus, id vestibulum sapien nisl at mi. Pellentesque laoreet dapibus ipsum vel fermentum. ";
        if(a == 0){

            list = new ArrayList<>();

            list.add(new BookingModel(new MarinaModel("Hello", image, "2.0", "default", 5.0f, 1, true, d, t, new int[]{1, 2, 3}),"12/12/18","14/12/18",BookingModel.PAST));
            list.add(new BookingModel(new MarinaModel("Hello1", image, "5.0", "default", 2.0f, 2, false, d, t, new int[]{0, 1, 2}),"12/12/18","14/12/18",BookingModel.PAST));
            list.add(new BookingModel(new MarinaModel("Hello2", image, "3.0", "default", 1.0f, 3, false, d, t, new int[]{1, 3}),"12/12/18","14/12/18",BookingModel.PAST));
            list.add(new BookingModel(new MarinaModel("Hello3", image, "1.0", "default", 4.0f, 4, true, d, t, new int[]{7, 1, 0}),"12/12/18","14/12/18",BookingModel.PAST));
            list.add(new BookingModel(new MarinaModel("Hello4", image, "4.0", "default", 3.0f, 5, true, d, t, new int[]{1, 8, 6, 0, 4}),"12/12/18","14/12/18",BookingModel.PAST));

        }

        if(a>0){
            list.add(new BookingModel(new MarinaModel("Hello"+Integer.toString(a+4), image, "4.0", "default", 3.0f, 5, true, d, t, new int[]{1, 8, 6, 0, 4}),"12/12/18","14/12/18",BookingModel.PAST));
        }

        a++;

    }

}
