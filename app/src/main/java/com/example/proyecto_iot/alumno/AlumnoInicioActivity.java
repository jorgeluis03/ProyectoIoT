package com.example.proyecto_iot.alumno;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.graphics.Typeface;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.example.proyecto_iot.alumno.Fragments.PackageAlumnoButtonFragment;
import com.example.proyecto_iot.alumno.Fragments.PackageAlumnoHeaderFragment;
import com.google.android.material.tabs.TabLayout;
import com.example.proyecto_iot.R;

public class AlumnoInicioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alumno_inicio);
        // Cambiar el color de la barra de estado aquí
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#0A0E19"));
        }

        // Obtén una referencia al TabLayout
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        ViewPager2 viewPager = findViewById(R.id.viewPager);
        AlumnoInicioViewPagerAdapter viewPagerAdapter = new AlumnoInicioViewPagerAdapter(AlumnoInicioActivity.this);
        viewPager.setAdapter(viewPagerAdapter);

        // Configura el oyente para el TabLayout
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                TextView tabTextView = (TextView) tab.getCustomView();
                if (tabTextView != null) {
                    tabTextView.setTypeface(null, Typeface.BOLD);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView tabTextView = (TextView) tab.getCustomView();
                if (tabTextView != null) {
                    tabTextView.setTypeface(null, Typeface.NORMAL);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Hacer algo cuando un tab ya seleccionado es reseleccionado si es necesario
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

        // Instanciar los fragmentos
        Fragment packageAlumnoButtonFragment = new PackageAlumnoButtonFragment();
        Fragment packageAlumnoHeaderFragment = new PackageAlumnoHeaderFragment();

        // Realizar la transacción de fragmento
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Reemplazar contenedores
        fragmentTransaction.replace(R.id.topFragmentContainer, packageAlumnoHeaderFragment);
        fragmentTransaction.replace(R.id.bottomFragmentContainer, packageAlumnoButtonFragment);

        // Commit
        fragmentTransaction.commit();
    }
}

