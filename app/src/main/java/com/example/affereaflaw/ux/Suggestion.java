package com.example.affereaflaw.ux;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;

public class Suggestion extends AppCompatActivity {

    private Button btnSimpan;
    private String kode = null;
    private EditText etSuggest, etObat;
    private ProgressDialog progress;
    private DatabaseReference databaseSO;
    private Query query;
    private RecyclerView rvSO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);

        kode = getIntent().getExtras().getString("Kode");
        btnSimpan = (Button) findViewById(R.id.btnAdd);
        etSuggest = (EditText) findViewById(R.id.etSuggestion);
        etObat = (EditText) findViewById(R.id.etObat);
        progress = new ProgressDialog(this);
        rvSO = (RecyclerView) findViewById(R.id.rvSuggestionObat);
        rvSO.setLayoutManager(new LinearLayoutManager(this));

        databaseSO = FirebaseDatabase.getInstance().getReference().child("SO").child(kode);
        //databaseSO = FirebaseDatabase.getInstance().getReference().child("Data Pasien").child(kode).child("SO");
        query = databaseSO.orderByChild("kode").equalTo(kode);

        databaseSO.keepSynced(true);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_postingSugesstion();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<SuggestionObatGetSet, suggestObat> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<SuggestionObatGetSet, suggestObat>(
                SuggestionObatGetSet.class,
                R.layout.suggestion_obat_row,
                suggestObat.class,
                databaseSO
        ) {
            @Override
            protected void populateViewHolder(suggestObat viewHolder, SuggestionObatGetSet model, int position) {
                viewHolder.setSuggest(model.getSuggestion());
                viewHolder.setObat(model.getObat());
            }
        };
        rvSO.setAdapter(firebaseRecyclerAdapter);

    }

    private void start_postingSugesstion() {

        progress.setMessage("Menambahkan data");
        progress.show();
        final String suggest = etSuggest.getText().toString();
        final String obat = etObat.getText().toString();
        if (!TextUtils.isEmpty(suggest)&&!TextUtils.isEmpty(obat)){
            DatabaseReference newPostSO = databaseSO.push();
            newPostSO.child("Suggestion").setValue(suggest);
            newPostSO.child("Obat").setValue(obat);
            newPostSO.child("kode").setValue(kode);
            //Intent i = new Intent(Suggestion.this, MainMenu.class);
            //startActivity(i);
            //finish();
        }
        progress.dismiss();
    }

    public static class suggestObat extends RecyclerView.ViewHolder {
        View viewSuggestObat;

        public suggestObat(View itemView) {
            super(itemView);
            viewSuggestObat  = itemView;
        }

        public void setSuggest(String Suggestion){
            final TextView txtSuggest = (TextView) viewSuggestObat.findViewById(R.id.txtSuggest);
            txtSuggest.setText(Suggestion);
        }

        public void setObat(String Obat){
            final TextView txtObat = (TextView) viewSuggestObat.findViewById(R.id.txtObat);
            txtObat.setText(Obat);
        }
    }
}