package com.bangunanbersejarah.penemu.bangunanbersejarah.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bangunanbersejarah.penemu.bangunanbersejarah.R;
import com.bangunanbersejarah.penemu.bangunanbersejarah.activity.TambahBangunanActivity;
import com.bangunanbersejarah.penemu.bangunanbersejarah.adapter.PengajuanAdapter;
import com.bangunanbersejarah.penemu.bangunanbersejarah.api.BaseApiService;
import com.bangunanbersejarah.penemu.bangunanbersejarah.api.UtilsApi;
import com.bangunanbersejarah.penemu.bangunanbersejarah.model.Pengajuan;
import com.bangunanbersejarah.penemu.bangunanbersejarah.model.PengajuanResponse;
import com.bangunanbersejarah.penemu.bangunanbersejarah.utils.SharedPreferencesUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PengajuanFragment extends Fragment {

    private FloatingActionButton fab;

    private RecyclerView rvHome;
    private PengajuanAdapter adapter;
    List<Pengajuan> listPengajuan = new ArrayList<>();

    ProgressDialog loading;

    BaseApiService apiService;

    String idUser;
    int idBangunan;

    private SharedPreferencesUtils userDataSharedPreferences;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Daftar Pengajuan Bangunan");
    }


    public PengajuanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pengajuan, container, false);

        userDataSharedPreferences = new SharedPreferencesUtils(getContext(), "UserData");
        try {
            JSONObject userProfile = new JSONObject(userDataSharedPreferences.getPreferenceData("userProfile"));
            idUser = userProfile.get("id_user").toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        rvHome = (RecyclerView) view.findViewById(R.id.rv_cicilan);

        adapter = new PengajuanAdapter(getContext(), listPengajuan);

        apiService = UtilsApi.getAPIService();

        rvHome.setHasFixedSize(true);
        rvHome.setLayoutManager(new LinearLayoutManager(getContext()));
        rvHome.setAdapter(adapter);

        refresh(idUser);

        fab = view.findViewById(R.id.btn_add_cicilan);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                idBangunan = (int)(Math.random() * (999 - 99) + 1) + 99;
                Intent i = new Intent(getContext(), TambahBangunanActivity.class);
                startActivity(i);
            }
        });
        adapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    public void refresh(String idUser) {
        loading = ProgressDialog.show(getContext(), null, "Harap Tunggu...", true, false);

        apiService.getPengajuanByUser(idUser).enqueue(new Callback<PengajuanResponse>() {
            @Override
            public void onResponse(Call<PengajuanResponse> call, Response<PengajuanResponse> response) {
                if (response.isSuccessful()){
                    loading.dismiss();

                    listPengajuan = response.body().getListPengajuan();

                    rvHome.setAdapter(new PengajuanAdapter(getContext(), listPengajuan));
                    adapter.notifyDataSetChanged();
                } else {
                    loading.dismiss();
                    Toast.makeText(getContext(), "Gagal koneksi !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PengajuanResponse> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(getContext(), "Koneksi internet bermasalah !", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
