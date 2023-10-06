package com.example.proyecto_iot.inicioApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.proyecto_iot.alumno.AlumnoInicioActivity;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.databinding.ActivityLoginBinding;
import com.example.proyecto_iot.delegadoGeneral.Dg_Activity;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    boolean check = false;

    ArrayList<Alumno> usuarios = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // alumno
        Alumno alumno = new Alumno("Noe", "Martinez", "alumno", "20203248", "a20203248@pucp.edu.pe", "messi");
        // delegado actividad
        Alumno delegadoActividad = new Alumno("", "", "", "20203554", "", "pipipi");

        //credenciales.put("20200643", "bicho"); // delegado general

        usuarios.add(alumno);
        usuarios.add(delegadoActividad);

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
            for(Alumno usuario: usuarios){
                if (usuario.getCodigo().equals(codigo) && usuario.getContrasena().equals(contrasena)){
                    check = true;
                    Intent intent = null;
                    switch (usuario.getRol()){
                        case "alumno":
                            intent = new Intent(LoginActivity.this, AlumnoInicioActivity.class);
                            intent.putExtra("alumno", usuario);
                            break;
                        case "delegadoActividad":
                            intent = new Intent(LoginActivity.this, AlumnoInicioActivity.class);
                            break;
                        case "delegadoGeneral":
                            intent = new Intent(LoginActivity.this, Dg_Activity.class);
                            break;
                    }

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