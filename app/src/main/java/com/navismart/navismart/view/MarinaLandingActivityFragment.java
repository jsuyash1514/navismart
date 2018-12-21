package com.navismart.navismart.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.navismart.navismart.R;
import com.navismart.navismart.adapters.MarinaActivityAdapter;
import com.navismart.navismart.model.MarinaActivityModel;
import com.navismart.navismart.model.MarinaActivityNewBookingsCardModel;
import com.navismart.navismart.model.MarinaActivityNewReviewsCardModel;

import java.util.ArrayList;
import java.util.List;

public class MarinaLandingActivityFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public MarinaLandingActivityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MarinaLandingActivityFragment.
     */
    public static MarinaLandingActivityFragment newInstance(String param1, String param2) {
        MarinaLandingActivityFragment fragment = new MarinaLandingActivityFragment();
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
        View view = inflater.inflate(R.layout.fragment_marina_landing_activity, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.marina_activity_recyclerview);
        final List<MarinaActivityModel> list = new ArrayList<>();
        final MarinaActivityAdapter adapter = new MarinaActivityAdapter(getContext(),list);

        MarinaActivityModel model = new MarinaActivityModel(0,"Today");
        list.add(model);
        adapter.notifyDataSetChanged();

        MarinaActivityModel modelBooking = new MarinaActivityModel(1,new MarinaActivityNewBookingsCardModel("Suyash","49m ago","Aquaqueen","P3EF9J","30 Dec 18","2 Jan 18","4827","$100"));
        list.add(modelBooking);
        adapter.notifyDataSetChanged();

        MarinaActivityModel model1 = new MarinaActivityModel(0,"30 Oct 18");
        list.add(model1);
        adapter.notifyDataSetChanged();

        MarinaActivityModel modelReview = new MarinaActivityModel(2,new MarinaActivityNewReviewsCardModel("Karthik","3.9","5w ago"));
        list.add(modelReview);
        adapter.notifyDataSetChanged();

        RecyclerView.LayoutManager recycler = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(recycler);
        recyclerView.setAdapter(adapter);


        return view;
    }

}
