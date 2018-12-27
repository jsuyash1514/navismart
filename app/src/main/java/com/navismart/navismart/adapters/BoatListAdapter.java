package com.navismart.navismart.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.navismart.navismart.R;
import com.navismart.navismart.model.BoatModel;

import java.util.List;

public class BoatListAdapter extends RecyclerView.Adapter<BoatListAdapter.MyViewHolder> {

    private List<BoatModel> boatList;

    public BoatListAdapter(List<BoatModel> boatList) {
        this.boatList = boatList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.boat_data_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        BoatModel boatModel = boatList.get(position);
        holder.typeView.setText("Type : " + boatModel.getType());
        holder.nameView.setText(boatModel.getName());
        holder.lengthView.setText("Length : " + Float.toString(boatModel.getLength()) + " m");
        holder.beamView.setText("Beam : " + Float.toString(boatModel.getBeam()) + " m");
        holder.idView.setText("ID : " + boatModel.getId());

    }

    @Override
    public int getItemCount() {
        return boatList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameView, idView, lengthView, beamView, typeView;

        public MyViewHolder(View view) {
            super(view);
            nameView = view.findViewById(R.id.boat_name);
            idView = view.findViewById(R.id.boat_id_display);
            lengthView = view.findViewById(R.id.boat_length_display);
            beamView = view.findViewById(R.id.boat_beam_display);
            typeView = view.findViewById(R.id.boat_type_display);
        }
    }
}
