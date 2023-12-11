package com.example.proyecto_iot.delegadoActividad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.AlumnoInicioActivity;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.databinding.ActivityDaInicioBinding;
import com.example.proyecto_iot.delegadoGeneral.entity.Actividades;
import com.example.proyecto_iot.delegadoGeneral.utils.FirebaseUtilDg;
import com.example.proyecto_iot.inicioApp.IngresarActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DaInicioActivity extends AppCompatActivity {

    ActivityDaInicioBinding binding;
    Alumno alumnoAutenticado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDaInicioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView bottomNavigationView = binding.bottomNavigationView2;
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerViewHost2);
        NavController navController = NavHostFragment.findNavController(navHostFragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);


    /*Siempre que iniciamos sesion llegarmos a la actividad principal
        y aqui se va a generar el token para las notificaciones*/
        getFCMToken();

    }
    public void getFCMToken(){
        //recupero el token
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                String token = task.getResult();
                FirebaseUtilDg.getUsuarioActualDetalles().update("fcmToken", token);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        String uid = FirebaseAuth.getInstance().getUid();
        FirebaseUtilDg.getCollAlumnos().document(uid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                alumnoAutenticado = task.getResult().toObject(Alumno.class);
                if (alumnoAutenticado.getEstado().equals("baneado")){
                    cerrarSesion("Su cuenta ha sido baneada");
                }
                else if (alumnoAutenticado.getActividadesId()==null) {
                    cerrarSesion("Ha dejado de ser delegado de actividad.");
                } else if (alumnoAutenticado.getActividadesId()!=null) {
                    boolean valido = false;
                    for (Actividades a : alumnoAutenticado.getActividadesId()) {
                        if (a.getEstado().equals("abierto")) {
                            valido = true;
                        }
                    }
                    if (!valido) {
                        cerrarSesion("Ha dejado de ser delegado de actividad.");
                    } else if (alumnoAutenticado.getActividadesId()!=obtenerActividadesDesdeMemoria()) {
                        guardarDataEnMemoria(alumnoAutenticado);
                    }
                }
            }
            else{
                Log.d("msg-test", "error al recuperar alumno AlumnoInicioActivity");
            }
        });
    }

    private void guardarDataEnMemoria(Alumno alumno) {
        Gson gson = new Gson();
        String alumnoJson = gson.toJson(alumno);
        try (FileOutputStream fileOutputStream = openFileOutput("userData", Context.MODE_PRIVATE);
             FileWriter fileWriter = new FileWriter(fileOutputStream.getFD())) {
            fileWriter.write(alumnoJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cerrarSesion(String mensaje){
        FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()){

                FirebaseAuth.getInstance().signOut(); // deslogueo de firebase auth
                Intent intent = new Intent(DaInicioActivity.this, IngresarActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    private ArrayList<Actividades> obtenerActividadesDesdeMemoria() {
        try (FileInputStream fileInputStream = openFileInput("userData");
             FileReader fileReader = new FileReader(fileInputStream.getFD());
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String jsonData = bufferedReader.readLine();
            Gson gson = new Gson();
            Alumno alumno = gson.fromJson(jsonData, Alumno.class);
            return alumno.getActividadesId();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}