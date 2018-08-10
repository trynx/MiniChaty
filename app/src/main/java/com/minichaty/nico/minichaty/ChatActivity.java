package com.minichaty.nico.minichaty;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.minichaty.nico.minichaty.data.MessagePost;
import com.minichaty.nico.minichaty.data.remote.APIService;
import com.minichaty.nico.minichaty.data.remote.APIUtils;


import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "MiniChat";

    // Layout references
    private EditText etMessage;
    private TextView tvChat;
    private ScrollView svChatScroll;

    // Firebase references
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private ValueEventListener chatListener;

    // HTTP Service
    private APIService apiService;

    // Only after getting the first batch of message, from then on only the last
    // update from the DB is needed to update the user
    private boolean isNewMsg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        etMessage = (EditText) findViewById(R.id.et_message);
        tvChat = (TextView) findViewById(R.id.tv_chat);
        svChatScroll = (ScrollView) findViewById(R.id.sv_chat_scroll);

        apiService = APIUtils.getAPIService(APIUtils.HTTPUrls.CHAT);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("chat"); // Reference where the chat is written

        prepareFirebaseListeners();
        myRef.addValueEventListener(chatListener);

    }

    /**
     * Attach the needed listeners to firebase database, for fetching current data and upload
     * the user chat, then to update every new message
     */
    private void prepareFirebaseListeners(){
        // Save the instance so it could be remove at the end of the app life cycle
        // Read every new message that is written to the database
        chatListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> chatList = new ArrayList<>();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.getValue() == null) {
                        continue;
                    }

                    for (DataSnapshot msgData: data.getChildren()) {
                        if (msgData.getValue() == null) {
                            continue;
                        }
                        String message = msgData.getValue().toString();

                        if (isNewMsg){
                            chatList.add(message);
                        } else {
                            tvChat.append(message + "\n");
                        }
                    }
                }
                // After adding all the values, scroll down
                svChatScroll.fullScroll(View.FOCUS_DOWN);

                isNewMsg = true; // Done fetching all the data, now only the last update is needed

                if(chatList.size() == 0){
                    return; // Nothing to update
                }
                String lastMsg = chatList.get(chatList.size() - 1); // Last new value
                tvChat.append(lastMsg + "\n");

                // After adding all the values, scroll down
                svChatScroll.fullScroll(View.FOCUS_DOWN);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        };

    }

    public void sendMsg(View view) {
        String message = etMessage.getText().toString();

        if (!TextUtils.isEmpty(message)) sendPost(message);
    }

    /**
     * Send post of the message through APIService and handles the request from the API
     */
    private void sendPost(String message) {

        MessagePost msg = new MessagePost(message);

        apiService.sendMsg(msg).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e(TAG, "Successfully sent message!");
                etMessage.setText(""); // Clean edit text message after it's done
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Unable to submit message post");
                Toast.makeText(getApplicationContext(), "Error sending message", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        myRef.removeEventListener(chatListener);
    }
}
