package com.navismart.navismart.adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.navismart.navismart.R;
import com.navismart.navismart.model.BoatModel;

import java.util.List;

public class BoatListAdapter extends RecyclerView.Adapter<BoatListAdapter.MyViewHolder> {

    FirebaseAuth auth;
    private List<BoatModel> boatList;
    private DatabaseReference databaseReference;
    private Context context;

    public BoatListAdapter(List<BoatModel> boatList, Context context) {
        this.boatList = boatList;
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(auth.getCurrentUser().getUid()).child("boats");
        this.context = context;
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
        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog deleteDialog = new Dialog(context);
                deleteDialog.setContentView(R.layout.delete_dialog);
                deleteDialog.show();
                Button cancel = deleteDialog.findViewById(R.id.cancel_delete);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteDialog.dismiss();
                    }
                });
                Button delete = deleteDialog.findViewById(R.id.delete_button);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseReference.child("ID " + boatModel.getId()).child("beam").setValue(null);
                        databaseReference.child("ID " + boatModel.getId()).child("id").setValue(null);
                        databaseReference.child("ID " + boatModel.getId()).child("length").setValue(null);
                        databaseReference.child("ID " + boatModel.getId()).child("name").setValue(null);
                        databaseReference.child("ID " + boatModel.getId()).child("type").setValue(null)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context, "Boat deleted successfully.", Toast.LENGTH_SHORT).show();
                                        deleteDialog.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Unable to delete boat.", Toast.LENGTH_SHORT).show();
                                        deleteDialog.dismiss();
                                    }
                                });
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return boatList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameView, idView, lengthView, beamView, typeView;
        public ImageView deleteIcon;

        public MyViewHolder(View view) {
            super(view);
            nameView = view.findViewById(R.id.boat_name);
            idView = view.findViewById(R.id.boat_id_display);
            lengthView = view.findViewById(R.id.boat_length_display);
            beamView = view.findViewById(R.id.boat_beam_display);
            typeView = view.findViewById(R.id.boat_type_display);
            deleteIcon = view.findViewById(R.id.deleteBoat);
        }
    }
}
