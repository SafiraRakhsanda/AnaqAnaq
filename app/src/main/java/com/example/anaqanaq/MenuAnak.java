package com.example.anaqanaq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class MenuAnak extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextId;
    private EditText editTextNama;
    private EditText editTextUmur;
    private EditText editTextTinggi;
    private EditText editTextBerat;

    private Button btn1;
    private Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_anak);

        Intent intent = getIntent();

        editTextId = (EditText) findViewById(R.id.buat_id);
        editTextNama = (EditText) findViewById(R.id.nama_anak);
        editTextUmur = (EditText) findViewById(R.id.umur_anak);
        editTextTinggi = (EditText) findViewById(R.id.tinggi_badan);
        editTextBerat = (EditText) findViewById(R.id.berat_badan);

        btn1 = (Button) findViewById(R.id.button1);
        btn2 = (Button) findViewById(R.id.button2);

        btn1.setOnClickListener(MenuAnak.this);
        btn2.setOnClickListener(MenuAnak.this);

    }

    private void addEmployee() {

        final String id = editTextId.getText().toString().trim();
        final String nama_anak = editTextNama.getText().toString().trim();
        final String umur_anak = editTextUmur.getText().toString().trim();
        final String tinggi_badan = editTextTinggi.getText().toString().trim();
        final String berat_badan = editTextBerat.getText().toString().trim();

        class AddEmployee extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MenuAnak.this, "Menambahkan Data...", "Tunggu", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(MenuAnak.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void...v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(konfigurasi.KEY_EMP_ID, id);
                params.put(konfigurasi.KEY_EMP_NAMA, nama_anak);
                params.put(konfigurasi.KEY_EMP_UMUR, umur_anak);
                params.put(konfigurasi.KEY_EMP_TINGGI, tinggi_badan);
                params.put(konfigurasi.KEY_EMP_BERAT, berat_badan);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(konfigurasi.URL_ADD, params);
                return res;
            }

        }

        AddEmployee ae = new AddEmployee();
        ae.execute();
    }

    public void onClick(View v) {
        if (v == btn1) {
            addEmployee();
        }

        if (v == btn2) {
            startActivity(new Intent(this, DaftarAnak.class));
        }
    }
}
