package com.bangunanbersejarah.penemu.bangunanbersejarah.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bangunanbersejarah.penemu.bangunanbersejarah.R;
import com.bangunanbersejarah.penemu.bangunanbersejarah.activity.DetailBangunanActivity;
import com.bangunanbersejarah.penemu.bangunanbersejarah.api.BaseApiService;
import com.bangunanbersejarah.penemu.bangunanbersejarah.api.UtilsApi;
import com.bangunanbersejarah.penemu.bangunanbersejarah.model.Bangunan;
import com.bangunanbersejarah.penemu.bangunanbersejarah.model.Pengajuan;
import com.bumptech.glide.Glide;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nikko Eka Saputra on 4/2/2018.
 */

public class PengajuanAdapter extends RecyclerView.Adapter<PengajuanAdapter.ViewHolder> {

    private Context context;
    private List<Pengajuan> listPengajuan;

    BaseApiService apiService;

    public PengajuanAdapter(Context context, List<Pengajuan> listPengajuan){
        this.context = context;
        this.listPengajuan = listPengajuan;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_pengajuan, null, false);

        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(layoutParams);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        apiService = UtilsApi.getAPIService();

        final Pengajuan bangunan = listPengajuan.get(position);
        final String idPengajuan = bangunan.getIdPengajuan();
        final String status = bangunan.getStatus();

        String sStatus;

        if(status.equalsIgnoreCase("1")){
            sStatus = "diterima";
            holder.btnBatal.setVisibility(View.GONE);
        }
        else if(status.equalsIgnoreCase("0")){
            sStatus = "diproses";
            holder.btnBatal.setVisibility(View.VISIBLE);
        }
        else{
            sStatus = "ditolak";
            holder.btnBatal.setVisibility(View.VISIBLE);
        }

        holder.namaBangunan.setText(bangunan.getNamaBangunan());
        holder.tanggalPengajuan.setText(bangunan.getTanggalPengajuan());
        holder.txtStatus.setText(sStatus);
        holder.btnPengajuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, DetailBangunanActivity.class);
                i.putExtra("id_pengajuan", bangunan.getIdPengajuan());
                i.putExtra("nama_bangunan", bangunan.getNamaBangunan());
                i.putExtra("sejarah_bangunan", bangunan.getSejarahBangunan());
                i.putExtra("image_bangunan", bangunan.getImageBangunan());
                i.putExtra("alamat_bangunan", bangunan.getAlamatBangunan());
                i.putExtra("id_provinsi", bangunan.getIdProvinsi());
                i.putExtra("id_daerah", bangunan.getIdDaerah());
                context.startActivity(i);
                ((Activity) context).overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
        holder.btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Anda yakin ingin membatalkan pengajuan ini ?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                hapusPengajuan(bangunan.getIdPengajuan(), position);
                            }
                        })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        });
    }

    private void hapusPengajuan(String idBangunan, final int position){
        apiService.deletePengajuan(idBangunan).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Toast.makeText(context, "Berhasil membatalkan pengajuan", Toast.LENGTH_SHORT).show();
                    listPengajuan.remove(position);
                    notifyDataSetChanged();
                }
                else {
                    Toast.makeText(context, "Ada kesalahan !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Koneksi internet bermasalah !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPengajuan.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView btnPengajuan;
        private TextView namaBangunan, tanggalPengajuan, txtStatus;
        private ImageView btnBatal;

        public ViewHolder(View itemView) {
            super(itemView);

            btnPengajuan = (CardView) itemView.findViewById(R.id.cv_pengajuan);
            namaBangunan = (TextView) itemView.findViewById(R.id.txt_nama_bangunan);
            txtStatus = (TextView) itemView.findViewById(R.id.txt_status);
            tanggalPengajuan = (TextView) itemView.findViewById(R.id.txt_tanggal_pengajuan);
            btnBatal = (ImageView) itemView.findViewById(R.id.btn_batal);
        }
    }
}