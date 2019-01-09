package com.navismart.navismart.view;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.navismart.navismart.R;
import com.navismart.navismart.adapters.ChatAdapter;
import com.navismart.navismart.model.ChatModel;
import com.navismart.navismart.viewmodelfactory.ChatViewModelFactory;
import com.navismart.navismart.viewmodels.ChatViewModel;

import java.util.ArrayList;

import static com.navismart.navismart.MainActivity.USER_TYPE;
import static com.navismart.navismart.MainActivity.getCurrentStringDate;
import static com.navismart.navismart.MainActivity.getCurrentStringTime;

public class ChatFragment extends Fragment {

    private RecyclerView chatRecyclerView;
    private FloatingActionButton sendButton;
    private EditText msgEditText;
    private TextView marinaChatName;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private String marinaID, boaterID, userName;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        chatRecyclerView = view.findViewById(R.id.chat_recycler_view);
        sendButton = view.findViewById(R.id.send_button);
        msgEditText = view.findViewById(R.id.msg_edit_text);
        marinaChatName = view.findViewById(R.id.marinaChatName);
        marinaChatName.setText(getArguments().getString("marinaName"));

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        marinaID = getArguments().getString("marinaID");
        boaterID = getArguments().getString("boaterID");

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!msgEditText.getText().toString().trim().isEmpty()) {
                    ChatModel chatModel = new ChatModel();
                    chatModel.setBoaterID(boaterID);
                    chatModel.setMarinaID(marinaID);
                    chatModel.setSENDER_TYPE(USER_TYPE);
                    chatModel.setMsg(msgEditText.getText().toString());
                    chatModel.setMsgTime(getCurrentStringTime());
                    chatModel.setMsgDate(getCurrentStringDate());
                    chatModel.setMsgName(auth.getCurrentUser().getDisplayName());
                    uploadMessage(chatModel);
                }
            }
        });

        ChatViewModel chatViewModel = ViewModelProviders.of(this, new ChatViewModelFactory(marinaID, boaterID)).get(ChatViewModel.class);
        LiveData<DataSnapshot> liveData = chatViewModel.getDataSnapshotLiveData();
        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {

                ArrayList<ChatModel> chatModelArrayList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    chatModelArrayList.add(snapshot.getValue(ChatModel.class));
                }

                ChatAdapter reviewListAdapter = new ChatAdapter(chatModelArrayList, getContext());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                ((LinearLayoutManager) mLayoutManager).setStackFromEnd(true);
                chatRecyclerView.setLayoutManager(mLayoutManager);
                chatRecyclerView.setItemAnimator(new DefaultItemAnimator());
                chatRecyclerView.setAdapter(reviewListAdapter);

            }
        });

        return view;
    }

    private void uploadMessage(ChatModel chatModel) {

        DatabaseReference chatReference = databaseReference.child("chats").child(chatModel.getMarinaID()).child(chatModel.getBoaterID());
        chatReference.push().setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                msgEditText.setText(null);
            }
        });

    }

}
