package com.example.proyecto_iot.inicioApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.proyecto_iot.alumno.AlumnoInicioActivity;
import com.example.proyecto_iot.alumno.AlumnoPerfilActivity;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.databinding.ActivityIngresarBinding;
import com.example.proyecto_iot.delegadoActividad.DaInicioActivity;
import com.example.proyecto_iot.delegadoGeneral.Dg_Activity;
import com.example.proyecto_iot.delegadoGeneral.entity.Actividades;
import com.example.proyecto_iot.delegadoGeneral.utils.FirebaseUtilDg;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
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
    private Alumno alumnoAutenticado;

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

            /*
            FirebaseUtilDg.getCollAlumnos().document(userUid).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    alumnoAutenticado = task.getResult().toObject(Alumno.class);
                    if (alumnoAutenticado.getEstado().equals("baneado")){
                        cerrarSesion();
                    }
                    else{
                        redirigirSegunRol(alumnoAutenticado);
                    }
                }
            });
            */


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

    private void cerrarSesion(){
        FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()){

                FirebaseAuth.getInstance().signOut(); // deslogueo de firebase auth
                Intent intent = new Intent(IngresarActivity.this, IngresarActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                Toast.makeText(this, "Su cuenta ha sido baneada, revise su correo ("+alumnoAutenticado.getCorreo()+") para más información", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}