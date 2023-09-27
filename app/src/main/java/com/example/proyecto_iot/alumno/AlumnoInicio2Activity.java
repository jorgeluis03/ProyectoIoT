package com.example.proyecto_iot.alumno;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.databinding.ActivityAlumnoInicio2Binding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AlumnoInicio2Activity extends AppCompatActivity {

    ActivityAlumnoInicio2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlumnoInicio2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView bottomNavigationView = binding.bottomNavigationView;
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerViewHost);
        NavController navController = NavHostFragment.findNavController(navHostFragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
}