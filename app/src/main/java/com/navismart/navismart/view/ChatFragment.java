package com.navismart.navismart.view;


import android.app.Dialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
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
import static com.navismart.navismart.adapters.ChatAdapter.SENDER_BOATER;

public class ChatFragment extends Fragment {

    private RecyclerView chatRecyclerView;
    private FloatingActionButton sendButton;
    private EditText msgEditText;
    private TextView marinaChatName;
    private ImageView moreIcon;
    private Button deleteButton, cancelButton;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private String marinaID, boaterID, marinaName, boaterName;

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
        moreIcon = view.findViewById(R.id.more_icon);

        marinaName = getArguments().getString("marinaName");
        boaterName = getArguments().getString("boaterName");

        if (USER_TYPE == SENDER_BOATER) {
            marinaChatName.setText(marinaName);
        } else {
            marinaChatName.setText(boaterName);
        }

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        marinaID = getArguments().getString("marinaID");
        boaterID = getArguments().getString("boaterID");

        Dialog deleteDialog = new Dialog(getContext());
        deleteDialog.setContentView(R.layout.delete_dialog);
        deleteButton = deleteDialog.findViewById(R.id.delete_button);
        cancelButton = deleteDialog.findViewById(R.id.cancel_delete);

        cancelButton.setOnClickListener((View v) -> deleteDialog.dismiss());

        deleteButton.setOnClickListener((View v) -> {
            deleteAllChats();
            deleteDialog.dismiss();
        });

        Log.d("marinaID", marinaID);
        Log.d("boaterID", boaterID);
        Log.d("USER_TYPE", USER_TYPE + "");

        moreIcon.setOnClickListener((View v) -> {

            PopupMenu popupMenu = new PopupMenu(getContext(), moreIcon);
            popupMenu.getMenuInflater().inflate(R.menu.chat_options, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Log.d("TAGTAGTAG", "Here");
                    if (item.getItemId() == R.id.delete_chats) {
                        deleteDialog.show();
                    }

                    return true;
                }
            });
            popupMenu.show();


        });

        sendButton.setOnClickListener((View v) -> {

            if (!msgEditText.getText().toString().trim().isEmpty()) {
                ChatModel chatModel = new ChatModel();
                chatModel.setSENDER_TYPE(USER_TYPE);
                chatModel.setMsg(msgEditText.getText().toString());
                chatModel.setMsgTime(getCurrentStringTime());
                chatModel.setMsgDate(getCurrentStringDate());
                uploadMessage(chatModel);
            }

        });

        if (USER_TYPE == SENDER_BOATER) {
            ChatViewModel chatViewModel = ViewModelProviders.of(this, new ChatViewModelFactory(marinaID, boaterID)).get(ChatViewModel.class);
            LiveData<DataSnapshot> liveData = chatViewModel.getDataSnapshotLiveData();
            liveData.observe(this, new Observer<DataSnapshot>() {
                @Override
                public void onChanged(@Nullable DataSnapshot dataSnapshot) {

                    ArrayList<ChatModel> chatModelArrayList = new ArrayList<>();

                    for (DataSnapshot snapshot : dataSnapshot.child("messages").getChildren()) {

                        ChatModel chatModel = snapshot.getValue(ChatModel.class);
                        if (chatModel.getSENDER_TYPE() == SENDER_BOATER) {
                            chatModel.setMsgName(boaterName);
                        } else {
                            chatModel.setMsgName(marinaName);
                        }
                        chatModelArrayList.add(chatModel);

                    }

                    ChatAdapter reviewListAdapter = new ChatAdapter(chatModelArrayList, getContext());
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                    ((LinearLayoutManager) mLayoutManager).setStackFromEnd(true);
                    chatRecyclerView.setLayoutManager(mLayoutManager);
                    chatRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    chatRecyclerView.setAdapter(reviewListAdapter);

                }
            });
        } else {
            ChatViewModel chatViewModel = ViewModelProviders.of(this, new ChatViewModelFactory(boaterID, marinaID)).get(ChatViewModel.class);
            LiveData<DataSnapshot> liveData = chatViewModel.getDataSnapshotLiveData();
            liveData.observe(this, new Observer<DataSnapshot>() {
                @Override
                public void onChanged(@Nullable DataSnapshot dataSnapshot) {

                    ArrayList<ChatModel> chatModelArrayList = new ArrayList<>();

                    for (DataSnapshot snapshot : dataSnapshot.child("messages").getChildren()) {

                        ChatModel chatModel = snapshot.getValue(ChatModel.class);
                        if (chatModel.getSENDER_TYPE() == SENDER_BOATER) {
                            chatModel.setMsgName(boaterName);
                        } else {
                            chatModel.setMsgName(marinaName);
                        }
                        chatModelArrayList.add(chatModel);

                    }

                    ChatAdapter reviewListAdapter = new ChatAdapter(chatModelArrayList, getContext());
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                    ((LinearLayoutManager) mLayoutManager).setStackFromEnd(true);
                    chatRecyclerView.setLayoutManager(mLayoutManager);
                    chatRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    chatRecyclerView.setAdapter(reviewListAdapter);

                }
            });
        }


        return view;
    }

    private void deleteAllChats() {

        DatabaseReference chatReference;
        chatReference = databaseReference.child("users").child(auth.getCurrentUser().getUid()).child("chats");
        chatReference.setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Chats deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Unable to delete chats", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void uploadMessage(ChatModel chatModel) {

        DatabaseReference chatReference;

        chatReference = databaseReference.child("users").child(marinaID).child("chats").child(boaterID).child("messages");
        chatReference.push().setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                msgEditText.setText(null);
            }
        });

        chatReference = databaseReference.child("users").child(marinaID).child("chats").child(boaterID).child("marinaName");
        chatReference.setValue(marinaName);
        chatReference = databaseReference.child("users").child(marinaID).child("chats").child(boaterID).child("boaterName");
        chatReference.setValue(boaterName);

        chatReference = databaseReference.child("users").child(boaterID).child("chats").child(marinaID).child("messages");
        chatReference.push().setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                msgEditText.setText(null);
            }
        });

        chatReference = databaseReference.child("users").child(boaterID).child("chats").child(marinaID).child("marinaName");
        chatReference.setValue(marinaName);
        chatReference = databaseReference.child("users").child(boaterID).child("chats").child(marinaID).child("boaterName");
        chatReference.setValue(boaterName);


    }

}
