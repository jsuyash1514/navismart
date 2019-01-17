package com.navismart.navismart.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.navismart.navismart.R;
import com.navismart.navismart.model.ReviewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.MyViewHolder> {

    private List<ReviewModel> reviewList;

    public ReviewListAdapter(List<ReviewModel> reviewList) {
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

        String dateStr = reviewModel.getReviewDate();
        SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        df.setTimeZone(TimeZone.getDefault());
        String formattedDate = df.format(date);
        holder.dateView.setText(formattedDate);
        holder.ratingView.setRating(Float.parseFloat(reviewModel.getStarRating()));
        holder.reviewText.setText(reviewModel.getReview());
        holder.initialView.setText(reviewModel.getReviewerName().toUpperCase().charAt(0)+"");
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameView, dateView, reviewText, initialView;
        public RatingBar ratingView;

        public MyViewHolder(View view) {
            super(view);
            nameView = view.findViewById(R.id.reviewer_name);
            dateView = view.findViewById(R.id.review_date);
            reviewText = view.findViewById(R.id.reviewTextView);
            ratingView = view.findViewById(R.id.rating_display_box);
            initialView = view.findViewById(R.id.initial_text_view);
        }
    }
}
