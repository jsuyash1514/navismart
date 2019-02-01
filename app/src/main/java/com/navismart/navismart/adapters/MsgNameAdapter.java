package com.navismart.navismart.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.navismart.navismart.R;
import com.navismart.navismart.model.MsgNameModel;

import java.util.List;

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
