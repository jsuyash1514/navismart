package com.navismart.navismart.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.navismart.navismart.R;
import com.navismart.navismart.model.MsgNameModel;

import java.util.List;

import androidx.navigation.Navigation;

public class MarinaMsgAdapter extends RecyclerView.Adapter<MarinaMsgAdapter.MyViewHolder> {

    private List<MsgNameModel> msgNameList;
    private Activity activity;

    public MarinaMsgAdapter(Activity activity, List<MsgNameModel> msgNameList) {
        this.msgNameList = msgNameList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.marina_msg_brick, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        MsgNameModel msgName = msgNameList.get(position);
        holder.msgNameView.setText(msgName.getMsgName());
        holder.initialView.setText(msgName.getMsgName().toUpperCase().charAt(0) + "");
        holder.msgOuterBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("boaterName", msgName.getMsgName());
                bundle.putString("marinaID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                bundle.putString("boaterID", msgName.getBoaterID());
                bundle.putString("marinaName", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                Navigation.findNavController(activity, R.id.my_nav_host_fragment).navigate(R.id.action_landingFragment_to_chatFragment, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return msgNameList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView msgNameView, initialView;
        public View msgOuterBox;

        public MyViewHolder(View view) {
            super(view);
            msgNameView = view.findViewById(R.id.boater_msg_name);
            initialView = view.findViewById(R.id.initial_text_view);
            msgOuterBox = view.findViewById(R.id.marina_msg_box);
        }
    }

}
