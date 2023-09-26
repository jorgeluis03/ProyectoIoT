package com.example.proyecto_iot.delegadoGeneral;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.databinding.ActivityDgBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Dg_Activity extends AppCompatActivity {
    ActivityDgBinding binding;
    BottomNavigationView buttomnavigationDg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDgBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Mapear el toolbar como ActionBar
        Toolbar toolbar = binding.toolbarActividadesDg;
        setSupportActionBar(toolbar);
        //================================
        buttomnavigationDg = binding.buttomnavigationDg;
        //Cargar el navigationComponent (navHost) en el bottomnavigation
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment_dg);
        NavController navController = NavHostFragment.findNavController(navHostFragment);
        NavigationUI.setupWithNavController(buttomnavigationDg,navController);
        //===============================================================

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_dg,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.buscar_dg){
            Toast.makeText(this,"Buscar actividad",Toast.LENGTH_SHORT).show();
        }
        if (id==R.id.perfil_dg){
            Toast.makeText(this,"Perfil",Toast.LENGTH_SHORT).show();
        }
        if (id==R.id.configuracion_dg){
            Toast.makeText(this,"Configuracion",Toast.LENGTH_SHORT).show();
        }
        if (id==R.id.cerrarSesion_dg){
            Toast.makeText(this,"Salir",Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}