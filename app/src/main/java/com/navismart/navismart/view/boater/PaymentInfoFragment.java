package com.navismart.navismart.view.boater;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.navismart.navismart.model.BookingModel;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import static com.navismart.navismart.MainActivity.getDateFromString;
import static com.navismart.navismart.view.boater.BoaterSearchResultsFragment.fromDate;
import static com.navismart.navismart.view.boater.BoaterSearchResultsFragment.noOfDocks;
import static com.navismart.navismart.view.boater.BoaterSearchResultsFragment.toDate;

public class PaymentInfoFragment extends Fragment {

    private CardInputWidget cardInputWidget;
    private Button checkoutButton, cancelButton, confirmButton;
    private TextView paymentMsgView;
    private String price;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private String stripeKey = "pk_test_bLbofd7OxiPMUbqIixXHoWlB", marinaUID = "";
    private Date date;
    private Long receptionCapacity;
    private Card card;
    private BookingModel bookingModel;

    public PaymentInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_info, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        bookingModel = getArguments().getParcelable("bookingModel");
        marinaUID = getArguments().getString("marinaUID", marinaUID);
        receptionCapacity = getArguments().getLong("receptionCapacity");

        Dialog confirmDialog = new Dialog(getContext());
        confirmDialog.setContentView(R.layout.confirm_payment_layout);

        cancelButton = confirmDialog.findViewById(R.id.cancel_action);
        confirmButton = confirmDialog.findViewById(R.id.confirm_action);
        paymentMsgView = confirmDialog.findViewById(R.id.confirm_msg);

        cardInputWidget = view.findViewById(R.id.card_input_widget);
        checkoutButton = view.findViewById(R.id.checkout_button);

        checkoutButton.setOnClickListener(v -> {
            card = cardInputWidget.getCard();
            if (card == null) {
                Toast.makeText(getContext(), "Invalid Card Data", Toast.LENGTH_SHORT).show();
                cardInputWidget.requestFocus();
            } else {
                paymentMsgView.setText("Your Card will be charged with €" + bookingModel.getFinalPrice());
                confirmDialog.show();
            }
        });

        cancelButton.setOnClickListener(v -> {
            confirmDialog.dismiss();
            Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigateUp();
        });

        confirmButton.setOnClickListener(v -> {
            chargeCard(card);
            uploadBooking();
            confirmDialog.dismiss();
        });

