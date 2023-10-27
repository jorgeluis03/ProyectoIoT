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
import com.example.proyecto_iot.databinding.FragmentAlumnoEventosApoyandoBinding;

import java.util.ArrayList;

public class AlumnoEventosApoyandoFragment extends Fragment {

    private ArrayList<Evento> eventoList = new ArrayList<>();

    private FragmentAlumnoEventosApoyandoBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAlumnoEventosApoyandoBinding.inflate(inflater, container, false);

        //eventos hardcodeados
        eventoList.add(new Evento("Segundo evento de la semana",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec tortor mi, vehicula sit.",
                "Nombre de actividad del evento",
                "10/09/23",
                "10:00",
                new Lugar("Cancha minas", 0),true));

        eventoList.add(new Evento("Otro evento de Semana de Ingeniería",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec tortor mi, vehicula sit.",
                "Nombre de actividad del evento",
                "13/09/23",
                "13:30",
                new Lugar("Polideportivo",0), true));

        ListaEventosAdapter adapter = new ListaEventosAdapter();
        adapter.setContext(getContext());
        adapter.setEventoList(eventoList);

        binding.rvEventosAp.setAdapter(adapter);
        binding.rvEventosAp.setLayoutManager(new LinearLayoutManager(getContext()));

        return binding.getRoot();
    }
}