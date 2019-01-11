package com.navismart.navismart.view;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.navismart.navismart.R;
import com.navismart.navismart.adapters.ReviewListAdapter;
import com.navismart.navismart.model.MarinaModel;
import com.navismart.navismart.model.ReviewModel;
import com.navismart.navismart.viewmodelfactory.ReviewListViewModelFactory;
import com.navismart.navismart.viewmodels.ReviewListViewModel;

import java.util.ArrayList;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import static com.navismart.navismart.view.BoaterSearchResultsFragment.fromDate;
import static com.navismart.navismart.view.BoaterSearchResultsFragment.toDate;

public class MarinaPageFragment extends Fragment {

    private TextView nameTextView, fromDateTextView, distSearchTextView, locationTextView, toDateTextView, descriptionTextView, facilitiesTextView, tNcTextView;
    private TextView seeMoreReviewsTextView;
    private RatingBar ratingBar;
    private Button bookButton;
    private Button sendMsgButton;
    private ImageView marinaImageView;
    private NavController navController;
    private RecyclerView reviewListView;
    private ArrayList<ReviewModel> reviewList;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private View reviewTab;

    public MarinaPageFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_marina_page, container, false);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        nameTextView = view.findViewById(R.id.marina_name_textView);
        fromDateTextView = view.findViewById(R.id.from_date_display_textView);
        toDateTextView = view.findViewById(R.id.to_date_display_textView);
        distSearchTextView = view.findViewById(R.id.dist_from_search_display);
        locationTextView = view.findViewById(R.id.location_display_textView);
        descriptionTextView = view.findViewById(R.id.description_display_textView);
        facilitiesTextView = view.findViewById(R.id.facilities_display_textView);
        tNcTextView = view.findViewById(R.id.tnc_display_textView);
        ratingBar = view.findViewById(R.id.marina_rating_bar);
        marinaImageView = view.findViewById(R.id.marina_imageView);
        bookButton = view.findViewById(R.id.book_button);
        reviewListView = view.findViewById(R.id.review_list);
        reviewTab = view.findViewById(R.id.review_tab);
        seeMoreReviewsTextView = view.findViewById(R.id.see_more_reviews_text);
        sendMsgButton = view.findViewById(R.id.send_msg_button);
        navController = Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment);

        MarinaModel marinaModel = getArguments().getParcelable("marina_model");
        Bundle bundle = new Bundle();
        bundle.putParcelable("marina_model", marinaModel);

        loadReviews(marinaModel.getMarinaUID());

        nameTextView.setText(marinaModel.getName());
        ratingBar.setRating(marinaModel.getRating());
        locationTextView.setText(marinaModel.getLocation());
        distSearchTextView.setText(Integer.toString((int) Math.round(marinaModel.getDistFromSearch())) + " km from searched location");
        descriptionTextView.setText(marinaModel.getDescription());
        facilitiesTextView.setText(marinaModel.getFacilityString());
        tNcTextView.setText(marinaModel.getTnc());
        marinaImageView.setImageBitmap(marinaModel.getImage());
        fromDateTextView.setText(fromDate);
        toDateTextView.setText(toDate);

        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_marinaPageFragment_to_checkoutFragment, bundle);
            }
        });

        seeMoreReviewsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("marinaID", marinaModel.getMarinaUID());
                bundle.putString("marinaName", marinaModel.getName());
                Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_marinaPageFragment_to_viewReviewFragment, bundle);

            }
        });

        sendMsgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("marinaName", marinaModel.getName());
                bundle.putString("boaterName", auth.getCurrentUser().getDisplayName());
                bundle.putString("marinaID", marinaModel.getMarinaUID());
                Log.d("marinaID marinaPage", marinaModel.getMarinaUID());
                bundle.putString("boaterID", auth.getCurrentUser().getUid());
                Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.action_marinaPageFragment_to_chatFragment, bundle);
            }
        });

        return view;
    }

    private void loadReviews(String marinaID) {

        ReviewListViewModel reviewListViewModel = ViewModelProviders.of(this, new ReviewListViewModelFactory(marinaID)).get(ReviewListViewModel.class);
        LiveData<DataSnapshot> liveData = reviewListViewModel.getDataSnapshotLiveData();
        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                ArrayList<ReviewModel> initReview = new ArrayList<>();
                Log.d("DataSnapshot", dataSnapshot.toString());
                if (dataSnapshot != null) {
                    reviewList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        reviewList.add(snapshot.getValue(ReviewModel.class));
                    }

                    if (reviewList.size() >= 2) {
                        initReview.add(reviewList.get(0));
                        initReview.add(reviewList.get(1));
                    } else if (reviewList.size() == 1) {
                        initReview.add(reviewList.get(0));
                    } else {
                        reviewTab.setVisibility(View.GONE);
                    }

                    if (initReview.size() > 0) {
                        ReviewListAdapter reviewListAdapter = new ReviewListAdapter(initReview);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                        reviewListView.setLayoutManager(mLayoutManager);
                        reviewListView.setItemAnimator(new DefaultItemAnimator());
                        reviewListView.setAdapter(reviewListAdapter);
                        reviewTab.setVisibility(View.VISIBLE);
                    }

                }
            }
        });

    }

}
