package com.example.proyecto_iot.inicioApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.databinding.ActivityLoginBinding;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;

    HashMap<String, String> credencials = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        credencials.put("20203248", "messi");
        credencials.put("20200643", "bicho");
        credencials.put("20203554", "pipipi");

        binding.backButton2.setOnClickListener(view -> {
            finish();
        });
        binding.loginSigninButton.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
            startActivity(intent);
        });
        binding.forgotPasswButton.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        binding.loginButton.setOnClickListener(view -> {
            String codigo = binding.inputCodigo.getText().toString();
            String contrasena = binding.inputContrasena.getText().toString();
            if (camposValidos(codigo, contrasena)){
                Intent intent;
                if (codigo.equals("20203248") && contrasena.equals("messi")){
                    //intent = new Intent(LoginActivity.this, )
                } else if (codigo.equals("20200643") && contrasena.equals("bicho")) {

                } else if (codigo.equals("20203554") && contrasena.equals("pipipi")) {

                }
            }
        });
    }

    boolean camposValidos(String codigo, String contrasena){
        return codigo.equals("") && contrasena.equals("");
    }

    void credencialesValidas(){

    }
}