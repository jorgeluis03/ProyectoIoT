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


public class Dg_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    ActivityDgBinding binding;
    BottomNavigationView buttomnavigationDg;
    NavController navController;
    FirebaseAuth auth = FirebaseAuth.getInstance(); // autenticacion
    FirebaseFirestore db;

    ImageView imgPerfilDrawer;
    TextView usernamePerfilDrawer;
    Alumno usuarioActual;
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
        imgPerfilDrawer = drawerLayout.findViewById(R.id.imgPerfilDrawer);
        usernamePerfilDrawer = drawerLayout.findViewById(R.id.textViewUsernameDrawer);

        NavigationView navigationView = binding.navView;
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //getDatosUsuario();

        buttomnavigationDg = binding.buttomnavigationDg;
        //Cargar el navigationComponent (navHost) en el bottomnavigation
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment_dg);
        navController = NavHostFragment.findNavController(navHostFragment);
        NavigationUI.setupWithNavController(buttomnavigationDg,navController);
        //===============================================================

    }
    public void getDatosUsuario(){

        //obtener la foto mia de FirebaseStorage (Descargar archivos)
        /*FirebaseUtilDg.getPerfilUsuarioActualPicStorageRef().getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Uri uri = task.getResult();
                        //carga el uri en la ImageView
                        AndroidUtilDg.setPerfilImg(Dg_Activity.this,uri,imgPerfilDrawer);
                    }
                });*/

        FirebaseUtilDg.getUsuarioActualDetalles().get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Log.d("msg-test","task success");
                usuarioActual = task.getResult().toObject(Alumno.class);
                usernamePerfilDrawer.setText(usuarioActual.getNombre()+' '+usuarioActual.getApellidos());
                Log.d("msg-test",usuarioActual.getNombre()+' '+usuarioActual.getApellidos());
            }else {
                Log.d("msg-test","task error");
            }
        });
    }
    public void setToolbarContent(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manu_toolbar_dg,menu);

        MenuItem menuItem = menu.findItem(R.id.buscar);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Buscar");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        return  true;
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
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(Dg_Activity.this, IngresarActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

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