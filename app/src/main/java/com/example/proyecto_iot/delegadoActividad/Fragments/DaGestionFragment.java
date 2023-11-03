package com.example.proyecto_iot.delegadoActividad.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.databinding.FragmentDaGestionBinding;
import com.example.proyecto_iot.delegadoActividad.Adapters.ListaActividadesAdpater;
import com.example.proyecto_iot.delegadoActividad.Entities.Actividad;

import java.util.ArrayList;

public class DaGestionFragment extends Fragment {

    FragmentDaGestionBinding binding;
    ArrayList<Actividad> actividadList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDaGestionBinding.inflate(inflater, container, false);

        actividadList.add(new Actividad("Práctica de barra Básquet", "26/07","15:40"));
        actividadList.add(new Actividad("Práctica de barra Básquet", "26/07","15:40"));
        actividadList.add(new Actividad("Práctica de barra Básquet", "26/07","15:40"));
        actividadList.add(new Actividad("Práctica de barra Básquet", "26/07","15:40"));
        actividadList.add(new Actividad("Práctica de barra Básquet", "26/07","15:40"));
        actividadList.add(new Actividad("Práctica de barra Básquet", "26/07","15:40"));


        ListaActividadesAdpater adapter = new ListaActividadesAdpater();
        adapter.setContext(getContext());
        adapter.setActividadList(actividadList);

        binding.rvActividades.setAdapter(adapter);
        binding.rvActividades.setLayoutManager(new LinearLayoutManager(getContext()));

        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Espera a que la vista esté lista antes de acceder a ella
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // La vista está lista
                TextView textView = view.findViewById(R.id.textHeader2);
                textView.setText("Gestionar eventos");

                TextView textView2 = view.findViewById(R.id.textView24);
                textView2.setText("Actividad 1");

                // Elimina el listener para que no se vuelva a llamar
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }
}