        return view;
    }

    private void uploadBooking() {

        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.startFragment, true)
                .build();

        ///////////////////////////////////////////////////////ADD TO USER BOOKING/////////////////////////////////////////////////////////////////////////////////////////////////
        databaseReference.child("users").child(auth.getCurrentUser().getUid()).child("bookings").child(bookingModel.getBookingID()).setValue(bookingModel)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Unable to complete booking! Sorry for the inconvenience!", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_paymentInfoFragment_to_boaterLandingFragment, null, navOptions);
                    }
                });

        /////////////////////////////////////////////////////////ADD TO BOOKINGS PARENT/////////////////////////////////////////////////////////////////////////////////////////////

        Date startDate = getDateFromString(fromDate);
        Log.d("booking date", "startDate: " + startDate);

        Date endDate = getDateFromString(toDate);
        Log.d("date", "endDate: " + endDate);

        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        DatabaseReference ref = databaseReference.child("bookings").child(marinaUID);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(String.valueOf(start.getTime().getYear() + 1900)).child(String.valueOf(start.getTime().getMonth() + 1)).child(String.valueOf(start.getTime().getDate())).child("noOfDocksAvailable").getValue() == null) {
                    databaseReference.child("bookings").child(marinaUID).child(String.valueOf(start.getTime().getYear() + 1900)).child(String.valueOf(start.getTime().getMonth() + 1)).child(String.valueOf(start.getTime().getDate())).child("noOfDocksAvailable").setValue(receptionCapacity - noOfDocks)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Unable to complete booking! Sorry for the inconvenience!", Toast.LENGTH_SHORT).show();
                                    Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_paymentInfoFragment_to_boaterLandingFragment, null, navOptions);
                                }
                            });
                } else {
                    long temp = (long) dataSnapshot.child(String.valueOf(start.getTime().getYear() + 1900)).child(String.valueOf(start.getTime().getMonth() + 1)).child(String.valueOf(start.getTime().getDate())).child("noOfDocksAvailable").getValue();
                    databaseReference.child("bookings").child(marinaUID).child(String.valueOf(start.getTime().getYear() + 1900)).child(String.valueOf(start.getTime().getMonth() + 1)).child(String.valueOf(start.getTime().getDate())).child("noOfDocksAvailable").setValue(temp - noOfDocks)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Unable to complete booking! Sorry for the inconvenience!", Toast.LENGTH_SHORT).show();
                                    Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_paymentInfoFragment_to_boaterLandingFragment, null, navOptions);
                                }
                            });
                }
                databaseReference.child("bookings").child(marinaUID).child(String.valueOf(start.getTime().getYear() + 1900)).child(String.valueOf(start.getTime().getMonth() + 1)).child(String.valueOf(start.getTime().getDate())).child(bookingModel.getBookingID()).setValue("arrival")
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Unable to complete booking! Sorry for the inconvenience!", Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_paymentInfoFragment_to_boaterLandingFragment, null, navOptions);
                            }
                        });
                start.add(Calendar.DATE, 1);

                for (date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
                    if (dataSnapshot.child(String.valueOf(date.getYear() + 1900)).child(String.valueOf(date.getMonth() + 1)).child(String.valueOf(date.getDate())).child("noOfDocksAvailable").getValue() == null) {
                        databaseReference.child("bookings").child(marinaUID).child(String.valueOf(date.getYear() + 1900)).child(String.valueOf(date.getMonth() + 1)).child(String.valueOf(date.getDate())).child("noOfDocksAvailable").setValue(receptionCapacity - noOfDocks)
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Unable to complete booking! Sorry for the inconvenience!", Toast.LENGTH_SHORT).show();
                                        Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_paymentInfoFragment_to_boaterLandingFragment, null, navOptions);
                                    }
                                });
                    } else {
                        long temp = (long) dataSnapshot.child(String.valueOf(date.getYear() + 1900)).child(String.valueOf(date.getMonth() + 1)).child(String.valueOf(date.getDate())).child("noOfDocksAvailable").getValue();
                        databaseReference.child("bookings").child(marinaUID).child(String.valueOf(date.getYear() + 1900)).child(String.valueOf(date.getMonth() + 1)).child(String.valueOf(date.getDate())).child("noOfDocksAvailable").setValue(temp - noOfDocks)
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Unable to complete booking! Sorry for the inconvenience!", Toast.LENGTH_SHORT).show();
                                        Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_paymentInfoFragment_to_boaterLandingFragment, null, navOptions);
                                    }
                                });
                    }
                    databaseReference.child("bookings").child(marinaUID).child(String.valueOf(date.getYear() + 1900)).child(String.valueOf(date.getMonth() + 1)).child(String.valueOf(date.getDate())).child(bookingModel.getBookingID()).setValue("stay")
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Unable to complete booking! Sorry for the inconvenience!", Toast.LENGTH_SHORT).show();
                                    Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_paymentInfoFragment_to_boaterLandingFragment, null, navOptions);
                                }
                            });
                }

                if (dataSnapshot.child(String.valueOf(start.getTime().getYear() + 1900)).child(String.valueOf(start.getTime().getMonth() + 1)).child(String.valueOf(start.getTime().getDate())).child("noOfDocksAvailable").getValue() == null) {
                    databaseReference.child("bookings").child(marinaUID).child(String.valueOf(start.getTime().getYear() + 1900)).child(String.valueOf(start.getTime().getMonth() + 1)).child(String.valueOf(start.getTime().getDate())).child("noOfDocksAvailable").setValue(receptionCapacity - noOfDocks)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Unable to complete booking! Sorry for the inconvenience!", Toast.LENGTH_SHORT).show();
                                    Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_paymentInfoFragment_to_boaterLandingFragment, null, navOptions);
                                }
                            });
                } else {
                    long temp = (long) dataSnapshot.child(String.valueOf(start.getTime().getYear() + 1900)).child(String.valueOf(start.getTime().getMonth() + 1)).child(String.valueOf(start.getTime().getDate())).child("noOfDocksAvailable").getValue();
                    databaseReference.child("bookings").child(marinaUID).child(String.valueOf(start.getTime().getYear() + 1900)).child(String.valueOf(start.getTime().getMonth() + 1)).child(String.valueOf(start.getTime().getDate())).child("noOfDocksAvailable").setValue(temp - noOfDocks)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Unable to complete booking! Sorry for the inconvenience!", Toast.LENGTH_SHORT).show();
                                    Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_paymentInfoFragment_to_boaterLandingFragment, null, navOptions);
                                }
                            });
                }
                databaseReference.child("bookings").child(marinaUID).child(String.valueOf(start.getTime().getYear() + 1900)).child(String.valueOf(start.getTime().getMonth() + 1)).child(String.valueOf(start.getTime().getDate())).child(bookingModel.getBookingID()).setValue("departure")
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Unable to complete booking! Sorry for the inconvenience!", Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_paymentInfoFragment_to_boaterLandingFragment, null, navOptions);
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Unable to complete booking! Sorry for the inconvenience!", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_paymentInfoFragment_to_boaterLandingFragment, null, navOptions);
            }
        });

        ////////////////////////////////////////////////////ADD TO MARINA MANAGER BOOKING////////////////////////////////////////////////////////////////////////////////////////////

        databaseReference.child("users").child(marinaUID).child("bookings").child(bookingModel.getBookingID()).setValue(bookingModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Booking Successfully completed!", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_paymentInfoFragment_to_boaterLandingFragment, null, navOptions);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Unable to complete booking! Sorry for the inconvenience!", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_paymentInfoFragment_to_boaterLandingFragment, null, navOptions);
                    }
                });
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    }

    private void chargeCard(Card card) {

        Stripe stripe = new Stripe(getContext(), stripeKey);
        stripe.createToken(card, new TokenCallback() {
            @Override
            public void onError(Exception error) {
                Log.d("TOKEN ERROR", error.toString());
            }

            @Override
            public void onSuccess(Token token) {
                Map<String, Object> params = new HashMap<>();
                params.put("amount", bookingModel.getFinalPrice());
                params.put("currency", "eur");
                params.put("descriptiopn", "Example Charge");
                params.put("source", token);
                Map<String, String> metadata = new HashMap<>();
                metadata.put("booking_id", bookingModel.getBookingID());
                params.put("metadata", metadata);
                try {
                    Charge charge = Charge.create(params);
                } catch (StripeException e) {
                    Log.d("STRIPE EXCEPTION", e.toString());
                }
            }

        });

    }

}
