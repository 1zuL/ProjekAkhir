package com.ijul.projekpembunuh;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.cast.framework.SessionManager;
import com.ijul.projekpembunuh.api.ApiClient;
import com.ijul.projekpembunuh.api.ApiInterface;
import com.ijul.projekpembunuh.model.login.Login;
import com.ijul.projekpembunuh.model.login.LoginData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etUsername, etPassword;
    Button btnLogin;
    String Username, Password;
    TextView tvRegister;
    SessionManager sessionManager;
    ApiInterface apiInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this::onClick);

        tvRegister = findViewById(R.id.tvCreateAccount);
        tvRegister.setOnClickListener(this::onClick);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                Username = etUsername.getText().toString();
                Password = etPassword.getText().toString();
                login(Username, Password);
                break;
            case R.id.tvCreateAccount:
                Intent intent = new Intent(MainActivity.this, Daftar.class);
                startActivity(intent);
                break;
        }
    }

    private void login(String username, String password) {

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Login> loginCall = apiInterface.loginResponse(username, password);
        loginCall.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if (response.body() != null && response.isSuccessful() && response.body().isStatus()) {

                  /*  // Ini untuk menyimpan sesi
                    sessionManager = new SessionManager(MainActivity.this);
                    LoginData loginData = response.body().getLoginData();
                    sessionManager.startSession(loginData);


                   */


                    //Ini untuk pindah
                    Toast.makeText(MainActivity.this, response.body().getLoginData().getName(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, Dasboard.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        tvRegister = findViewById(R.id.tvCreateAccount);
        tvRegister.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Daftar.class);
            startActivity(intent);
        });
    }
}
