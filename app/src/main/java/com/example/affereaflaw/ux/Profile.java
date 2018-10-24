package com.example.affereaflaw.ux;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class Profile extends Fragment {


    public Profile() {
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

    public static class profilPasien extends RecyclerView.ViewHolder {
        static View viewProfileP;

        public profilPasien(View itemView) {
            super(itemView);
            viewProfileP  = itemView;
        }

        public void setNama(String nama){
            final TextView txtNama = (TextView) viewProfileP.findViewById(R.id.nama);
            txtNama.setText(nama);
        }

        public void setKamar(String noKamar){
            final TextView txtKamar = (TextView) viewProfileP.findViewById(R.id.kamar);
            txtKamar.setText(noKamar);
        }

        public void setKode(String kode){
            final TextView txtKode = (TextView) viewProfileP.findViewById(R.id.kode);
            txtKode.setText(kode);
        }

        public void setImage(final String image, final Context ctx) {
            final CircleImageView imgProfil = (de.hdodenhof.circleimageview.CircleImageView) viewProfileP.findViewById(R.id.imgProfil);

            Picasso.with(ctx).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(imgProfil, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ctx).load(image).into(imgProfil);
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);

        final RecyclerView rvPasien = (RecyclerView) view.findViewById(R.id.rvProfil);
        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Data Pasien");
        Query query = databaseReference.orderByChild("kode").equalTo("abc");
        databaseReference.keepSynced(true);
        //rvPasien.setHasFixedSize(true);
        rvPasien.setLayoutManager(new LinearLayoutManager(getActivity()));

        FirebaseRecyclerAdapter<profilGetSet, profilPasien> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<profilGetSet, profilPasien>(
                profilGetSet.class,
                R.layout.profile_row,
                profilPasien.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(profilPasien viewHolder, profilGetSet model, int position) {
                final String key = getRef(position).getKey();
                viewHolder.setNama(model.getNama());
                viewHolder.setKamar(model.getNoKamar());
                viewHolder.setKode(model.getKode());
                viewHolder.setImage(model.getImage(),getContext());
                viewHolder.viewProfileP.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(),PasienView.class);
                        i.putExtra("Data Pasien",key);
                        startActivity(i);
                    }
                });
            }
        };
        rvPasien.setAdapter(firebaseRecyclerAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Pasien.class);
                startActivity(intent);
            }
        });
        return view;
    }
}