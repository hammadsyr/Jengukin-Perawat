package com.example.affereaflaw.ux;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class Register extends AppCompatActivity{

    private String userKey, gender;
    private Button btnRegister;
    private EditText etName, etDate, etAddress, etTelp, etEmail, etUsername, etPassReg, etPassConfirm;
    private FirebaseAuth auth;
    private DatabaseReference dbPerawat;
    private ProgressDialog progress;
    private Spinner spnGender;
    private Calendar tanggal;
    ArrayAdapter<CharSequence> aptGender;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        btnRegister = (Button) findViewById(R.id.btnRegister);
        etName = (EditText) findViewById(R.id.etFirstName);
        etDate = (EditText) findViewById(R.id.etDate);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etTelp = (EditText) findViewById(R.id.etTelp);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etUsername = (EditText) findViewById(R.id.etUserReg);
        etPassReg = (EditText) findViewById(R.id.etPassRegister);
        etPassConfirm = (EditText) findViewById(R.id.etPassConfirm);
        progress = new ProgressDialog(this);
        dbPerawat = FirebaseDatabase.getInstance().getReference().child("Perawat");

        auth = FirebaseAuth.getInstance();

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tanggal = Calendar.getInstance();
                int tahun = tanggal.get(Calendar.YEAR);
                int bulan = tanggal.get(Calendar.MONTH);
                int hari = tanggal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Register.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        int bulan = i1+1;
                        etDate.setText(i2+"/"+bulan+"/"+i);
                    }
                }, tahun, bulan, hari);
                datePickerDialog.show();
            }
        });

        spnGender = (Spinner) findViewById(R.id.spinnerJenisKelamin);
        aptGender = ArrayAdapter.createFromResource(this, R.array.jenis_kelamin, android.R.layout.simple_spinner_item);
        aptGender.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spnGender.setAdapter(aptGender);
        spnGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = parent.getItemAtPosition(position).toString();
                ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#F50057"));
                ((TextView) parent.getChildAt(0)).setTextSize(12);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = etEmail.getText().toString().trim();
                final String password = etPassReg.getText().toString().trim();
                final String nama = etName.getText().toString().trim();
                final String username = etUsername.getText().toString().trim();
                final String jeniskelamin = gender;

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Masukkan email Anda", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Masukkan password Anda", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password terlalu pendek, minimal 6 karakter", Toast.LENGTH_SHORT).show();
                    return;
                }

                //progressBar.setVisibility(View.VISIBLE);
                progress.setMessage("Register...");
                progress.show();
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(Register.this, "Pembuatan akun berhasil", Toast.LENGTH_SHORT).show();
                                userKey = auth.getCurrentUser().getUid();
                                dbPerawat = FirebaseDatabase.getInstance().getReference().child("Perawat").child(userKey);
                                HashMap<String, String> userMap = new HashMap<>();
                                userMap.put("nama", nama);
                                userMap.put("username", username);
                                dbPerawat.setValue(userMap);
                                //progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(Register.this, "Pembuatan akun gagal" + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                    progress.dismiss();
                                } else {
                                    Intent i = new Intent(Register.this, Login.class);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        });
            }
        });
    }
}