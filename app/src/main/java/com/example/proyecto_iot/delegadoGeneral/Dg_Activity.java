package com.example.proyecto_iot.delegadoGeneral;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.AlumnoPerfilActivity;
import com.example.proyecto_iot.databinding.ActivityDgBinding;
import com.example.proyecto_iot.inicioApp.IngresarActivity;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.material.navigation.NavigationView;

import com.google.firebase.auth.FirebaseAuth;


public class Dg_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    ActivityDgBinding binding;
    BottomNavigationView buttomnavigationDg;
    NavController navController;
    FirebaseAuth mAuth = FirebaseAuth.getInstance(); // autenticacion
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
        NavigationView navigationView = binding.navView;
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

    }
    public void setToolbarContent(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manu_toolbar_dg,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.salir){
            //Logica para salir
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(Dg_Activity.this, IngresarActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        }
        return  true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.config_dg){
            Toast.makeText(this,"Configuracion",Toast.LENGTH_SHORT).show();
        }
        if(id==R.id.salir_dg){
            Toast.makeText(this,"Salir",Toast.LENGTH_SHORT).show();
        }
        if(id==R.id.perfil_dg){
            Toast.makeText(this,"Perfil",Toast.LENGTH_SHORT).show();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
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