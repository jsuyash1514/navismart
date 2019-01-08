package com.navismart.navismart.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.navismart.navismart.R;
import com.navismart.navismart.model.ReviewModel;

import java.util.List;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.MyViewHolder> {

    private List<ReviewModel> reviewList;

    public ReviewListAdapter(List<ReviewModel> boatList) {
        this.reviewList = reviewList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_brick, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        ReviewModel reviewModel = reviewList.get(position);
        holder.nameView.setText(reviewModel.getReviewerName());
        holder.dateView.setText(reviewModel.getReviewDate());
        holder.ratingView.setText(reviewModel.getStarRating());
        holder.reviewText.setText(reviewModel.getReview());

    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameView, dateView, reviewText, ratingView;

        public MyViewHolder(View view) {
            super(view);
            nameView = view.findViewById(R.id.reviewer_name);
            dateView = view.findViewById(R.id.review_date);
            reviewText = view.findViewById(R.id.reviewTextView);
            ratingView = view.findViewById(R.id.rating_display_box);
        }
    }
}
