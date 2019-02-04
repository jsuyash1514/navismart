package com.navismart.navismart.view;


import android.app.Dialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.navismart.navismart.BuildConfig;
import com.navismart.navismart.R;
import com.navismart.navismart.adapters.ChatAdapter;
import com.navismart.navismart.model.ChatModel;
import com.navismart.navismart.utils.PreferencesHelper;
import com.navismart.navismart.viewmodelfactory.ChatViewModelFactory;
import com.navismart.navismart.viewmodels.ChatViewModel;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.navismart.navismart.MainActivity.USER_TYPE;
import static com.navismart.navismart.MainActivity.getCurrentStringDate;
import static com.navismart.navismart.MainActivity.getCurrentStringTime;
import static com.navismart.navismart.adapters.ChatAdapter.SENDER_BOATER;
import static com.navismart.navismart.adapters.ChatAdapter.SENDER_MARINA;

public class ChatFragment extends Fragment {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private RecyclerView chatRecyclerView;
    private FloatingActionButton sendButton;
    private EditText msgEditText;
    private TextView marinaChatName;
    private ImageView moreIcon;
    private Button deleteButton, cancelButton;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private String marinaID, boaterID, marinaName, boaterName;
    private String receiverToken, marinaToken, boaterToken;
    private PreferencesHelper preferencesHelper;

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

        preferencesHelper = new PreferencesHelper(getActivity());

        chatRecyclerView = view.findViewById(R.id.chat_recycler_view);
        sendButton = view.findViewById(R.id.send_button);
        msgEditText = view.findViewById(R.id.msg_edit_text);
        marinaChatName = view.findViewById(R.id.marinaChatName);
        moreIcon = view.findViewById(R.id.more_icon);

        msgEditText.setEnabled(false);
        sendButton.setEnabled(false);

        marinaName = getArguments().getString("marinaName");
        boaterName = getArguments().getString("boaterName");
        marinaID = getArguments().getString("marinaID");
        boaterID = getArguments().getString("boaterID");

        if (USER_TYPE == SENDER_BOATER) {
            marinaChatName.setText(marinaName);
        } else if (USER_TYPE == SENDER_MARINA) {
            marinaChatName.setText(boaterName);
        }

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("users").child(marinaID).child("profile").child("fcm_token").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                marinaToken = dataSnapshot.getValue().toString();
                databaseReference.child("users").child(boaterID).child("profile").child("fcm_token").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boaterToken = dataSnapshot.getValue().toString();
                        if (USER_TYPE == SENDER_BOATER)
                            receiverToken = marinaToken;
                        else
                            receiverToken = boaterToken;
                        msgEditText.setEnabled(true);
                        sendButton.setEnabled(true);
                        Log.d("MARINA_TOKEN", marinaToken);
                        Log.d("BOATER_TOKEN", boaterToken);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Dialog deleteDialog = new Dialog(getContext());
        deleteDialog.setContentView(R.layout.delete_chat_dialog);
        deleteButton = deleteDialog.findViewById(R.id.delete_button);
        cancelButton = deleteDialog.findViewById(R.id.cancel_delete);

        cancelButton.setOnClickListener((View v) -> deleteDialog.dismiss());

        deleteButton.setOnClickListener((View v) -> {
            deleteAllChats();
            deleteDialog.dismiss();
        });

        Log.d("marinaID", "" + marinaID);
        Log.d("boaterID", "" + boaterID);
        Log.d("USER_TYPE", USER_TYPE + "");

        moreIcon.setOnClickListener((View v) -> {

            PopupMenu popupMenu = new PopupMenu(getContext(), moreIcon);
            popupMenu.getMenuInflater().inflate(R.menu.chat_options, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {

                if (item.getItemId() == R.id.delete_chats) {
                    deleteDialog.show();
                }

                return true;
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
            liveData.observe(this, dataSnapshot -> {

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

            });
        } else {
            ChatViewModel chatViewModel = ViewModelProviders.of(this, new ChatViewModelFactory(boaterID, marinaID)).get(ChatViewModel.class);
            LiveData<DataSnapshot> liveData = chatViewModel.getDataSnapshotLiveData();
            liveData.observe(this, dataSnapshot -> {

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

            });
        }


        return view;
    }

    private void deleteAllChats() {

        DatabaseReference chatReference;
        String id = "";
        if (USER_TYPE == SENDER_BOATER)
            id = marinaID;
        else
            id = boaterID;
        chatReference = databaseReference.child("users").child(auth.getCurrentUser().getUid()).child("chats").child(id).child("messages");
        chatReference.setValue(null)
                .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Chats deleted successfully", Toast.LENGTH_SHORT).show()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Unable to delete chats", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void uploadMessage(ChatModel chatModel) {

        DatabaseReference chatReference;

        chatReference = databaseReference.child("users").child(marinaID).child("chats").child(boaterID).child("messages");
        chatReference.push().setValue(chatModel).addOnSuccessListener(aVoid -> msgEditText.setText(null));

        chatReference = databaseReference.child("users").child(marinaID).child("chats").child(boaterID).child("marinaName");
        chatReference.setValue(marinaName);
        chatReference = databaseReference.child("users").child(marinaID).child("chats").child(boaterID).child("boaterName");
        chatReference.setValue(boaterName);

        chatReference = databaseReference.child("users").child(boaterID).child("chats").child(marinaID).child("messages");
        chatReference.push().setValue(chatModel).addOnSuccessListener(aVoid -> msgEditText.setText(null));

        chatReference = databaseReference.child("users").child(boaterID).child("chats").child(marinaID).child("marinaName");
        chatReference.setValue(marinaName);
        chatReference = databaseReference.child("users").child(boaterID).child("chats").child(marinaID).child("boaterName");
        chatReference.setValue(boaterName);
        String marinaToken = "", boaterToken = "";
        if (USER_TYPE == SENDER_BOATER) {
            marinaToken = receiverToken;
            boaterToken = preferencesHelper.getToken();
        } else {
            marinaToken = preferencesHelper.getToken();
            boaterToken = receiverToken;
        }
        chatReference = databaseReference.child("users").child(boaterID).child("chats").child(marinaID).child("marinaToken");
        chatReference.setValue(marinaToken);
        chatReference = databaseReference.child("users").child(boaterID).child("chats").child(marinaID).child("boaterToken");
        chatReference.setValue(boaterToken);
        chatReference = databaseReference.child("users").child(marinaID).child("chats").child(boaterID).child("marinaToken");
        chatReference.setValue(marinaToken);
        chatReference = databaseReference.child("users").child(marinaID).child("chats").child(boaterID).child("boaterToken");
        chatReference.setValue(boaterToken);

        sendNotification(receiverToken, chatModel.getMsg());
    }

    private void sendNotification(String regToken, String msg) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json = new JSONObject();
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("body", msg);
                    dataJson.put("title", auth.getCurrentUser().getDisplayName());
                    json.put("notification", dataJson);
                    json.put("to", regToken);
                    RequestBody body = RequestBody.create(JSON, json.toString());
                    Request request = new Request.Builder()
                            .header("Authorization", "key=" + BuildConfig.LEGACY_SERVER_KEY)
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String finalResponse = response.body().string();
                } catch (Exception e) {
                    Log.d("Notification Error: ", e.toString());
                }
                return null;
            }
        }.execute();
    }

}
