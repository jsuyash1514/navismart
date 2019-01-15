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
import com.navismart.navismart.adapters.MarinaActivityAdapter;
import com.navismart.navismart.model.MarinaActivityModel;
import com.navismart.navismart.model.MarinaActivityNewBookingsCardModel;
import com.navismart.navismart.viewmodels.MarinaLandingActivityViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static android.text.format.DateUtils.MINUTE_IN_MILLIS;

public class MarinaLandingActivityFragment extends Fragment {
    private MarinaLandingActivityViewModel viewModel;
    private List<MarinaActivityModel> list;
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

//        MarinaActivityModel model1 = new MarinaActivityModel(0,"30 Oct 18");
//        list.add(model1);
//        adapter.notifyDataSetChanged();
//
//        MarinaActivityModel modelReview = new MarinaActivityModel(2,new MarinaActivityNewReviewsCardModel("Karthik","3.9","5w ago"));
//        list.add(modelReview);
//        adapter.notifyDataSetChanged();

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
                    adapter = new MarinaActivityAdapter(getActivity(),getContext(), list);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot != null) {
                            String dateStr = snapshot.child("dateTimeStamp").getValue(String.class);
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
                                            snapshot.child("boaterName").getValue(String.class),
                                            String.valueOf(DateUtils.getRelativeTimeSpanString((long) snapshot.child("bookingDate").getValue(), time, MINUTE_IN_MILLIS)),
                                            snapshot.child("boatName").getValue(String.class),
                                            snapshot.child("boatID").getValue(String.class),
                                            snapshot.child("fromDate").getValue(String.class),
                                            snapshot.child("toDate").getValue(String.class),
                                            snapshot.child("bookingID").getValue(String.class),
                                            String.valueOf(snapshot.child("finalPrice").getValue())
                                    ));
                            list.add(dateModel);
                            list.add(modelBooking);
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
