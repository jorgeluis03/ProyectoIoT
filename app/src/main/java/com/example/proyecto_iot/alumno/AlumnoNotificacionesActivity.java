package com.example.proyecto_iot.alumno;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Objetos.Notificacion;
import com.example.proyecto_iot.alumno.RecyclerViews.ListaNotificacionesAdapter;
import com.example.proyecto_iot.databinding.ActivityAlumnoNotificacionesBinding;

import java.util.ArrayList;
import java.util.List;

public class AlumnoNotificacionesActivity extends AppCompatActivity {

    private List<Notificacion> notificacionList = new ArrayList<>();
    private ActivityAlumnoNotificacionesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlumnoNotificacionesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        notificacionList.add(new Notificacion("Evento de Semana de Ingeniería - Cristiano Donaldo ha enviado un nuevo mensaje al chat", "hace 2h"));
        notificacionList.add(new Notificacion("Otro evento de Semana de Ingeiería - Kike Ramos ha añadido una nueva foto al evento", "hace 8h"));
        notificacionList.add(new Notificacion("Donaciones - Su registro de donación ha sido aceptado", "hace 2d"));
        notificacionList.add(new Notificacion("Donaciones - Su registro de donación ha sido aceptado", "hace 1d"));

        ListaNotificacionesAdapter adapter = new ListaNotificacionesAdapter();
        adapter.setContext(AlumnoNotificacionesActivity.this);
        adapter.setNotificacionList(notificacionList);

        binding.rvNotificaciones.setAdapter(adapter);
        binding.rvNotificaciones.setLayoutManager(new LinearLayoutManager(AlumnoNotificacionesActivity.this));
    }
}