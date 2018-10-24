package com.example.affereaflaw.ux;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PasienView extends AppCompatActivity {

    private String key = null;
    private DatabaseReference databasePasienView, databaseSO;
    private StorageReference storageReference;
    private Query query;
    private EditText etNamaPasien, etTanggal, etKamar, etRs, etKode, etTelp;
    private Button btnRemove, btnUpdate;
    private ImageButton panic, suggestion, timeline;
    private CircleImageView imgProfilView;
    private Uri imageUri;
    private static final int GALLERY_REQUEST = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            imageUri = data.getData();
            imgProfilView.setImageURI(imageUri);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasien_view);

        key = getIntent().getExtras().getString("Data Pasien");
        etNamaPasien = (EditText) findViewById(R.id.etvFirstName);
        etTanggal = (EditText) findViewById(R.id.etvDate);
        etKamar = (EditText) findViewById(R.id.etvKamar);
        etRs = (EditText) findViewById(R.id.etvHospital);
        etKode = (EditText) findViewById(R.id.etvKode);
        etTelp = (EditText) findViewById(R.id.etvTelp);
        btnRemove = (Button) findViewById(R.id.btnRemove);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        panic  = (ImageButton) findViewById(R.id.imageButton5);
        suggestion = (ImageButton) findViewById(R.id.imageButton2);
        timeline = (ImageButton) findViewById(R.id.imageButton4);
        imgProfilView = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.imgProfilView);

        databasePasienView = FirebaseDatabase.getInstance().getReference().child("Data Pasien");

        imgProfilView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galery = new Intent(Intent.ACTION_GET_CONTENT);
                galery.setType("image/*");
                startActivityForResult(galery, GALLERY_REQUEST);
            }
        });



        databasePasienView.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String nameView = (String) dataSnapshot.child("nama").getValue();
                String tanggalView = (String) dataSnapshot.child("tanggalMasuk").getValue();
                String noKamarView = (String) dataSnapshot.child("noKamar").getValue();
                String rsView = (String) dataSnapshot.child("rs").getValue();
                String kodeView = (String) dataSnapshot.child("kode").getValue();
                String telpView = (String) dataSnapshot.child("telp").getValue();
                final String imageView = (String) dataSnapshot.child("image").getValue();

                etNamaPasien.setText(nameView);
                etTanggal.setText(tanggalView);
                etKamar.setText(noKamarView);
                etRs.setText(rsView);
                etKode.setText(kodeView);
                etTelp.setText(telpView);
                Picasso.with(PasienView.this).load(imageView).networkPolicy(NetworkPolicy.OFFLINE).into(imgProfilView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(PasienView.this).load(imageView).into(imgProfilView);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String kode = etKode.getText().toString();
                databaseSO = FirebaseDatabase.getInstance().getReference().child("SO").child(kode);
                //databaseSO = FirebaseDatabase.getInstance().getReference().child("Data Pasien").child(kode).child("SO");
                databaseSO.removeValue();
                databasePasienView.child(key).removeValue();
                //FirebaseStorage fbS = FirebaseStorage.getInstance();
                //StorageReference imgDelete = fbS.getReferenceFromUrl(imageUri.toString());
                startActivity(new Intent(PasienView.this, MainMenu.class));
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference databaseReferenceUpdate = databasePasienView.child(key);
                final String nama = etNamaPasien.getText().toString();
                final String tanggal = etTanggal.getText().toString();
                final String kamar = etKamar.getText().toString();
                final String rs = etRs.getText().toString();
                final String kode = etKode.getText().toString();
                final String telp = etTelp.getText().toString();
                storageReference = FirebaseStorage.getInstance().getReference().child("Foto Pasien").child(kode);

                if (!TextUtils.isEmpty(nama)&&!TextUtils.isEmpty(tanggal)&&!TextUtils.isEmpty(kamar)&&!TextUtils.isEmpty(rs)&&!TextUtils.isEmpty(telp)){
                    if (imageUri!=null) {
                        storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Uri downloader = taskSnapshot.getDownloadUrl();
                                databaseReferenceUpdate.child("image").setValue(downloader.toString());
                            }
                        });
                    }
                    databaseReferenceUpdate.child("nama").setValue(nama);
                    databaseReferenceUpdate.child("tanggalMasuk").setValue(tanggal);
                    databaseReferenceUpdate.child("noKamar").setValue(kamar);
                    databaseReferenceUpdate.child("rs").setValue(rs);
                    databaseReferenceUpdate.child("kode").setValue(kode);
                    databaseReferenceUpdate.child("telp").setValue(telp);
                }
                Toast.makeText(getApplicationContext(), "Update berhasil", Toast.LENGTH_SHORT).show();
            }
        });

        panic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String telp = etTelp.getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telp));
                startActivity(intent);
            }
        });

        suggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String kode = etKode.getText().toString();
                Intent intent = new Intent(PasienView.this, Suggestion.class);
                intent.putExtra("Kode",kode);
                startActivity(intent);
            }
        });

        timeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String kode = etKode.getText().toString();
                Intent intent = new Intent(PasienView.this, Timeline.class);
                intent.putExtra("Kode",kode);
                startActivity(intent);
            }
        });
    }
}
