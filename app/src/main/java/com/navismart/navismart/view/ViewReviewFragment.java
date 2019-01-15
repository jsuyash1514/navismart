package com.navismart.navismart.view;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class ViewReviewFragment extends Fragment {

    DividerItemDecoration itemDecoration;
    private TextView marinaNameTextView;
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

        marinaNameTextView = view.findViewById(R.id.marina_name_textView);
        reviewRecyclerView = view.findViewById(R.id.review_recycler_view);
        noReview = view.findViewById(R.id.no_review_text);

        itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL);

        marinaNameTextView.setText(getArguments().getString("marinaName"));

        ReviewListViewModel reviewListViewModel = ViewModelProviders.of(this, new ReviewListViewModelFactory(getArguments().getString("marinaID"))).get(ReviewListViewModel.class);
        LiveData<DataSnapshot> liveData = reviewListViewModel.getDataSnapshotLiveData();
        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {

                ArrayList<ReviewModel> reviewList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    reviewList.add(snapshot.getValue(ReviewModel.class));
                }
                if (reviewList.size() > 0) {
                    ReviewListAdapter reviewListAdapter = new ReviewListAdapter(reviewList);
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

            }
        });

        return view;
    }

}
