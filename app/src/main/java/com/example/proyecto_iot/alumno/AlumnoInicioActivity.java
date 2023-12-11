package com.example.proyecto_iot.alumno;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.databinding.ActivityAlumnoInicioBinding;
import com.example.proyecto_iot.delegadoGeneral.dto.ActividadesDto;
import com.example.proyecto_iot.delegadoGeneral.entity.Actividades;
import com.example.proyecto_iot.delegadoGeneral.utils.FirebaseUtilDg;
import com.example.proyecto_iot.inicioApp.IngresarActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

public class AlumnoInicioActivity extends AppCompatActivity {

    ActivityAlumnoInicioBinding binding;
    Alumno alumnoAutenticado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlumnoInicioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        BottomNavigationView bottomNavigationView = binding.bottomNavigationView;
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerViewHost);
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
                else if (alumnoAutenticado.getActividadesId()!=null) {
                    boolean valido = false;
                    for (Actividades a : alumnoAutenticado.getActividadesId()) {
                        if (a.getEstado().equals("abierto")) {
                            valido = true;
                        }
                    }
                    if (valido) {
                        cerrarSesion("Usted cuenta con nuevas actividades. Inicie sesión nuevamente para verlas.");
                    }
                }
            }
            else{
                Log.d("msg-test", "error al recuperar alumno AlumnoInicioActivity");
            }
        });
    }

    private void cerrarSesion(String mensaje){
        FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()){

                FirebaseAuth.getInstance().signOut(); // deslogueo de firebase auth
                Intent intent = new Intent(AlumnoInicioActivity.this, IngresarActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}