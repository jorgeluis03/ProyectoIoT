package com.example.proyecto_iot.delegadoGeneral;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.databinding.ActivityDgBinding;
import com.example.proyecto_iot.delegadoGeneral.utils.AndroidUtilDg;
import com.example.proyecto_iot.delegadoGeneral.utils.FirebaseUtilDg;
import com.example.proyecto_iot.inicioApp.IngresarActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.material.navigation.NavigationView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.messaging.FirebaseMessaging;


public class Dg_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    ActivityDgBinding binding;
    BottomNavigationView buttomnavigationDg;
    NavController navController;
    NavigationView navigationView;
    ImageView imgPerfilDrawer;
    TextView usernamePerfilDrawer;
    View headerView;
    Alumno delegadoActual;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDgBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Mapear el toolbar como ActionBar
        Toolbar toolbar = binding.toolbarActividadesDg;
        setSupportActionBar(toolbar);
        //================================

        drawerLayout = binding.drawerLayoutDg;
        navigationView= binding.navView;
        headerView= navigationView.getHeaderView(0);
        imgPerfilDrawer = headerView.findViewById(R.id.imgPerfilDrawer);
        usernamePerfilDrawer = headerView.findViewById(R.id.textViewUsernameDrawer);

        cargarDatosDrawer();

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();



        buttomnavigationDg = binding.buttomnavigationDg;
        //Cargar el navigationComponent (navHost) en el bottomnavigation
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment_dg);
        navController = NavHostFragment.findNavController(navHostFragment);
        NavigationUI.setupWithNavController(buttomnavigationDg,navController);
        //===============================================================


        /*Siempre que iniciamos sesion llegarmos a la actividad principal
        y aqui se va a generar el token para las notificaciones*/
        getFCMToken();

    }
    public void cargarDatosDrawer(){
        //obtener la foto mia de FirebaseStorage (Descargar archivos)
        FirebaseUtilDg.getPerfilUsuarioActualPicStorageRef().getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Uri uri = task.getResult();
                        //carga el uri en la ImageView
                        AndroidUtilDg.setPerfilImg(this,uri,imgPerfilDrawer);
                    }
                });
        FirebaseUtilDg.getUsuarioActualDetalles().get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                delegadoActual = task.getResult().toObject(Alumno.class);
                usernamePerfilDrawer.setText(delegadoActual.getNombre()+' '+delegadoActual.getApellidos());
            }
        });
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
    public void setToolbarContent(String title) {
        getSupportActionBar().setTitle(title);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.config_dg){
            Intent intent = new Intent(this, Dg_configuracion.class);
            startActivity(intent);

        }
        if(id==R.id.salir_dg){
            //Logica para salir
            //Borrar el token al cerrar sesion
            FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener(task -> {
                if(task.isSuccessful()){

                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(Dg_Activity.this, IngresarActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                }
            });




        }
        if(id==R.id.perfil_dg){
            Intent intent = new Intent(this, Perfil_dg.class);
            startActivity(intent);

        }

        //drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
}