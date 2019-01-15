package com.navismart.navismart.view;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.navismart.navismart.R;
import com.navismart.navismart.Triplet;
import com.navismart.navismart.adapters.MarinaActivityAdapter;
import com.navismart.navismart.model.MarinaActivityModel;
import com.navismart.navismart.model.MarinaActivityNewBookingsCardModel;
import com.navismart.navismart.model.MarinaActivityNewReviewsCardModel;
import com.navismart.navismart.viewmodels.MarinaLandingActivityViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static android.text.format.DateUtils.MINUTE_IN_MILLIS;

public class MarinaLandingActivityFragment extends Fragment {
    private MarinaLandingActivityViewModel viewModel;
    private List<MarinaActivityModel> list;
    private List<Triplet<Long, Integer, String>> tripletList;
    private MarinaActivityAdapter adapter;
    private RecyclerView recyclerView;

    public MarinaLandingActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_marina_landing_activity, container, false);
        viewModel = ViewModelProviders.of(this).get(MarinaLandingActivityViewModel.class);
        recyclerView = view.findViewById(R.id.marina_activity_recyclerview);

        fetchData();

        return view;
    }

    private void fetchData() {
        LiveData<DataSnapshot> liveData = viewModel.getDataSnapshotLiveData();
        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    long time = cal.getTimeInMillis();
                    list = new ArrayList<>();
                    tripletList = new ArrayList<>();
                    adapter = new MarinaActivityAdapter(getActivity(), getContext(), list);
                    for (DataSnapshot snapshot : dataSnapshot.child("bookings").getChildren()) {
                        if (snapshot != null) {
                            tripletList.add(new Triplet<Long, Integer, String>((long) snapshot.child("bookingDate").getValue(), 1, snapshot.child("bookingID").getValue(String.class)));
                        }
                    }
                    for (DataSnapshot snapshot : dataSnapshot.child("review").getChildren()) {
                        if (snapshot != null) {
                            tripletList.add(new Triplet<Long, Integer, String>((long) snapshot.child("timeStamp").getValue(), 2, snapshot.getKey()));
                        }
                    }
                    Collections.sort(tripletList, new Comparator<Triplet<Long, Integer, String>>() {
                        @Override
                        public int compare(Triplet<Long, Integer, String> object1, Triplet<Long, Integer, String> object2) {
                            return (int) (object2.getFirst() - object1.getFirst());
                        }
                    });
                    for (int i = 0; i < tripletList.size(); i++) {
                        if (tripletList.get(i).getSecond() == 1) {
                            String dateStr = dataSnapshot.child("bookings").child(tripletList.get(i).getThird()).child("dateTimeStamp").getValue(String.class);
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
                            MarinaActivityModel dateModel = new MarinaActivityModel(0, formattedDate);
                            MarinaActivityModel modelBooking = new MarinaActivityModel(
                                    1,
                                    new MarinaActivityNewBookingsCardModel(
                                            dataSnapshot.child("bookings").child(tripletList.get(i).getThird()).child("boaterName").getValue(String.class),
                                            String.valueOf(DateUtils.getRelativeTimeSpanString((long) dataSnapshot.child("bookings").child(tripletList.get(i).getThird()).child("bookingDate").getValue(), time, MINUTE_IN_MILLIS)),
                                            dataSnapshot.child("bookings").child(tripletList.get(i).getThird()).child("boatName").getValue(String.class),
                                            dataSnapshot.child("bookings").child(tripletList.get(i).getThird()).child("boatID").getValue(String.class),
                                            dataSnapshot.child("bookings").child(tripletList.get(i).getThird()).child("fromDate").getValue(String.class),
                                            dataSnapshot.child("bookings").child(tripletList.get(i).getThird()).child("toDate").getValue(String.class),
                                            dataSnapshot.child("bookings").child(tripletList.get(i).getThird()).child("bookingID").getValue(String.class),
                                            String.valueOf(dataSnapshot.child("bookings").child(tripletList.get(i).getThird()).child("finalPrice").getValue())
                                    ));
                            list.add(dateModel);
                            list.add(modelBooking);
                            adapter.notifyDataSetChanged();
                        } else if (tripletList.get(i).getSecond() == 2) {
                            String dateStr = dataSnapshot.child("review").child(tripletList.get(i).getThird()).child("reviewDate").getValue(String.class);
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
                            MarinaActivityModel dateModel = new MarinaActivityModel(0, formattedDate);
                            MarinaActivityModel modelReview = new MarinaActivityModel(
                                    2,
                                    new MarinaActivityNewReviewsCardModel(
                                            dataSnapshot.child("review").child(tripletList.get(i).getThird()).child("reviewerName").getValue(String.class),
                                            String.valueOf(dataSnapshot.child("review").child(tripletList.get(i).getThird()).child("starRating").getValue()),
                                            String.valueOf(DateUtils.getRelativeTimeSpanString((long) dataSnapshot.child("review").child(tripletList.get(i).getThird()).child("timeStamp").getValue(), time, MINUTE_IN_MILLIS))
                                    ));
                            list.add(dateModel);
                            list.add(modelReview);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    RecyclerView.LayoutManager recycler = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(recycler);
                    recyclerView.setAdapter(adapter);
                }
            }
        });
    }

}
