package com.example.proyecto_iot.delegadoGeneral.fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.databinding.FragmentDgEstadisticasBinding;
import com.example.proyecto_iot.databinding.FragmentDgNotificacionesBinding;
import com.example.proyecto_iot.delegadoGeneral.Dg_Activity;

public class Dg_notificacionesFragment extends Fragment {
    FragmentDgNotificacionesBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentDgNotificacionesBinding.inflate(inflater,container,false);
        // Cambiar el contenido del Toolbar
        ((Dg_Activity) requireActivity()).setToolbarContent("Notificaciones");

        return binding.getRoot();
    }
}