package com.navismart.navismart.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.navismart.navismart.R;

public class MarinaImagesAdapter extends RecyclerView.Adapter<MarinaImagesAdapter.MyViewHolder> {

    FirebaseAuth auth;
    private int n = 0;
    private StorageReference storageReference;
    private Context context;
    private String marinaUID;

    public MarinaImagesAdapter(int n, String marinaUID, Context context) {
        this.n = n;
        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        this.context = context;
        this.marinaUID = marinaUID;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.images_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        StorageReference profilePicRef = storageReference.child("users").child(marinaUID).child("marina" + (position + 1));
        profilePicRef.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(context)
                .load(uri)
                .into(holder.marinaImage));

    }

    @Override
    public int getItemCount() {
        return n;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView marinaImage;

        public MyViewHolder(View view) {
            super(view);
            marinaImage = view.findViewById(R.id.marina_image);
        }
    }
}
