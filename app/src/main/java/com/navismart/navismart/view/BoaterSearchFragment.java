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

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.navismart.navismart.R;

import java.util.Calendar;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import static android.app.Activity.RESULT_OK;

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

        final Calendar c = Calendar.getInstance();
        mYearFrom = c.get(Calendar.YEAR);
        mMonthFrom = c.get(Calendar.MONTH);
        mDateFrom = c.get(Calendar.DATE);
        mYearTo = mYearFrom;
        mMonthTo = mMonthFrom;
        mDateTo = mDateFrom + 1;

        dateFromEditText.setText(mDateFrom + "/" + mMonthFrom + "/" + mYearFrom);
        dateToEditText.setText(mDateTo + "/" + mMonthTo + "/" + mYearTo);

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
                        dateFromEditText.setText(mDateFrom + "/" + mMonthFrom + "/" + mYearFrom);

                    }
                }, mYearFrom, mMonthFrom, mDateFrom);

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
                        dateToEditText.setText(mDateTo + "/" + mMonthTo + "/" + mYearTo);

                    }
                }, mYearTo, mMonthTo, mDateTo);

                datePickerDialogTo.show();

            }
        });

        searchButton = view.findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("fromDate", dateFromEditText.getText().toString());
                bundle.putString("toDate", dateToEditText.getText().toString());
                bundle.putString("location_address", locationAddress);
                bundle.putParcelable("locationLatLng", locationLatLng);
                NavController navController = Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment);
                navController.navigate(R.id.action_boaterLandingFragment_to_boaterSearchResultsFragment, bundle);
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
