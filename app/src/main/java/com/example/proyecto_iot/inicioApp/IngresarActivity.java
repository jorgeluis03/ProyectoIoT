package com.example.proyecto_iot.inicioApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.proyecto_iot.alumno.AlumnoInicioActivity;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.databinding.ActivityIngresarBinding;
import com.example.proyecto_iot.delegadoActividad.DaInicioActivity;
import com.example.proyecto_iot.delegadoGeneral.Dg_Activity;
import com.example.proyecto_iot.delegadoGeneral.entity.Actividades;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class IngresarActivity extends AppCompatActivity {

    private ActivityIngresarBinding binding;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Intent intent;
    String userUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIngresarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.buttonRegistrarme.setOnClickListener(view -> {
            intent = new Intent(IngresarActivity.this, RegistroActivity.class);
            startActivity(intent);
        });

        binding.buttonIniciarSesion.setOnClickListener(view -> {
            intent = new Intent(IngresarActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

            userUid = currentUser.getUid();

            try (FileInputStream fileInputStream = openFileInput("userData");
                 FileReader fileReader = new FileReader(fileInputStream.getFD());
                 BufferedReader bufferedReader = new BufferedReader(fileReader)){

                String jsonData = bufferedReader.readLine();
                Gson gson = new Gson();
                Alumno alumnoAutenticado = gson.fromJson(jsonData, Alumno.class);

                redirigirSegunRol(alumnoAutenticado);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    void redirigirSegunRol(Alumno alumno) {
        Intent intent = null;
        String rol = alumno.getRol();
        ArrayList<Actividades> actividades = alumno.getActividadesId();
        boolean valido = false;
        switch (rol) {
            case "Alumno":
                if (alumno.getActividadesId() == null){
                    // caso no actividades
                    intent = new Intent(IngresarActivity.this, AlumnoInicioActivity.class);
                    break;

                }else {
                    for (Actividades a: actividades){
                        if (a.getEstado().equals("abierto")){
                            valido = true;
                        }
                    }
                    if (valido){
                        // caso delegadoActividad
                        intent = new Intent(IngresarActivity.this, DaInicioActivity.class);
                        break;
                    }
                    // caso no actividades
                    intent = new Intent(IngresarActivity.this, AlumnoInicioActivity.class);
                    break;
                }
            case "Delegado General":
                intent = new Intent(IngresarActivity.this, Dg_Activity.class);
                break;
        }
        startActivity(intent);
        finish();
    }



}