package com.example.proyecto_iot.delegadoGeneral.fragmentos;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.databinding.FragmentDgEstadisticasBinding;
import com.example.proyecto_iot.delegadoGeneral.DgEstadisticasDetallesActivity;
import com.example.proyecto_iot.delegadoGeneral.Dg_Activity;
import com.google.android.material.card.MaterialCardView;

public class Dg_estadisticasFragment extends Fragment {
    FragmentDgEstadisticasBinding binding;
    MaterialCardView cardView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentDgEstadisticasBinding.inflate(inflater,container,false);
        // Cambiar el contenido del Toolbar
        ((Dg_Activity) requireActivity()).setToolbarContent("Estadísticas");

        cardView = binding.cardDonaciones;
        cardView.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), DgEstadisticasDetallesActivity.class);
            startActivity(intent);
        });


        return binding.getRoot();
    }
}