package com.navismart.navismart.adapters;

import android.app.Activity;
import android.os.Bundle;
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

import androidx.navigation.Navigation;

import static com.navismart.navismart.MainActivity.USER_TYPE;
import static com.navismart.navismart.adapters.ChatAdapter.SENDER_MARINA;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.MyViewHolder> {

    private List<ReviewModel> reviewList;
    private Activity activity;

    public ReviewListAdapter(List<ReviewModel> reviewList, Activity activity) {
        this.reviewList = reviewList;
        this.activity = activity;
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
        holder.initialView.setText(reviewModel.getReviewerName().toUpperCase().charAt(0) + "");
        if (!reviewModel.getReply().trim().isEmpty()) {
            holder.replyTextView.setText(reviewModel.getReply());
            holder.replyTextView.setVisibility(View.VISIBLE);
        } else {
            holder.replyTextView.setVisibility(View.GONE);
        }
        if (!reviewModel.getReply().trim().isEmpty()) {
            holder.replyBrick.setVisibility(View.VISIBLE);
        } else {
            holder.replyBrick.setVisibility(View.GONE);
        }
        if (USER_TYPE == SENDER_MARINA) {
            holder.writeReplyOption.setVisibility(View.VISIBLE);
        } else {
            holder.writeReplyOption.setVisibility(View.GONE);
        }
        holder.writeReplyOption.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("reviewModel", reviewModel);
            Navigation.findNavController(activity, R.id.my_nav_host_fragment).navigate(R.id.action_viewReviewFragment_to_writeReviewReplyFragment, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameView, dateView, reviewText, initialView, writeReplyOption, replyTextView;
        public RatingBar ratingView;
        public View replyBrick;

        public MyViewHolder(View view) {
            super(view);
            nameView = view.findViewById(R.id.reviewer_name);
            dateView = view.findViewById(R.id.review_date);
            reviewText = view.findViewById(R.id.reviewTextView);
            ratingView = view.findViewById(R.id.rating_display_box);
            initialView = view.findViewById(R.id.initial_text_view);
            writeReplyOption = view.findViewById(R.id.write_reply_option);
            replyTextView = view.findViewById(R.id.replyTextView);
            replyBrick = view.findViewById(R.id.replyBrick);
        }
    }
}
