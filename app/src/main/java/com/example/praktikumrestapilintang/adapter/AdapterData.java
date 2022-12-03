package com.example.praktikumrestapilintang.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.praktikumrestapilintang.api.APIRequestData;
import com.example.praktikumrestapilintang.api.RetroServer;
import com.example.praktikumrestapilintang.activity.MainActivity;
import com.example.praktikumrestapilintang.activity.UbahActivity;
import com.example.praktikumrestapilintang.model.DataModel;
import com.example.praktikumrestapilintang.model.ResponseModel;
import com.example.praktikumrestapilintang.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterData extends RecyclerView.Adapter<AdapterData.HolderData>{
    private Context ctx;
    private List<DataModel> listData;
    private List<DataModel> listMahasiswa;
    private String nrpMahasiswa;

    public AdapterData(Context ctx, List<DataModel> listData) {
        this.ctx = ctx;
        this.listData = listData;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        HolderData holder = new HolderData(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        DataModel dm = listData.get(position);

        holder.tvNrp.setText(String.valueOf(dm.getNrp()));
        holder.tvNama.setText(dm.getNama());
        holder.tvEmail.setText(dm.getEmail());
        holder.tvJurusan.setText(dm.getJurusan());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class HolderData extends RecyclerView.ViewHolder {
        TextView tvNrp, tvNama, tvEmail, tvJurusan;

        public HolderData(@NonNull View itemView) {
            super(itemView);

            tvNrp = itemView.findViewById(R.id.tv_nrp);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvEmail = itemView.findViewById(R.id.tv_email);
            tvJurusan = itemView.findViewById(R.id.tv_jurusan);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder dialogPesan = new AlertDialog.Builder(ctx);
                    dialogPesan.setMessage("Pilih Operasi yang Akan Dilakukan");
                    dialogPesan.setTitle("Perhatian");
                    dialogPesan.setIcon(R.mipmap.ic_launcher_round);
                    dialogPesan.setCancelable(true);

                    dialogPesan.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteData();
                            dialogInterface.dismiss();
                            Handler hand = new Handler();
                            hand.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ((MainActivity) ctx).retrieveData();
                                }
                            }, 1000);
                        }
                    });

                    dialogPesan.setNegativeButton("Ubah", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getData();
                            dialogInterface.dismiss();
                        }
                    });

                    dialogPesan.show();

                    return false;
                }
            });
        }

        private void deleteData(){
            APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
            Call<ResponseModel> hapusData = ardData.ardDeleteData(nrpMahasiswa);

            hapusData.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();

                    Toast.makeText(ctx, "Kode : "+kode+" | Pesan : "+pesan, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(ctx, "Gagal Menghubungi Server : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void getData(){
            APIRequestData ardData = RetroServer.konekRetrofit().create(APIRequestData.class);
            Call<ResponseModel> ambilData = ardData.ardGetData(nrpMahasiswa);

            ambilData.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();
                    listMahasiswa = response.body().getData();

                    int varNRP = listMahasiswa.get(0).getNrp();
                    String varNama = listMahasiswa.get(0).getNama();
                    String varEmail = listMahasiswa.get(0).getEmail();
                    String varJurusan = listMahasiswa.get(0).getJurusan();

                    Intent kirim = new Intent(ctx, UbahActivity.class);
                    kirim.putExtra("xNRP", varNRP);
                    kirim.putExtra("xNama", varNama);
                    kirim.putExtra("xEmail", varEmail);
                    kirim.putExtra("xJurusan", varJurusan);
                    ctx.startActivity(kirim);
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(ctx, "Gagal Menghubungi Server : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
