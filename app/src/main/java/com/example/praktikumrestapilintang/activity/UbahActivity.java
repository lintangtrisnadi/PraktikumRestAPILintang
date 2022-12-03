package com.example.praktikumrestapilintang.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.praktikumrestapilintang.api.APIRequestData;
import com.example.praktikumrestapilintang.api.RetroServer;
import com.example.praktikumrestapilintang.model.ResponseModel;
import com.example.praktikumrestapilintang.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UbahActivity extends AppCompatActivity {
    private int xId;
    private String xNRP, xNama, xEmail, xJurusan;
    private EditText etNRP, etNama, etEmail, etJurusan;
    private Button btnUbah;
    private String yNRP, yNama, yEmail, yJurusan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah);

        Intent terima = getIntent();
        xId = terima.getIntExtra("xId", -1);
        xNRP = terima.getStringExtra("xNRP");
        xNama = terima.getStringExtra("xNama");
        xEmail = terima.getStringExtra("xEmail");
        xJurusan = terima.getStringExtra("xJurusan");

        etNRP = findViewById(R.id.et_nrp);
        etNama = findViewById(R.id.et_nama);
        etEmail = findViewById(R.id.et_email);
        etJurusan = findViewById(R.id.et_jurusan);
        btnUbah = findViewById(R.id.btn_ubah);

        etNRP.setText(xNRP);
        etNama.setText(xNama);
        etEmail.setText(xEmail);
        etJurusan.setText(xJurusan);

        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yNRP = etNama.getText().toString();
                yNama = etNama.getText().toString();
                yEmail = etEmail.getText().toString();
                yJurusan = etJurusan.getText().toString();

                updateData();
            }
        });
    }

    private void updateData(){
        APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> ubahData = ardData.ardUpdateData(yNRP, yNama, yEmail, yJurusan);

        ubahData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();

                Toast.makeText(UbahActivity.this, "Kode : "+kode+" | Pesan : "+pesan, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(UbahActivity.this, "Gagal Menghubungi Server | "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
