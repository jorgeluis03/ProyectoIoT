package com.example.proyecto_iot.inicioApp;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;


import com.cometchat.chat.core.AppSettings;
import com.cometchat.chat.core.CometChat;
import com.cometchat.chat.exceptions.CometChatException;
import com.cometchat.chat.models.User;
import com.example.proyecto_iot.AppConstants;

import com.example.proyecto_iot.R;

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
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        binding.buttonIniciarSesion.setOnClickListener(view -> {
            intent = new Intent(IngresarActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

            //logueo en cometchat en caso haya caducado
            userUid = currentUser.getUid();
            verficarLogueoCometChat();

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

    private void verficarLogueoCometChat(){
        String region = AppConstants.REGION;
        String appID = AppConstants.APP_ID;
        String authKey = AppConstants.AUTH_KEY;

        AppSettings appSettings = new AppSettings.AppSettingsBuilder()
                .subscribePresenceForAllUsers()
                .setRegion(region)
                .autoEstablishSocketConnection(true)
                .build();

        CometChat.init(IngresarActivity.this, appID, appSettings, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                Log.d("msg-test", "IngresarActivity: Initialization completed successfully");

                if (CometChat.getLoggedInUser() == null){
                    loguearCometChat(authKey);
                }

            }

            @Override
            public void onError(CometChatException e) {
                Log.d("msg-test", "IngresarActivity: Login failed with exception: " + e.getMessage());
            }
        });
    }

    private void loguearCometChat(String authKey){
        CometChat.login(userUid, authKey, new CometChat.CallbackListener<User>() {
            @Override
            public void onSuccess(User user) {
                Log.d("msg-test", "IngresarActivity: Login Successful : " + user.toString());
            }

            @Override
            public void onError(CometChatException e) {
                Log.d("msg-test", "IngresarActivity: Login failed with exception: " + e.getMessage());
            }
        });
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