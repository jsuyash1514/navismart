package com.navismart.navismart.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
        holder.msgDateTimeView.setText(chatModel.getMsgDate() + "   " + chatModel.getMsgTime());

        if (chatModel.getSENDER_TYPE() == USER_TYPE) {
            holder.chatBrickHolder.setGravity(Gravity.END);
            holder.chatBrickView.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_green));
        } else {
            holder.chatBrickHolder.setGravity(Gravity.START);
            holder.chatBrickView.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle_white));
        }

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameView, messageView, msgDateTimeView;
        public LinearLayout chatBrickHolder, chatBrickView;

        public MyViewHolder(View view) {
            super(view);
            nameView = view.findViewById(R.id.msg_name);
            messageView = view.findViewById(R.id.msg_text);
            msgDateTimeView = view.findViewById(R.id.msg_date_time);
            chatBrickHolder = view.findViewById(R.id.chat_brick_holder);
            chatBrickView = view.findViewById(R.id.chat_brick_card);
        }
    }

}
