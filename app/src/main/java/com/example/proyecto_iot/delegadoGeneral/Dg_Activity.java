package com.example.proyecto_iot.delegadoGeneral;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.databinding.ActivityDgBinding;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Dg_Activity extends AppCompatActivity {
    ActivityDgBinding binding;
    BottomNavigationView buttomnavigationDg;
    NavController navController;
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


        }
        return  true;
    }
}