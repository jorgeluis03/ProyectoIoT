package com.example.proyecto_iot.delegadoActividad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.databinding.ActivityDaInicioBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class DaInicioActivity extends AppCompatActivity {

    ActivityDaInicioBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDaInicioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView bottomNavigationView = binding.bottomNavigationView2;
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerViewHost2);
        NavController navController = NavHostFragment.findNavController(navHostFragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
}