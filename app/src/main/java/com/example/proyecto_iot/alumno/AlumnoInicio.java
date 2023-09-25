package com.example.proyecto_iot.alumno;

import android.os.Bundle;
import android.graphics.Typeface;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.example.proyecto_iot.R;

public class AlumnoInicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno_inicio);

        // Obtén una referencia al TabLayout
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        // Configura el oyente para el TabLayout
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
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
