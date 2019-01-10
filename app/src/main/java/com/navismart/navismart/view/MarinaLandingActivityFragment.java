package com.navismart.navismart.view;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.navismart.navismart.R;
import com.navismart.navismart.adapters.MarinaActivityAdapter;
import com.navismart.navismart.model.MarinaActivityModel;
import com.navismart.navismart.model.MarinaActivityNewBookingsCardModel;
import com.navismart.navismart.model.MarinaActivityNewReviewsCardModel;
import com.navismart.navismart.viewmodels.MarinaLandingActivityViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import static android.text.format.DateUtils.MINUTE_IN_MILLIS;

public class MarinaLandingActivityFragment extends Fragment {
    private MarinaLandingActivityViewModel viewModel;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private List<MarinaActivityModel> list;
    private MarinaActivityAdapter adapter;
    private RecyclerView recyclerView;
    private TextView marinaHeadingDate,newBookingGuestName,newBookingBoatName,newBookingBoatID,newBookingArrivalDate,newBookingDepartureDate,newBookingID,newBookingPrice,newBookingTimestamp;

    public MarinaLandingActivityFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_marina_landing_activity, container, false);
        viewModel = ViewModelProviders.of(this).get(MarinaLandingActivityViewModel.class);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        recyclerView = view.findViewById(R.id.marina_activity_recyclerview);
        marinaHeadingDate = view.findViewById(R.id.marina_activity_date);
        newBookingGuestName = view.findViewById(R.id.marina_activity_new_booking_card_guest_name);
        newBookingBoatName = view.findViewById(R.id.marina_activity_guest_detail_boat_name);
        newBookingBoatID = view.findViewById(R.id.marina_activity_guest_detail_boat_id);
        newBookingArrivalDate = view.findViewById(R.id.marina_activity_guest_detail_arrival);
        newBookingDepartureDate = view.findViewById(R.id.marina_activity_guest_detail_departure);
        newBookingID = view.findViewById(R.id.marina_activity_guest_detail_booking);
        newBookingPrice = view.findViewById(R.id.marina_activity_guest_detail_price);
        newBookingTimestamp = view.findViewById(R.id.marina_activity_new_booking_card_timestamp);

        fetchData();


//        MarinaActivityModel model = new MarinaActivityModel(0,"Today");
//        list.add(model);
//        adapter.notifyDataSetChanged();
//
//        MarinaActivityModel model1 = new MarinaActivityModel(0,"30 Oct 18");
//        list.add(model1);
//        adapter.notifyDataSetChanged();
//
//        MarinaActivityModel modelReview = new MarinaActivityModel(2,new MarinaActivityNewReviewsCardModel("Karthik","3.9","5w ago"));
//        list.add(modelReview);
//        adapter.notifyDataSetChanged();




        return view;
    }

    private void fetchData(){
        LiveData<DataSnapshot> liveData = viewModel.getDataSnapshotLiveData();
        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    long time = cal.getTimeInMillis();
                    list = new ArrayList<>();
                    adapter = new MarinaActivityAdapter(getContext(),list);
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        if(snapshot!=null){
                            MarinaActivityModel modelBooking = new MarinaActivityModel(
                                    1,
                                    new MarinaActivityNewBookingsCardModel(
                                            snapshot.child("boaterName").getValue(String.class),
                                            String.valueOf(DateUtils.getRelativeTimeSpanString((long)snapshot.child("bookingDate").getValue(),time,MINUTE_IN_MILLIS)),
                                            snapshot.child("boatName").getValue(String.class),
                                            snapshot.child("boatID").getValue(String.class),
                                            snapshot.child("fromDate").getValue(String.class),
                                            snapshot.child("toDate").getValue(String.class),
                                            snapshot.child("bookingID").getValue(String.class),
                                            String.valueOf(snapshot.child("finalPrice").getValue())
                            ));
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
