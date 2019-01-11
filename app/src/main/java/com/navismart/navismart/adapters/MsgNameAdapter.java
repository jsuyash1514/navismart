package com.navismart.navismart.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.navismart.navismart.R;
import com.navismart.navismart.model.MsgNameModel;

import java.util.List;

import androidx.navigation.Navigation;

import static com.navismart.navismart.MainActivity.USER_TYPE;
import static com.navismart.navismart.adapters.ChatAdapter.SENDER_BOATER;
import static com.navismart.navismart.adapters.ChatAdapter.SENDER_MARINA;

public class MsgNameAdapter extends RecyclerView.Adapter<MsgNameAdapter.MyViewHolder> {

    private List<MsgNameModel> msgNameList;
    private Activity activity;

    public MsgNameAdapter(Activity activity, List<MsgNameModel> msgNameList) {
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
                if (USER_TYPE == SENDER_MARINA) {
                    bundle.putString("boaterName", msgName.getMsgName());
                    bundle.putString("marinaID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    bundle.putString("boaterID", msgName.getID());
                    bundle.putString("marinaName", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                    Navigation.findNavController(activity, R.id.my_nav_host_fragment).navigate(R.id.action_landingFragment_to_chatFragment, bundle);
                } else if (USER_TYPE == SENDER_BOATER) {
                    bundle.putString("boaterName", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                    bundle.putString("marinaID", msgName.getID());
                    bundle.putString("boaterID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    Log.d("adapter marinaID", msgName.getID());
                    Log.d("adapter boaterID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    bundle.putString("marinaName", msgName.getMsgName());
                    Navigation.findNavController(activity, R.id.my_nav_host_fragment).navigate(R.id.action_boaterLandingFragment_to_chatFragment, bundle);
                }

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
