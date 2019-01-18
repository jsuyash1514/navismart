package com.navismart.navismart.view;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.navismart.navismart.R;
import com.navismart.navismart.model.BoatModel;
import com.navismart.navismart.model.BookingModel;
import com.navismart.navismart.model.MarinaModel;
import com.navismart.navismart.viewmodels.BoatListViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import static com.navismart.navismart.MainActivity.getCountOfDays;
import static com.navismart.navismart.MainActivity.getDateFromString;
import static com.navismart.navismart.view.BoaterSearchResultsFragment.fromDate;
import static com.navismart.navismart.view.BoaterSearchResultsFragment.noOfDocks;
import static com.navismart.navismart.view.BoaterSearchResultsFragment.toDate;

public class CheckoutFragment extends Fragment {

    NavOptions navOptions;
    private TextView marinaNameTextView, fromDateTextView, toDateTextView, priceDisplayTextView;
    private Spinner boatSelectSpinner;
    private String boatNames[];
    private ArrayAdapter<String> dropDownAdapter;
    private Button checkoutButton;
    private ArrayList<BoatModel> boatModelsList;
    private DatabaseReference databaseUserReference, databaseReference;
    private FirebaseAuth auth;
    private String boaterName = "";
    private long receptionCapacity;
    private Date startDate, endDate, date;

