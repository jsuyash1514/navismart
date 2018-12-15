package com.navismart.navismart.view;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.navismart.navismart.R;

import java.util.Calendar;

public class BoaterSearchFragment extends Fragment {

    private ImageView datePickFromImageView;
    private ImageView datePickToImageView;
    private int mYearFrom, mMonthFrom, mDateFrom;
    private int mYearTo, mMonthTo, mDateTo;
    private EditText dateFromEditText;
    private EditText dateToEditText;

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

        final Calendar c = Calendar.getInstance();
        mYearFrom = c.get(Calendar.YEAR);
        mMonthFrom = c.get(Calendar.MONTH);
        mDateFrom = c.get(Calendar.DATE);
        mYearTo = mYearFrom;
        mMonthTo = mMonthFrom;
        mDateTo = mDateFrom + 1;

        dateFromEditText.setText(mDateFrom + "/" + mMonthFrom + "/" + mYearFrom);
        dateToEditText.setText(mDateTo + "/" + mMonthTo + "/" + mYearTo);

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

        return view;
    }

}
