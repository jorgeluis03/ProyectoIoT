package com.example.proyecto_iot.alumno.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_iot.alumno.Objetos.Evento;
import com.example.proyecto_iot.alumno.Objetos.Lugar;
import com.example.proyecto_iot.alumno.RecyclerViews.ListaEventosAdapter;
import com.example.proyecto_iot.databinding.FragmentAlumnoEventosTodosBinding;

import java.util.ArrayList;

public class AlumnoEventosTodosFragment extends Fragment {

    private ArrayList<Evento> eventoList = new ArrayList<>();

    FragmentAlumnoEventosTodosBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAlumnoEventosTodosBinding.inflate(inflater, container, false);

        //eventos hardcodeados
        eventoList.add(new Evento("Evento de Semana de Ingeniería",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec tortor mi, vehicula sit.",
                "Nombre de actividad del evento",
                "10/09/23",
                "10:00",
                new Lugar("Cancha minas", 0),false));

        eventoList.add(new Evento("Otro evento de Semana de Ingeniería",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec tortor mi, vehicula sit.",
                "Nombre de actividad del evento",
                "13/09/23",
                "Polideportivo",
                new Lugar("Polideportivo",0), false));

        ListaEventosAdapter adapter = new ListaEventosAdapter();
        adapter.setContext(getContext());
        adapter.setEventoList(eventoList);

        binding.rvEventos.setAdapter(adapter);
        binding.rvEventos.setLayoutManager(new LinearLayoutManager(getContext()));

        return binding.getRoot();
    }
}