package com.example.affereaflaw.ux;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class Timeline extends AppCompatActivity {

    private String kode = null, pilihan, image;
    private Spinner kategori;
    ArrayAdapter<CharSequence> adapter;
    private DatabaseReference dbTimeline;
    private EditText etTimeline;
    private Button btnPostTimeline;
    private RecyclerView rvTimeline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        kode = getIntent().getExtras().getString("Kode");
        etTimeline = (EditText) findViewById(R.id.etTimeline);
        btnPostTimeline = (Button) findViewById(R.id.btnPostTimeline);
        rvTimeline = (RecyclerView) findViewById(R.id.rvTimeline);
        rvTimeline.setLayoutManager(new LinearLayoutManager(this));
        kategori = (Spinner) findViewById(R.id.spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.pilih_kategori, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        kategori.setAdapter(adapter);
        kategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pilihan = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        /*CheckBox Obat = (CheckBox) findViewById(R.id.cbObat);
        boolean Obatchk = Obat.isChecked();
        CheckBox Tensi = (CheckBox) findViewById(R.id.cbTensi);
        boolean Tensichk = Tensi.isChecked();
        CheckBox Dokter = (CheckBox) findViewById(R.id.cbDokter);
        boolean Dokterchk = Dokter.isChecked();

        if (Obatchk) {
            kategori = "Obat";
        }else if (Tensichk) {
            kategori = "Tensi";
        }else if (Dokterchk) {
            kategori = "Pemeriksaan";
        }*/

        dbTimeline = FirebaseDatabase.getInstance().getReference().child("Timeline").child(kode);
        dbTimeline.keepSynced(true);

        btnPostTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postingTimeline();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<TimelineGetSet, timeline> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<TimelineGetSet, timeline>(
                TimelineGetSet.class,
                R.layout.timeline_row,
                timeline.class,
                dbTimeline
        ) {
            @Override
            protected void populateViewHolder(timeline viewHolder, TimelineGetSet model, int position) {
                viewHolder.setTimeline(model.getTimeline());
                viewHolder.setTime(model.getTime());
                viewHolder.setKategori(model.getKategori());
                viewHolder.setImage(model.getImage(), getBaseContext());
            }
        };
        rvTimeline.setAdapter(firebaseRecyclerAdapter);
    }

    private void postingTimeline() {
        final String timeline = etTimeline.getText().toString();
        final String kategoriPilihan = pilihan;
        if(pilihan.equals("Obat")){
            image = "https://firebasestorage.googleapis.com/v0/b/uxmanager-9be9a.appspot.com/o/d3.png?alt=media&token=dc70fa3b-2896-4e61-a3f1-fbb98b39201b";
        }
        if(pilihan.equals("Tensi")){
            image = "https://firebasestorage.googleapis.com/v0/b/uxmanager-9be9a.appspot.com/o/d1.png?alt=media&token=e01ffd39-4982-4400-bd06-99f66ace09fc";
        }
        if(pilihan.equals("Pemeriksaan")){
            image = "https://firebasestorage.googleapis.com/v0/b/uxmanager-9be9a.appspot.com/o/d2.png?alt=media&token=713e1426-54c3-44dc-8e10-d81861b46a57";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy hh.mm");
        final String time = simpleDateFormat.format(new Date());
        if (!TextUtils.isEmpty(timeline)){
            DatabaseReference newTimeline = dbTimeline.push();
            newTimeline.child("Timeline").setValue(timeline);
            newTimeline.child("Kategori").setValue(kategoriPilihan);
            newTimeline.child("Time").setValue(time);
            newTimeline.child("Image").setValue(image);
        }
    }


    public static class timeline extends RecyclerView.ViewHolder {
        View viewTimeline;

        public timeline (View itemView) {
            super(itemView);
            viewTimeline  = itemView;
        }

        public void setTimeline(String Timeline){
            final TextView txtTimeline = (TextView) viewTimeline.findViewById(R.id.timeline);
            txtTimeline.setText(Timeline);
        }

        public void setKategori(String Kategori){
            final TextView txtKategori = (TextView) viewTimeline.findViewById(R.id.kategori);
            txtKategori.setText(Kategori);
        }

        public void setTime(String Time){
            final TextView txtTime = (TextView) viewTimeline.findViewById(R.id.time);
            txtTime.setText(Time);
        }
        public void setImage(final String Image, final Context ctx) {
            final CircleImageView imgTimeline = (de.hdodenhof.circleimageview.CircleImageView) viewTimeline.findViewById(R.id.imgTimeline);

            Picasso.with(ctx).load(Image).networkPolicy(NetworkPolicy.OFFLINE).into(imgTimeline, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ctx).load(Image).into(imgTimeline);
                }
            });
        }
    }
}
