package com.navismart.navismart.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.navismart.navismart.R;
import com.navismart.navismart.model.AdminVerificationModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminVerificationAdapter extends RecyclerView.Adapter<AdminVerificationAdapter.AdminVerificationViewHolder> {
    Context context;
    List<AdminVerificationModel> list;
    DatabaseReference databaseReference;
    FirebaseFirestore firestore;
    ArrayList<String> marinaUIDList;

    public AdminVerificationAdapter(Context context, List<AdminVerificationModel> list) {
        this.context = context;
        this.list = list;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();
        marinaUIDList = new ArrayList<>();
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

        holder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Verify Marina");
                alert.setMessage("Are you sure you want to approve this marina?");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference reference = databaseReference.child("admin").child("verification");
                        reference.child(verificationModel.getMarinaUID()).setValue(null);
                        DatabaseReference ref = databaseReference.child("users").child(verificationModel.getMarinaUID()).child("profile");
                        ref.child("status").setValue("approved");

                        double latitude = verificationModel.getLatitude();
                        double longitude = verificationModel.getLongitude();

                        // i is the greatest multiple of 5 less than the value of latitude.
                        int i = (int) (latitude / 10);
                        int temp = ((int) latitude) % 10;
                        if (temp < 5) i = i * 10;
                        else i = (i * 10) + 5;

                        int j = (int) (longitude / 10);
                        temp = ((int) longitude) % 10;
                        if (temp < 5) j = j * 10;
                        else j = (j * 10) + 5;


                        DocumentReference location = firestore.collection("Location").document(i + "," + j);
                        location.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot doc = task.getResult();
                                    marinaUIDList = (ArrayList<String>) doc.get("Marina List");
                                    marinaUIDList.add(verificationModel.getMarinaUID());
                                    Map<String, ArrayList<String>> map = new HashMap<>();
                                    map.put("Marina List", marinaUIDList);
                                    location.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                        }
                                    })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context, "Can't add location in databse.", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                }
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Can't add location in database.", Toast.LENGTH_LONG).show();
                                    }
                                });


                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // close dialog
                        dialog.cancel();
                    }
                });
                alert.show();

            }

        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Verify Marina");
                alert.setMessage("Are you sure you want to reject this marina?");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference reference = databaseReference.child("admin").child("verification");
                        reference.child(verificationModel.getMarinaUID()).setValue(null);
                        DatabaseReference ref = databaseReference.child("users").child(verificationModel.getMarinaUID()).child("profile");
                        ref.child("status").setValue("rejected");
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                        StorageReference deleteFile = storageReference.child("users").child(verificationModel.getMarinaUID());
                        deleteFile.delete();
                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // close dialog
                        dialog.cancel();
                    }
                });
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AdminVerificationViewHolder extends RecyclerView.ViewHolder {
        TextView marinaName, marinaManagerName, email, receptionCapacity, address, description, termsAndCondition;
        Button approve, reject;

        public AdminVerificationViewHolder(@NonNull View itemView) {
            super(itemView);
            marinaName = itemView.findViewById(R.id.admin_verification_marina_name);
            marinaManagerName = itemView.findViewById(R.id.admin_verification_marina_manager_name);
            email = itemView.findViewById(R.id.admin_verification_email);
            receptionCapacity = itemView.findViewById(R.id.admin_verification_reception_capacity);
            address = itemView.findViewById(R.id.admin_verification_address);
            description = itemView.findViewById(R.id.admin_verification_description);
            termsAndCondition = itemView.findViewById(R.id.admin_verification_tnc);
            approve = itemView.findViewById(R.id.admin_verification_approve);
            reject = itemView.findViewById(R.id.admin_verification_reject);
        }
    }
}
