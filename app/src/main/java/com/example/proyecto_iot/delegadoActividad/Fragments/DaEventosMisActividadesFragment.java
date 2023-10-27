package com.example.proyecto_iot.delegadoActividad.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_iot.alumno.Entities.Evento;
import com.example.proyecto_iot.alumno.Entities.Lugar;
import com.example.proyecto_iot.alumno.RecyclerViews.ListaEventosAdapter;
import com.example.proyecto_iot.databinding.FragmentDaEventosMisActividadesBinding;

import java.util.ArrayList;

public class DaEventosMisActividadesFragment extends Fragment {
    private ArrayList<Evento> eventoList = new ArrayList<>();

    private FragmentDaEventosMisActividadesBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDaEventosMisActividadesBinding.inflate(inflater, container, false);

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

        binding.rvEventosMisAct.setAdapter(adapter);
        binding.rvEventosMisAct.setLayoutManager(new LinearLayoutManager(getContext()));

        return binding.getRoot();
    }
}