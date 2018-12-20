package com.navismart.navismart.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.navismart.navismart.R;
import com.navismart.navismart.adapters.MarinaBookingsAdapter;
import com.navismart.navismart.model.MarinaBookingsModel;

import java.util.ArrayList;
import java.util.List;

public class MarinaLandingBookingFragment extends Fragment {


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    public MarinaLandingBookingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MarinaLandingBookingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MarinaLandingBookingFragment newInstance(String param1, String param2) {
        MarinaLandingBookingFragment fragment = new MarinaLandingBookingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_marina_landing_booking, container, false);


        RecyclerView recyclerView = view.findViewById(R.id.marina_booking_arrival_departure_recyclerview);
        final List<MarinaBookingsModel> list = new ArrayList<>();
        final MarinaBookingsAdapter adapter = new MarinaBookingsAdapter(getContext(),list);

        // Dummy data for bookings fragment recycler view.
        for (int i=0;i<5;i++) {
            MarinaBookingsModel bookingsModel = new MarinaBookingsModel(R.drawable.ic_marina_booking_arrival_24dp, "Suyash Jain", "29/12/2018", "02/01/2019");
            list.add(bookingsModel);
            adapter.notifyDataSetChanged();
        }

        RecyclerView.LayoutManager recycler = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(recycler);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
