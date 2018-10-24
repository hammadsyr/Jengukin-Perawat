package com.example.affereaflaw.ux;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class Chat extends Fragment {

    public Chat() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public static class user extends RecyclerView.ViewHolder {
        static View viewUser;

        public user(View itemView) {
            super(itemView);
            viewUser  = itemView;
        }

        public void setNama(String nama){
            final TextView txtNama = (TextView) viewUser.findViewById(R.id.nama);
            txtNama.setText(nama);
        }

        public void setUsername(String username){
            final TextView txtUsername = (TextView) viewUser.findViewById(R.id.username);
            txtUsername.setText(username);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_chat, container, false);

        final RecyclerView rvUser = (RecyclerView) view.findViewById(R.id.rvUser);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.keepSynced(true);
        rvUser.setHasFixedSize(true);
        rvUser.setLayoutManager(new LinearLayoutManager(getActivity()));

        final FirebaseRecyclerAdapter<ChatGetSet, user> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ChatGetSet, user>(
                ChatGetSet.class,
                R.layout.chat_row,
                user.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(user viewHolder, ChatGetSet model, int position) {
                final String key = getRef(position).getKey();
                viewHolder.setNama(model.getNama());
                viewHolder.setUsername(model.getUsername());
                viewHolder.viewUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ChatView.class);
                        intent.putExtra("User", key);
                        startActivity(intent);
                    }
                });
            }
        };
        rvUser.setAdapter(firebaseRecyclerAdapter);
        return view;
    }
}