package com.example.affereaflaw.ux;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatView extends AppCompatActivity {

    private String key = null, userKey;
    private DatabaseReference dbMessage;
    private ImageButton btnSend;
    private RecyclerView rvMessage;
    private EditText txtMessage;
    private FirebaseAuth auth;

    private MessageAdapter adapter;
    private List<MessagesGetSet> messagesGetSetList = new ArrayList<>();
    private LinearLayoutManager LinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_view);

        key = getIntent().getExtras().getString("User");

        btnSend = (ImageButton) findViewById(R.id.btnSend);
        txtMessage = (EditText) findViewById(R.id.txtMesssage);
        rvMessage = (RecyclerView) findViewById(R.id.rvMessage);

        auth = FirebaseAuth.getInstance();
        userKey = auth.getCurrentUser().getUid();
        dbMessage = FirebaseDatabase.getInstance().getReference();
        dbMessage.keepSynced(true);

        adapter = new MessageAdapter(messagesGetSetList);
        rvMessage.setHasFixedSize(true);
        LinearLayout = new LinearLayoutManager(this);
        rvMessage.setLayoutManager(LinearLayout);
        rvMessage.setAdapter(adapter);

        
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        loadMessage();
    }

    private void sendMessage(){
        String message = txtMessage.getText().toString();
        //push()
        if(!TextUtils.isEmpty(message)){

            String current_user_ref = "Chat/" + userKey + "/" + key;
            String chat_user_ref = "Chat/" + key + "/" + userKey;

            DatabaseReference user_message_push = dbMessage.child("Chat").child(userKey).child(key).push();

            String push_id = user_message_push.getKey();

            Map messageMap = new HashMap();
            messageMap.put("pesan", message);
            //messageMap.put("time", ServerValue.TIMESTAMP);
            messageMap.put("from", userKey);

            Map messageUserMap = new HashMap();
            messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
            messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);

            txtMessage.setText("");


            //dbMessage.child("Messages").child(userKey).child(key).child("timestamp").setValue(ServerValue.TIMESTAMP);
            //dbMessage.child("Me").child(key).child(userKey).child("timestamp").setValue(ServerValue.TIMESTAMP);

            dbMessage.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                    if(databaseError != null){

                        Log.d("CHAT_LOG", databaseError.getMessage().toString());

                    }

                }
            });

        }
        /*DatabaseReference newMessage = dbMessage.child(userKey).child(key).push();
        DatabaseReference newMessageR = dbMessage.child(key).child(userKey).push();
        newMessage.child("pesan").setValue(message);
        newMessage.child("from").setValue(userKey);
        newMessageR.child("pesan").setValue(message);
        newMessageR.child("from").setValue(userKey);
        txtMessage.setText("");*/
    }


    private void loadMessage() {
        dbMessage.child("Chat").child(userKey).child(key).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                MessagesGetSet message2 = dataSnapshot.getValue(MessagesGetSet.class);

                messagesGetSetList.add(message2);
                adapter.notifyDataSetChanged();
                rvMessage.scrollToPosition(messagesGetSetList.size()-1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
