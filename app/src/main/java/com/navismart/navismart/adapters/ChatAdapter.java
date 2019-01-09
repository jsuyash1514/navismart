package com.navismart.navismart.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.navismart.navismart.R;
import com.navismart.navismart.model.ChatModel;

import java.util.List;

import static com.navismart.navismart.MainActivity.USER_TYPE;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    public static int SENDER_BOATER = 1;
    public static int SENDER_MARINA = 2;

    private List<ChatModel> chatList;

    private Context context;

    public ChatAdapter(List<ChatModel> chatList, Context context) {
        this.chatList = chatList;
        this.context = context;
    }

    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_brick, parent, false);

        return new ChatAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChatAdapter.MyViewHolder holder, int position) {

        ChatModel chatModel = chatList.get(position);

        holder.nameView.setText(chatModel.getMsgName());
        holder.messageView.setText(chatModel.getMsg());
        holder.msgDateTimeView.setText(chatModel.getMsgDate() + " " + chatModel.getMsgTime());

        if (chatModel.getSENDER_TYPE() == USER_TYPE) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_END);
            holder.chatBrickCard.setLayoutParams(params);
            holder.chatBrickCard.setBackgroundColor(context.getResources().getColor(R.color.green));
        } else {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_START);
            holder.chatBrickCard.setLayoutParams(params);
            holder.chatBrickCard.setBackgroundColor(context.getResources().getColor(R.color.white));
        }

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameView, messageView, msgDateTimeView;
        public CardView chatBrickCard;

        public MyViewHolder(View view) {
            super(view);
            nameView = view.findViewById(R.id.msg_name);
            messageView = view.findViewById(R.id.msg_text);
            msgDateTimeView = view.findViewById(R.id.msg_date_time);
            chatBrickCard = view.findViewById(R.id.chat_brick_card);
        }
    }

}
