package com.navismart.navismart.view;

import android.app.ProgressDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firestore.admin.v1beta1.Progress;
import com.navismart.navismart.R;
import com.navismart.navismart.adapters.MarinaBookingsAdapter;
import com.navismart.navismart.model.BookingModel;
import com.navismart.navismart.model.MarinaBookingsModel;
import com.navismart.navismart.viewmodels.MarinaLandingBookingViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.navigation.Navigation;

public class MarinaLandingBookingFragment extends Fragment {


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int arrival=0,departure=0,stay=0;
    private TextView bookedCount, availableCount, arrivalCount,departureCount, stayCount;
    private MarinaLandingBookingViewModel viewModel;
    private DatePicker datePicker;
    private List<String> bookingID;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_marina_landing_booking, container, false);

        viewModel = ViewModelProviders.of(this).get(MarinaLandingBookingViewModel.class);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getContext());

        datePicker = view.findViewById(R.id.marina_booking_calender);


        bookedCount = view.findViewById(R.id.marina_booked_count);
        availableCount = view.findViewById(R.id.marina_available_count);
        arrivalCount = view.findViewById(R.id.marina_booking_arrival_count);
        departureCount = view.findViewById(R.id.marina_booking_departure_count);
        stayCount = view.findViewById(R.id.marina_booking_stay_count);
        recyclerView = view.findViewById(R.id.marina_booking_arrival_departure_recyclerview);

        if(viewModel.getYear() == 0){
            viewModel.setYear(datePicker.getYear());
            viewModel.setMonth(datePicker.getMonth()+1);
            viewModel.setDay(datePicker.getDayOfMonth());
        }

        if(viewModel.getCapacity()==0){
            DatabaseReference reference = databaseReference.child("users").child(auth.getCurrentUser().getUid()).child("marina-description");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    viewModel.setCapacity(Integer.parseInt(dataSnapshot.child("capacity").getValue(String.class)));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(),"Some error occured. Please try again!",Toast.LENGTH_LONG).show();
                }
            });
        }

        prepareData();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker dP, int y, int m, int d) {
                viewModel.setYear(y);
                viewModel.setMonth(m+1);
                viewModel.setDay(d);
                prepareData();
            }
        });

        return view;
    }

    public void prepareData(){
        LiveData<DataSnapshot> liveData = viewModel.getDataSnapshotLiveData();
        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    arrival=0;
                    departure=0;
                    stay=0;
                    bookingID = new ArrayList<>();
                    final List<MarinaBookingsModel> list = new ArrayList<>();
                    final MarinaBookingsAdapter adapter = new MarinaBookingsAdapter(getActivity(),list);
                    progressDialog.setMessage("Fetching data...");
                    progressDialog.show();
                    for (DataSnapshot snapshot : dataSnapshot.child(String.valueOf(viewModel.getYear())).child(String.valueOf(viewModel.getMonth())).child(String.valueOf(viewModel.getDay())).getChildren()){
                        if(snapshot!=null && !snapshot.getKey().equals("noOfDocksAvailable")) {
                            MarinaBookingsModel marinaBookingsModel = new MarinaBookingsModel();
                            bookingID.add(snapshot.getKey());
                            String s = snapshot.getValue(String.class);
                            if (s != null && !s.isEmpty()) {
                                if (s.equals("arrival")) {
                                    arrival++;
                                    marinaBookingsModel.setBitmap(R.drawable.ic_marina_booking_arrival_24dp);
                                }
                                else if (s.equals("departure")){
                                    departure++;
                                    marinaBookingsModel.setBitmap(R.drawable.ic_marina_booking_departure_24dp);
                                }
                                else if (s.equals("stay")) {
                                    stay++;
                                    marinaBookingsModel.setBitmap(R.drawable.ic_marina_booking_stay_24dp);
                                }
                            }
                            DatabaseReference ref = databaseReference.child("users").child(auth.getCurrentUser().getUid()).child("bookings");
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                    marinaBookingsModel.setGuestName(dataSnapshot1.child(snapshot.getKey()).child("boaterName").getValue(String.class));
                                    marinaBookingsModel.setArrivingOn(dataSnapshot1.child(snapshot.getKey()).child("fromDate").getValue(String.class));
                                    marinaBookingsModel.setDepartingOn(dataSnapshot1.child(snapshot.getKey()).child("toDate").getValue(String.class));
                                    marinaBookingsModel.setBookingID(dataSnapshot1.child(snapshot.getKey()).child("bookingID").getValue(String.class));
                                    marinaBookingsModel.setBoaterID(dataSnapshot1.child(snapshot.getKey()).child("boaterUID").getValue(String.class));
                                    list.add(marinaBookingsModel);
                                    adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                    bookedCount.setText(String.valueOf(arrival+stay));
                    availableCount.setText(String.valueOf(viewModel.getCapacity()-(arrival+stay)));
                    arrivalCount.setText(String.valueOf(arrival));
                    departureCount.setText(String.valueOf(departure));
                    stayCount.setText(String.valueOf(stay));
                    progressDialog.dismiss();

                    RecyclerView.LayoutManager recycler = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(recycler);
                    recyclerView.setAdapter(adapter);
                }
                else{
                    Log.d("DatePicker","Null datasnapshot");
                }
            }
        });
    }
}
