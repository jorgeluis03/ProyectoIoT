package com.example.proyecto_iot.delegadoGeneral;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.databinding.ActivityMainBinding;
import com.example.proyecto_iot.delegadoGeneral.ActividadesFragment;
import com.example.proyecto_iot.delegadoGeneral.AlumnosFragment;
import com.example.proyecto_iot.delegadoGeneral.EstadisticasFragment;
import com.example.proyecto_iot.delegadoGeneral.NorificacionFragment;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    ActividadesFragment actividadesFragment = new ActividadesFragment();
    NorificacionFragment norificacionFragment= new NorificacionFragment();
    AlumnosFragment alumnosFragment = new AlumnosFragment();
    EstadisticasFragment estadisticasFragment = new EstadisticasFragment();
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        replaceFragment(actividadesFragment);


        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                 int itemID = item.getItemId();
                if(itemID == R.id.page_1){
                    replaceFragment(actividadesFragment);
                    item.setIcon(R.drawable.home_dark);
                    binding.bottomNavigation.getMenu().findItem(R.id.page_2).setIcon(R.drawable.users_line);
                    binding.bottomNavigation.getMenu().findItem(R.id.page_3).setIcon(R.drawable.notific_line);
                    binding.bottomNavigation.getMenu().findItem(R.id.page_4).setIcon(R.drawable.statistic_line);
                    return true;
                }
                 if(itemID == R.id.page_2){
                     replaceFragment(new AlumnosFragment());
                     item.setIcon(R.drawable.users_dark);
                     binding.bottomNavigation.getMenu().findItem(R.id.page_1).setIcon(R.drawable.home_line);
                     binding.bottomNavigation.getMenu().findItem(R.id.page_3).setIcon(R.drawable.notific_line);
                     binding.bottomNavigation.getMenu().findItem(R.id.page_4).setIcon(R.drawable.statistic_line);
                     return true;
                 }
                if(itemID == R.id.page_3){
                    replaceFragment(norificacionFragment);
                    item.setIcon(R.drawable.notific_dark);
                    binding.bottomNavigation.getMenu().findItem(R.id.page_2).setIcon(R.drawable.users_line);
                    binding.bottomNavigation.getMenu().findItem(R.id.page_1).setIcon(R.drawable.home_line);
                    binding.bottomNavigation.getMenu().findItem(R.id.page_4).setIcon(R.drawable.statistic_line);
                    return true;
                }
                if(itemID == R.id.page_4){
                    replaceFragment(new EstadisticasFragment());
                    item.setIcon(R.drawable.statistic_dark);
                    binding.bottomNavigation.getMenu().findItem(R.id.page_2).setIcon(R.drawable.users_line);
                    binding.bottomNavigation.getMenu().findItem(R.id.page_3).setIcon(R.drawable.notific_line);
                    binding.bottomNavigation.getMenu().findItem(R.id.page_1).setIcon(R.drawable.home_line);
                    return true;
                }

                return false;
            }

        });

    }

    private void replaceFragment (Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container,fragment);
        fragmentTransaction.commit();
    }
}