package com.example.praktikumrestapilintang.activity;

import androidx.appcompat.app.AppCompatActivity;

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

public class TambahActivity extends AppCompatActivity {
    private EditText etNRP, etNama, etEmail, etJurusan;
    private Button btnSimpan;
    private String nrp, nama, email, jurusan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);

        etNRP = findViewById(R.id.et_nrp);
        etNama = findViewById(R.id.et_nama);
        etEmail = findViewById(R.id.et_email);
        etJurusan = findViewById(R.id.et_jurusan);
        btnSimpan = findViewById(R.id.btn_simpan);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nrp = etNRP.getText().toString();
                nama = etNama.getText().toString();
                email = etEmail.getText().toString();
                jurusan = etJurusan.getText().toString();

                if(nama.trim().equals("")){
                    etNRP.setError("NRP Harus Diisi");
                }
                if(nama.trim().equals("")){
                    etNama.setError("Nama Harus Diisi");
                }
                else if(email.trim().equals("")){
                    etEmail.setError("Email Harus Diisi");
                }
                else if(jurusan.trim().equals("")){
                    etJurusan.setError("Jurusan Harus Diisi");
                }
                else{
                    createData();
                }
            }
        });
    }

    private void createData(){
        APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> simpanData = ardData.ardCreateData(nrp, nama, email, jurusan);

        simpanData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();

                Toast.makeText(TambahActivity.this, "Kode : "+kode+" | Pesan : "+pesan, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(TambahActivity.this, "Gagal Menghubungi Server | "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
