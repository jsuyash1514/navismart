package com.navismart.navismart.view;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.navismart.navismart.R;
import com.navismart.navismart.adapters.ReviewListAdapter;
import com.navismart.navismart.model.ReviewModel;
import com.navismart.navismart.viewmodelfactory.ReviewListViewModelFactory;
import com.navismart.navismart.viewmodels.ReviewListViewModel;

import java.util.ArrayList;
import java.util.Collections;

public class ViewReviewFragment extends Fragment {

    DividerItemDecoration itemDecoration;
    private RecyclerView reviewRecyclerView;
    private TextView noReview;

    public ViewReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_review, container, false);

        reviewRecyclerView = view.findViewById(R.id.review_recycler_view);
        noReview = view.findViewById(R.id.no_review_text);
        itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL);

        ReviewListViewModel reviewListViewModel = ViewModelProviders.of(this, new ReviewListViewModelFactory(getArguments().getString("marinaID"))).get(ReviewListViewModel.class);
        LiveData<DataSnapshot> liveData = reviewListViewModel.getDataSnapshotLiveData();
        liveData.observe(this, dataSnapshot -> {

            ArrayList<ReviewModel> reviewList = new ArrayList<>();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                reviewList.add(snapshot.getValue(ReviewModel.class));
            }
            if (reviewList.size() > 0) {
                Collections.reverse(reviewList);
                ReviewListAdapter reviewListAdapter = new ReviewListAdapter(reviewList, getActivity());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                reviewRecyclerView.setLayoutManager(mLayoutManager);
                reviewRecyclerView.setItemAnimator(new DefaultItemAnimator());
                reviewRecyclerView.setAdapter(reviewListAdapter);
                reviewRecyclerView.addItemDecoration(itemDecoration);
                reviewRecyclerView.setVisibility(View.VISIBLE);
                noReview.setVisibility(View.GONE);
            } else {
                reviewRecyclerView.setVisibility(View.GONE);
                noReview.setVisibility(View.VISIBLE);
            }

        });

        return view;
    }

}
