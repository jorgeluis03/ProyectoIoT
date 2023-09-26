package com.example.proyecto_iot.delegadoGeneral;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;

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

}