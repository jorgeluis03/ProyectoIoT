package com.example.proyecto_iot.inicioApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.proyecto_iot.alumno.AlumnoEventoActivity;
import com.example.proyecto_iot.alumno.AlumnoInicioActivity;
import com.example.proyecto_iot.databinding.ActivityLoginBinding;
import com.example.proyecto_iot.delegadoGeneral.Dg_Activity;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    boolean check = false;

    HashMap<String, String> credenciales = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        credenciales.put("20203248", "messi"); // alumno
        credenciales.put("20200643", "bicho"); // delegado general
        credenciales.put("20203554", "pipipi"); // delegado actividad

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
            for(Map.Entry<String, String> credencial: credenciales.entrySet()){
                if (credencial.getKey().equals(codigo) && credencial.getValue().equals(contrasena)){
                    check = true;
                    Intent intent = null;
                    if (codigo.equals("20203248")){
                        intent = new Intent(LoginActivity.this, AlumnoInicioActivity.class);
                    } else if (codigo.equals("20200643")) {
                        intent = new Intent(LoginActivity.this, Dg_Activity.class);
                    }
                    //else if (codigo.equals("20203554")) {

                    //}
                    startActivity(intent);
                }
            }
            if (!check){
                Snackbar.make(binding.getRoot(), "Las credenciales son incorrectas.", Snackbar.LENGTH_SHORT)
                        .show();
            }
        });
    }

    boolean camposValidos(String codigo, String contrasena){
        return !(codigo.equals("") && contrasena.equals(""));
    }

    void credencialesValidas(){

    }
}