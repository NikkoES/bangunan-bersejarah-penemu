package com.bangunanbersejarah.penemu.bangunanbersejarah.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Nikko Eka Saputra on 4/3/2018.
 */

public class PengajuanResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private List<Pengajuan> listPengajuan;

    public String getStatus() {
        return status;
    }

    public List<Pengajuan> getListPengajuan() {
        return listPengajuan;
    }
}