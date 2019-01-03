package com.navismart.navismart.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.navismart.navismart.R;
import com.navismart.navismart.model.MarinaPicModel;

import java.util.List;

public class MarinaPicAdapter  extends RecyclerView.Adapter<MarinaPicAdapter.MarinaPicViewHolder>{
    List<MarinaPicModel> list;
    Context context;

    public MarinaPicAdapter(Context context, List<MarinaPicModel> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MarinaPicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_marina_pics, parent, false);
        return new MarinaPicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarinaPicViewHolder marinaPicViewHolder, int position) {
        MarinaPicModel marinaPicModel = list.get(position);
        marinaPicViewHolder.marinaPic.setImageBitmap(marinaPicModel.getPic());

    }

    @Override
    public int getItemCount() {
        int arr = 0;
        try {
            if (list.size() == 0) {
                arr = 0;
            } else {
                arr = list.size();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arr;
    }

    class MarinaPicViewHolder extends RecyclerView.ViewHolder{
        private ImageView marinaPic;

        public MarinaPicViewHolder(@NonNull View itemView) {
            super(itemView);
            marinaPic = (itemView).findViewById(R.id.list_item_marina_pic);
        }
    }
}