    public CheckoutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);

        marinaNameTextView = view.findViewById(R.id.marina_name_checkout);
        fromDateTextView = view.findViewById(R.id.from_date_checkout_textView);
        toDateTextView = view.findViewById(R.id.to_date_checkout_textView);
        priceDisplayTextView = view.findViewById(R.id.checkout_priceDisplay_textView);
        boatSelectSpinner = view.findViewById(R.id.boatSelectSpinner);
        checkoutButton = view.findViewById(R.id.checkout_button);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseUserReference = FirebaseDatabase.getInstance().getReference().child("users").child(auth.getCurrentUser().getUid()).child("profile").child("name");

        prepareBoatList();


        fromDateTextView.setText(fromDate);
        toDateTextView.setText(toDate);

        navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.startFragment, true)
                .build();

        MarinaModel marinaModel = getArguments().getParcelable("marina_model");
        marinaNameTextView.setText(marinaModel.getName());
        priceDisplayTextView.setText(Float.toString(Float.parseFloat(marinaModel.getPrice()) * getCountOfDays(fromDate, toDate)));
        receptionCapacity = marinaModel.getReceptionCapacity();

        checkoutButton.setOnClickListener((View v) -> {
            String boat = boatSelectSpinner.getSelectedItem().toString();
            BoatModel bM = new BoatModel();
            for (BoatModel boatModel : boatModelsList) {

                if (boatModel.getName().equals(boat)) {

                    bM = boatModel;
                    break;

                }

            }

            BookingModel bookingModel = new BookingModel(bM.getName(), marinaModel.getName(), bM.getId(), fromDate, toDate, BookingModel.UPCOMING, boaterName);

            String bookingUID = UUID.randomUUID().toString();
            bookingModel.setBookingID(bookingUID);
            bookingModel.setMarinaUID(marinaModel.getMarinaUID());
            bookingModel.setNoOfDocks(noOfDocks);
            bookingModel.setBoaterUID(auth.getCurrentUser().getUid());
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            long time = cal.getTimeInMillis();
            bookingModel.setBookingDate(time);
            SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            String gmtTime = df.format(new Date());
            bookingModel.setDateTimeStamp(gmtTime);

            ///////////////////////////////////////////////////////ADD TO USER BOOKING/////////////////////////////////////////////////////////////////////////////////////////////////
            databaseReference.child("users").child(auth.getCurrentUser().getUid()).child("bookings").child(bookingUID).setValue(bookingModel)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Unable to complete booking! Sorry for the inconvenience!", Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_checkoutFragment_to_boaterLandingFragment, null, navOptions);
                        }
                    });

            /////////////////////////////////////////////////////////ADD TO BOOKINGS PARENT/////////////////////////////////////////////////////////////////////////////////////////////

            startDate = getDateFromString(fromDate);
            Log.d("booking date", "startDate: " + startDate);

            endDate = getDateFromString(toDate);
            Log.d("date", "endDate: " + endDate);

            Calendar start = Calendar.getInstance();
            start.setTime(startDate);
            Calendar end = Calendar.getInstance();
            end.setTime(endDate);

            DatabaseReference ref = databaseReference.child("bookings").child(marinaModel.getMarinaUID());
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(String.valueOf(start.getTime().getYear() + 1900)).child(String.valueOf(start.getTime().getMonth() + 1)).child(String.valueOf(start.getTime().getDate())).child("noOfDocksAvailable").getValue() == null){
                        databaseReference.child("bookings").child(marinaModel.getMarinaUID()).child(String.valueOf(start.getTime().getYear() + 1900)).child(String.valueOf(start.getTime().getMonth() + 1)).child(String.valueOf(start.getTime().getDate())).child("noOfDocksAvailable").setValue(receptionCapacity-noOfDocks)
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Unable to complete booking! Sorry for the inconvenience!", Toast.LENGTH_SHORT).show();
                                        Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_checkoutFragment_to_boaterLandingFragment, null, navOptions);
                                    }
                                });
                    }
                    else{
                        long temp = (long)dataSnapshot.child(String.valueOf(start.getTime().getYear() + 1900)).child(String.valueOf(start.getTime().getMonth() + 1)).child(String.valueOf(start.getTime().getDate())).child("noOfDocksAvailable").getValue();
                        databaseReference.child("bookings").child(marinaModel.getMarinaUID()).child(String.valueOf(start.getTime().getYear() + 1900)).child(String.valueOf(start.getTime().getMonth() + 1)).child(String.valueOf(start.getTime().getDate())).child("noOfDocksAvailable").setValue(temp-noOfDocks)
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Unable to complete booking! Sorry for the inconvenience!", Toast.LENGTH_SHORT).show();
                                        Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_checkoutFragment_to_boaterLandingFragment, null, navOptions);
                                    }
                                });
                    }
                    databaseReference.child("bookings").child(marinaModel.getMarinaUID()).child(String.valueOf(start.getTime().getYear() + 1900)).child(String.valueOf(start.getTime().getMonth() + 1)).child(String.valueOf(start.getTime().getDate())).child(bookingUID).setValue("arrival")
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Unable to complete booking! Sorry for the inconvenience!", Toast.LENGTH_SHORT).show();
                                    Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_checkoutFragment_to_boaterLandingFragment, null, navOptions);
                                }
                            });
                    start.add(Calendar.DATE, 1);

                    for (date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
                        if(dataSnapshot.child(String.valueOf(date.getYear() + 1900)).child(String.valueOf(date.getMonth() + 1)).child(String.valueOf(date.getDate())).child("noOfDocksAvailable").getValue() == null){
                            databaseReference.child("bookings").child(marinaModel.getMarinaUID()).child(String.valueOf(date.getYear() + 1900)).child(String.valueOf(date.getMonth() + 1)).child(String.valueOf(date.getDate())).child("noOfDocksAvailable").setValue(receptionCapacity-noOfDocks)
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), "Unable to complete booking! Sorry for the inconvenience!", Toast.LENGTH_SHORT).show();
                                            Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_checkoutFragment_to_boaterLandingFragment, null, navOptions);
                                        }
                                    });
                        }
                        else{
                            long temp = (long)dataSnapshot.child(String.valueOf(date.getYear() + 1900)).child(String.valueOf(date.getMonth() + 1)).child(String.valueOf(date.getDate())).child("noOfDocksAvailable").getValue();
                            databaseReference.child("bookings").child(marinaModel.getMarinaUID()).child(String.valueOf(date.getYear() + 1900)).child(String.valueOf(date.getMonth() + 1)).child(String.valueOf(date.getDate())).child("noOfDocksAvailable").setValue(temp-noOfDocks)
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), "Unable to complete booking! Sorry for the inconvenience!", Toast.LENGTH_SHORT).show();
                                            Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_checkoutFragment_to_boaterLandingFragment, null, navOptions);
                                        }
                                    });
                        }
                        databaseReference.child("bookings").child(marinaModel.getMarinaUID()).child(String.valueOf(date.getYear() + 1900)).child(String.valueOf(date.getMonth() + 1)).child(String.valueOf(date.getDate())).child(bookingUID).setValue("stay")
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Unable to complete booking! Sorry for the inconvenience!", Toast.LENGTH_SHORT).show();
                                        Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_checkoutFragment_to_boaterLandingFragment, null, navOptions);
                                    }
                                });
                    }

                    if(dataSnapshot.child(String.valueOf(start.getTime().getYear() + 1900)).child(String.valueOf(start.getTime().getMonth() + 1)).child(String.valueOf(start.getTime().getDate())).child("noOfDocksAvailable").getValue() == null){
                        databaseReference.child("bookings").child(marinaModel.getMarinaUID()).child(String.valueOf(start.getTime().getYear() + 1900)).child(String.valueOf(start.getTime().getMonth() + 1)).child(String.valueOf(start.getTime().getDate())).child("noOfDocksAvailable").setValue(receptionCapacity-noOfDocks)
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Unable to complete booking! Sorry for the inconvenience!", Toast.LENGTH_SHORT).show();
                                        Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_checkoutFragment_to_boaterLandingFragment, null, navOptions);
                                    }
                                });
                    }
                    else{
                        long temp = (long)dataSnapshot.child(String.valueOf(start.getTime().getYear() + 1900)).child(String.valueOf(start.getTime().getMonth() + 1)).child(String.valueOf(start.getTime().getDate())).child("noOfDocksAvailable").getValue();
                        databaseReference.child("bookings").child(marinaModel.getMarinaUID()).child(String.valueOf(start.getTime().getYear() + 1900)).child(String.valueOf(start.getTime().getMonth() + 1)).child(String.valueOf(start.getTime().getDate())).child("noOfDocksAvailable").setValue(temp-noOfDocks)
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Unable to complete booking! Sorry for the inconvenience!", Toast.LENGTH_SHORT).show();
                                        Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_checkoutFragment_to_boaterLandingFragment, null, navOptions);
                                    }
                                });
                    }
                    databaseReference.child("bookings").child(marinaModel.getMarinaUID()).child(String.valueOf(start.getTime().getYear() + 1900)).child(String.valueOf(start.getTime().getMonth() + 1)).child(String.valueOf(start.getTime().getDate())).child(bookingUID).setValue("departure")
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Unable to complete booking! Sorry for the inconvenience!", Toast.LENGTH_SHORT).show();
                                    Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_checkoutFragment_to_boaterLandingFragment, null, navOptions);
                                }
                            });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Unable to complete booking! Sorry for the inconvenience!", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_checkoutFragment_to_boaterLandingFragment, null, navOptions);
                }
            });

            ////////////////////////////////////////////////////ADD TO MARINA MANAGER BOOKING////////////////////////////////////////////////////////////////////////////////////////////

            databaseReference.child("users").child(marinaModel.getMarinaUID()).child("bookings").child(bookingUID).setValue(bookingModel)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), "Booking Successfully completed!", Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_checkoutFragment_to_boaterLandingFragment, null, navOptions);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Unable to complete booking! Sorry for the inconvenience!", Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_checkoutFragment_to_boaterLandingFragment, null, navOptions);
                        }
                    });
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        });

        return view;
    }

    private void prepareBoatList() {
        ArrayList<String> boatNamesList = new ArrayList<>();
        boatModelsList = new ArrayList<>();
        BoatListViewModel boatListViewModel = ViewModelProviders.of(this).get(BoatListViewModel.class);
        LiveData<DataSnapshot> liveData = boatListViewModel.getDataSnapshotLiveData();
        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    BoatModel boat = postSnapshot.getValue(BoatModel.class);
                    boatModelsList.add(boat);
                    boatNamesList.add(boat.getName());
                }
                boatNames = new String[boatNamesList.size()];
                for (int i = 0; i < boatNamesList.size(); i++) {
                    boatNames[i] = boatNamesList.get(i);
                }
                dropDownAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, boatNames);
                boatSelectSpinner.setAdapter(dropDownAdapter);
                checkoutButton.setEnabled(true);
            }
        });

        databaseUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boaterName = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
