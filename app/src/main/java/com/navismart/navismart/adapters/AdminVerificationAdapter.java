package com.navismart.navismart.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.navismart.navismart.R;
import com.navismart.navismart.model.AdminVerificationModel;

import java.util.List;

public class AdminVerificationAdapter extends RecyclerView.Adapter<AdminVerificationAdapter.AdminVerificationViewHolder> {
    Context context;
    List<AdminVerificationModel> list;

    public AdminVerificationAdapter(Context context, List<AdminVerificationModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AdminVerificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_admin_verification, parent, false);

        return new AdminVerificationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminVerificationViewHolder holder, int i) {
        AdminVerificationModel verificationModel = list.get(i);

        holder.marinaName.setText(verificationModel.getMarinaName());
        holder.marinaManagerName.setText(verificationModel.getMarinaManagerName());
        holder.email.setText(verificationModel.getEmail());
        holder.receptionCapacity.setText(verificationModel.getReceptionCapacity());
        holder.address.setText(verificationModel.getLocationAddress());
        holder.description.setText(verificationModel.getDescription());
        holder.termsAndCondition.setText(verificationModel.getTermsAndCondition());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AdminVerificationViewHolder extends RecyclerView.ViewHolder {
        TextView marinaName, marinaManagerName, email, receptionCapacity, address, description, termsAndCondition;

        public AdminVerificationViewHolder(@NonNull View itemView) {
            super(itemView);
            marinaName = itemView.findViewById(R.id.admin_verification_marina_name);
            marinaManagerName = itemView.findViewById(R.id.admin_verification_marina_manager_name);
            email = itemView.findViewById(R.id.admin_verification_email);
            receptionCapacity = itemView.findViewById(R.id.admin_verification_reception_capacity);
            address = itemView.findViewById(R.id.admin_verification_address);
            description = itemView.findViewById(R.id.admin_verification_description);
            termsAndCondition = itemView.findViewById(R.id.admin_verification_tnc);
        }
    }
}
