package com.navismart.navismart.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.navismart.navismart.R;
import com.navismart.navismart.model.MarinaModel;

import java.util.List;

import androidx.navigation.Navigation;

public class MarinaListAdapter extends RecyclerView.Adapter<MarinaListAdapter.MyViewHolder> {

    private List<MarinaModel> marinaList;
    private Activity activity;

    public MarinaListAdapter(Activity activity, List<MarinaModel> marinaList) {
        this.marinaList = marinaList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.marina_detail_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        MarinaModel marinaModel = marinaList.get(position);
        holder.ratingBar.setRating(marinaModel.getRating());
        holder.marinaPriceTextView.setText(marinaModel.getPrice());
        holder.marinaLocationTextView.setText(marinaModel.getLocation());
        holder.marinaNameTextView.setText(marinaModel.getName());
        holder.marinaImageView.setImageBitmap(marinaModel.getImage());
        holder.marinaDistaFromCityTextView.setText(Float.toString(marinaModel.getDistFromCity()) + " km from city center");

        holder.seeMarinaDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("marina_model", marinaModel);
                Navigation.findNavController(activity, R.id.my_nav_host_fragment).navigate(R.id.action_boaterSearchResultsFragment_to_marinaPageFragment, bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return marinaList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView marinaImageView;
        public TextView marinaNameTextView, marinaLocationTextView, marinaDistaFromCityTextView, marinaPriceTextView;
        public RatingBar ratingBar;
        public Button seeMarinaDetailsButton;


        public MyViewHolder(View view) {
            super(view);
            marinaImageView = view.findViewById(R.id.marina_imageView);
            marinaDistaFromCityTextView = view.findViewById(R.id.dist_from_city_display);
            marinaLocationTextView = view.findViewById(R.id.location_display_card_textView);
            marinaNameTextView = view.findViewById(R.id.marina_name_textView);
            marinaPriceTextView = view.findViewById(R.id.price_textView);
            ratingBar = view.findViewById(R.id.rating_bar);
            seeMarinaDetailsButton = view.findViewById(R.id.see_details_button);
        }
    }
}
