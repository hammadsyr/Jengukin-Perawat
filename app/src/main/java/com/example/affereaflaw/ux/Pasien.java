package com.example.affereaflaw.ux;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class Pasien extends AppCompatActivity {

    private Button btnaddPasien;
    private EditText etNamaPasien, etTanggal, etKamar, etRs, etKode, etTelp;
    private CircleImageView imgProfilSelect;
    private ProgressDialog progress;
    private DatabaseReference databaseSO;
    private StorageReference storageReference;
    private Uri imageUri;
    private Calendar tanggalMasuk;
    private static final int GALLERY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasien);

        btnaddPasien = (Button) findViewById(R.id.btnAddPasien);
        etNamaPasien = (EditText) findViewById(R.id.etFirstName);
        etTanggal = (EditText) findViewById(R.id.etDate);
        etKamar = (EditText) findViewById(R.id.etKamar);
        etRs = (EditText) findViewById(R.id.etHospital);
        etKode = (EditText) findViewById(R.id.etKode);
        etTelp = (EditText) findViewById(R.id.etTelp);
        //imgSelect = (ImageButton) findViewById(R.id.imgSelect);
        imgProfilSelect = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.imgProfilSelect);

        progress = new ProgressDialog(this);
        databaseSO = FirebaseDatabase.getInstance().getReference().child("Data Pasien");

        etTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tanggalMasuk = Calendar.getInstance();
                int tahunMasuk = tanggalMasuk.get(Calendar.YEAR);
                int bulanMasuk = tanggalMasuk.get(Calendar.MONTH);
                int hariMasuk = tanggalMasuk.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Pasien.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        int bulan = i1+1;
                        etTanggal.setText(i2+"/"+bulan+"/"+i);
                    }
                }, tahunMasuk, bulanMasuk, hariMasuk);
                datePickerDialog.show();
            }
        });

        btnaddPasien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPasien();
            }
        });

        imgProfilSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galery = new Intent(Intent.ACTION_GET_CONTENT);
                galery.setType("image/*");
                startActivityForResult(galery, GALLERY_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            imageUri = data.getData();
            imgProfilSelect.setImageURI(imageUri);
        }
    }

    private void addPasien() {

        progress.setMessage("Menambahkan data pasien...");
        progress.show();
        final String nama = etNamaPasien.getText().toString();
        final String tanggal = etTanggal.getText().toString();
        final String kamar = etKamar.getText().toString();
        final String rs = etRs.getText().toString();
        final String kode = etKode.getText().toString();
        final String telp = etTelp.getText().toString();
        storageReference = FirebaseStorage.getInstance().getReference().child("Foto Pasien").child(kode);

        if (!TextUtils.isEmpty(nama)&&!TextUtils.isEmpty(tanggal)&&!TextUtils.isEmpty(kamar)&&!TextUtils.isEmpty(rs)&&!TextUtils.isEmpty(telp)&&imageUri!=null){

            storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloader = taskSnapshot.getDownloadUrl();
                    DatabaseReference newPostSO = databaseSO.push();
                    newPostSO.child("nama").setValue(nama);
                    newPostSO.child("tanggalMasuk").setValue(tanggal);
                    newPostSO.child("noKamar").setValue(kamar);
                    newPostSO.child("rs").setValue(rs);
                    newPostSO.child("kode").setValue(kode);
                    newPostSO.child("telp").setValue(telp);
                    newPostSO.child("image").setValue(downloader.toString());

                }
            });

            Intent i = new Intent(Pasien.this, MainMenu.class);
            startActivity(i);
            finish();
        }
    }
}
