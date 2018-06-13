package com.bangunanbersejarah.penemu.bangunanbersejarah.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.bangunanbersejarah.penemu.bangunanbersejarah.R;
import com.bangunanbersejarah.penemu.bangunanbersejarah.api.BaseApiService;
import com.bangunanbersejarah.penemu.bangunanbersejarah.api.UtilsApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.et_nama)
    EditText etNama;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_no_hp)
    EditText etPhoneNumber;
    @BindView(R.id.et_password)
    EditText etPassword;

    String nama, email, phone, password;

    private ProgressDialog loadingDaftar;

    BaseApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);

        apiService = UtilsApi.getAPIService();

        loadingDaftar = new ProgressDialog(this);
        loadingDaftar.setTitle("Loading");
        loadingDaftar.setMessage("Registering your account");
        loadingDaftar.setCancelable(false);
    }

    @OnClick(R.id.btn_register)
    public void register(){
        loadingDaftar.show();
        if(TextUtils.isEmpty(etNama.getText().toString()) || TextUtils.isEmpty(etEmail.getText().toString()) || TextUtils.isEmpty(etPhoneNumber.getText().toString()) || TextUtils.isEmpty(etPassword.getText().toString())){
            Toast.makeText(this, "Data belum lengkap !", Toast.LENGTH_SHORT).show();
        }
        else{
            nama = etNama.getText().toString();
            email = etEmail.getText().toString();
            phone = etPhoneNumber.getText().toString();
            password = etPassword.getText().toString();
            simpanData(nama, email, phone, password);
        }
    }

    private void simpanData(String nama, String email, String phone, String password){
        apiService.register("", nama, phone, email, password, "", "penemu")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            loadingDaftar.dismiss();
                            finish();
                        } else {
                            loadingDaftar.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loadingDaftar.dismiss();
                        Toast.makeText(getApplicationContext(), "Koneksi internet bermasalah !", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @OnClick(R.id.btn_to_login)
    public void toLogin(){
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
