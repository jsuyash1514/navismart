package com.navismart.navismart.view;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.navismart.navismart.R;

import java.util.Calendar;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import static android.app.Activity.RESULT_OK;
import static com.navismart.navismart.MainActivity.getCountOfDays;

public class BoaterSearchFragment extends Fragment {

    int PLACE_PICKER_REQUEST = 1;
    private String locationAddress;
    private LatLng locationLatLng;
    private ImageView datePickFromImageView;
    private ImageView datePickToImageView;
    private int mYearFrom, mMonthFrom, mDateFrom;
    private int mYearTo, mMonthTo, mDateTo;
    private EditText dateFromEditText;
    private EditText dateToEditText;
    private EditText locationEditText;
    private Button searchButton;
    private String fromDate, toDate;
    private NumberPicker noOfDockPicker;

    public BoaterSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_boater_search, container, false);

        dateFromEditText = view.findViewById(R.id.date_display_from_editText);
        dateToEditText = view.findViewById(R.id.date_display_to_editText);
        locationEditText = view.findViewById(R.id.location_search_editText);
        noOfDockPicker = view.findViewById(R.id.no_of_dock_picker);
        noOfDockPicker.setMaxValue(10);
        noOfDockPicker.setMinValue(1);

        final Calendar c = Calendar.getInstance();
        mYearFrom = c.get(Calendar.YEAR);
        mMonthFrom = c.get(Calendar.MONTH);
        mDateFrom = c.get(Calendar.DATE);
        c.add(Calendar.DATE, 1);
        mYearTo = c.get(Calendar.YEAR);
        mMonthTo = c.get(Calendar.MONTH);
        mDateTo = c.get(Calendar.DATE);

        dateFromEditText.setText(mDateFrom + "/" + (mMonthFrom + 1) + "/" + mYearFrom);
        dateToEditText.setText(mDateTo + "/" + (mMonthTo + 1) + "/" + mYearTo);

        locationEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        fromDate = mDateFrom + "/" + (mMonthFrom + 1) + "/" + mYearFrom;
        toDate = mDateTo + "/" + (mMonthTo + 1) + "/" + mYearTo;


        datePickFromImageView = view.findViewById(R.id.date_pick_from_icon);
        datePickFromImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialogFrom = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        mYearFrom = year;
                        mMonthFrom = month;
                        mDateFrom = dayOfMonth;
                        dateFromEditText.setText(mDateFrom + "/" + (mMonthFrom + 1) + "/" + mYearFrom);
                        fromDate = mDateFrom + "/" + (mMonthFrom + 1) + "/" + mYearFrom;

                    }
                }, mYearFrom, mMonthFrom, mDateFrom);
                datePickerDialogFrom.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                datePickerDialogFrom.show();

            }
        });

        datePickToImageView = view.findViewById(R.id.date_pick_to_icon);
        datePickToImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialogTo = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        mYearTo = year;
                        mMonthTo = month;
                        mDateTo = dayOfMonth;
                        dateToEditText.setText(mDateTo + "/" + (mMonthTo + 1) + "/" + mYearTo);
                        toDate = mDateTo + "/" + (mMonthTo + 1) + "/" + mYearTo;
                        if (getCountOfDays(fromDate, toDate) < 0) {
                            Toast.makeText(getContext(), "Departure Date cannout be earlier than Arrival Date!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, mYearTo, mMonthTo, mDateTo);
                datePickerDialogTo.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                datePickerDialogTo.show();

            }
        });

        searchButton = view.findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getCountOfDays(fromDate, toDate) < 0) {
                    Toast.makeText(getContext(), "Departure Date cannout be earlier than Arrival Date!", Toast.LENGTH_SHORT).show();
                } else if (locationAddress != null && !locationAddress.trim().isEmpty()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("fromDate", dateFromEditText.getText().toString());
                    bundle.putString("toDate", dateToEditText.getText().toString());
                    bundle.putString("location_address", locationAddress);
                    bundle.putInt("noOfDocks", noOfDockPicker.getValue());
                    bundle.putParcelable("locationLatLng", locationLatLng);
                    NavController navController = Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment);
                    navController.navigate(R.id.action_boaterLandingFragment_to_boaterSearchResultsFragment, bundle);
                } else {
                    Toast.makeText(getContext(), "Enter Search location!", Toast.LENGTH_SHORT).show();

                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(data, getContext());
            locationAddress = place.getAddress().toString();
            locationEditText.setText(locationAddress);
            locationLatLng = place.getLatLng();
        }
    }

}
