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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.proyecto_iot.alumno.Objetos.Evento;
import com.example.proyecto_iot.alumno.RecyclerViews.ListaEventosAdapter;
import com.example.proyecto_iot.databinding.ActivityAlumnoInicioBinding;
import com.google.android.material.tabs.TabLayout;
import com.example.proyecto_iot.R;

import java.util.ArrayList;

public class AlumnoInicioActivity extends AppCompatActivity {

    private ArrayList<Evento> eventoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alumno_inicio);
        // Cambiar el color de la barra de estado aquí
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#0A0E19"));
        }

        //eventos hardcodeados
        /*eventoList.add(new Evento("Evento de Semana de Ingeniería", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec tortor mi, vehicula sit.", "Nombre de actividad del evento", "10/09/23", "Cancha de minas"));

        ListaEventosAdapter adapter = new ListaEventosAdapter();
        adapter.setContext(AlumnoInicioActivity.this);
        adapter.setEventoList(eventoList);

        binding.rvEventos.setAdapter(adapter);
        binding.rvEventos.setLayoutManager(new LinearLayoutManager(AlumnoInicioActivity.this));

         */

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

