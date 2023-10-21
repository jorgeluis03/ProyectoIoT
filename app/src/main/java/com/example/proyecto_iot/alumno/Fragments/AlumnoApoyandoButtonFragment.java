package com.example.proyecto_iot.alumno.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.databinding.FragmentAlumnoApoyandoButtonBinding;

public class AlumnoApoyandoButtonFragment extends Fragment {

    FragmentAlumnoApoyandoButtonBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAlumnoApoyandoButtonBinding.inflate(inflater, container, false);

        binding.buttonEventoApoyando.setOnClickListener(view -> {
            AlumnoApoyarButtonFragment apoyarFragment = new AlumnoApoyarButtonFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerViewEventoButtons, apoyarFragment)
                    .addToBackStack(null)
                    .commit();
        });

        binding.buttonAbrirChat.setOnClickListener(view -> {

        });

        return binding.getRoot();
    }
}