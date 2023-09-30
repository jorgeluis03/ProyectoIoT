package com.example.proyecto_iot.alumno.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.proyecto_iot.alumno.Objetos.Notificacion;
import com.example.proyecto_iot.alumno.RecyclerViews.ListaNotificacionesAdapter;
import com.example.proyecto_iot.databinding.FragmentAlumnoHeader2Binding;
import com.example.proyecto_iot.databinding.FragmentAlumnoNotificacionesBinding;

import java.util.ArrayList;

public class AlumnoNotificacionesFragment extends Fragment {

    FragmentAlumnoNotificacionesBinding binding;
    ArrayList<Notificacion> notificacionList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAlumnoNotificacionesBinding.inflate(inflater, container, false);

        notificacionList.add(new Notificacion("Evento de Semana de Ingeniería - Cristiano Donaldo ha enviado un nuevo mensaje al chat", "hace 2h"));
        notificacionList.add(new Notificacion("Otro evento de Semana de Ingeiería - Kike Ramos ha añadido una nueva foto al evento", "hace 8h"));
        notificacionList.add(new Notificacion("Donaciones - Su registro de donación ha sido aceptado", "hace 2d"));
        notificacionList.add(new Notificacion("Donaciones - Su registro de donación ha sido aceptado", "hace 1d"));

        ListaNotificacionesAdapter adapter = new ListaNotificacionesAdapter();
        adapter.setContext(getContext());
        adapter.setNotificacionList(notificacionList);

        binding.rvNotificaciones.setAdapter(adapter);
        binding.rvNotificaciones.setLayoutManager(new LinearLayoutManager(getContext()));

        return binding.getRoot();
    }
}