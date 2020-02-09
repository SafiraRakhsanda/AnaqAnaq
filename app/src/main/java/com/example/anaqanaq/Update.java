package com.example.anaqanaq;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;


public class Update extends AppCompatActivity implements View.OnClickListener {

    private SimpleDateFormat dateFormatter;
    private DatePickerDialog datePickerDialog;
    private TextView id_tabel;
    private TextView nama_anak;
    private TextView umur_anak;
    private TextView dateView;
    private Calendar c = Calendar.getInstance();

    private String id;
    private int year, month, day;

    private Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        Intent intent = getIntent();
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        id = intent.getStringExtra(konfigurasi.EMP_ID);

        id_tabel = (TextView) findViewById(R.id.id);
        nama_anak = (TextView) findViewById(R.id.update_nama);
        umur_anak = (TextView) findViewById(R.id.update_umur);
        dateView = (TextView) findViewById(R.id.date);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);

        btnUpdate = (Button) findViewById(R.id.btn_update);

        btnUpdate.setOnClickListener(this);

        id_tabel.setText(id);
        getEmployee();

        dateView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDateDialog();
            }

        });
    }

    private void showDateDialog() {
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                dateView.setText(dateFormatter.format(newDate.getTime()));

                Toast.makeText(getApplicationContext(), "Tanggal Dipilih :"+ dateFormatter.format(newDate.getTime()), Toast.LENGTH_LONG).show();
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void getEmployee(){
        class GetEmployee extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Update.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showEmployee(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_GET_EMP,id);
                return s;
            }
        }
        GetEmployee ge = new GetEmployee();
        ge.execute();
    }

    private void showEmployee(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String nama = c.getString(konfigurasi.TAG_NAMA);
            String id = c.getString(konfigurasi.TAG_ID);
            String umur = c.getString(konfigurasi.TAG_UMUR);

            nama_anak.setText(nama);
            id_tabel.setText(id);
            umur_anak.setText(umur);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void updateEmployee(){
        final String nama = nama_anak.getText().toString().trim();
        final String buat_id = id_tabel.getText().toString().trim();
        final String umur = umur_anak.getText().toString().trim();

        class UpdateEmployee extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Update.this,"Updating...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(Update.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(konfigurasi.KEY_EMP_ID, id);
                hashMap.put(konfigurasi.KEY_EMP_NAMA, nama);
                hashMap.put(konfigurasi.KEY_EMP_UMUR, umur);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(konfigurasi.URL_UPDATE_EMP,hashMap);

                return s;
            }
        }

        UpdateEmployee ue = new UpdateEmployee();
        ue.execute();
    }

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
    }

    @Override
    public void onClick(View v) {
        if(v == btnUpdate){
            updateEmployee();
        }
    }

}
