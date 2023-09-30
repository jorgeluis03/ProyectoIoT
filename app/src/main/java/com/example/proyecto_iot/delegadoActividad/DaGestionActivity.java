package com.example.proyecto_iot.delegadoActividad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.databinding.ActivityDaGestionBinding;
import com.example.proyecto_iot.databinding.FragmentAlumnoNotificacionesBinding;
import com.example.proyecto_iot.delegadoActividad.Adapters.ListaActividadesAdpter;
import com.example.proyecto_iot.delegadoActividad.Entities.Actividad;

import java.util.ArrayList;

public class DaGestionActivity extends AppCompatActivity {

    ActivityDaGestionBinding binding;

    ArrayList<Actividad> actividadList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDaGestionBinding.inflate(getLayoutInflater());

        actividadList.add(new Actividad("Práctica de barra Básquet", "26/07","15:40"));

        ListaActividadesAdpter adapter = new ListaActividadesAdpter();
        adapter.setContext(this);
        adapter.setActividadList(actividadList);

        binding.rvActividades.setAdapter(adapter);
        binding.rvActividades.setLayoutManager(new LinearLayoutManager(this));

        setContentView(binding.getRoot());
    }
}