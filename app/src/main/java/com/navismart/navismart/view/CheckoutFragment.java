package com.navismart.navismart.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.navismart.navismart.R;
import com.navismart.navismart.model.MarinaModel;

public class CheckoutFragment extends Fragment {

    private TextView marinaNameTextView, fromDateTextView, toDateTextView, priceDisplayTextView;
    private Spinner boatSelectSpinner;
    private String boatNames[];
    private ArrayAdapter<String> dropDownAdapter;

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

        boatNames = new String[]{"Karaboudjan", "Serenity", "Orion"};
        dropDownAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, boatNames);
        boatSelectSpinner.setAdapter(dropDownAdapter);

        MarinaModel marinaModel = getArguments().getParcelable("marina_model");

        return view;
    }

}
