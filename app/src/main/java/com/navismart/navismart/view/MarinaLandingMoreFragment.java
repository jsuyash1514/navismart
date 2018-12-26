package com.navismart.navismart.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.navismart.navismart.R;

import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

public class MarinaLandingMoreFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FirebaseAuth auth;


    public MarinaLandingMoreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MarinaLandingMoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MarinaLandingMoreFragment newInstance(String param1, String param2) {
        MarinaLandingMoreFragment fragment = new MarinaLandingMoreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_marina_landing_more, container, false);

        auth = FirebaseAuth.getInstance();

        Button logoutButton = view.findViewById(R.id.marina_logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Toast.makeText(getContext(), "Logged out Successful", Toast.LENGTH_SHORT).show();
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.landingFragment, true)
                        .build();
                Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.log_out_action, null, navOptions);
            }
        });
        return view;
    }


}
