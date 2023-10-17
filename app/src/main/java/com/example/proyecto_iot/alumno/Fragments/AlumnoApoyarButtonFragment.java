package com.example.proyecto_iot.alumno.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.databinding.FragmentAlumnoApoyarButtonBinding;

public class AlumnoApoyarButtonFragment extends Fragment {

    FragmentAlumnoApoyarButtonBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAlumnoApoyarButtonBinding.inflate(inflater, container, false);

        binding.buttonEventoApoyar.setOnClickListener(view -> {
            AlumnoApoyandoButtonFragment apoyandoFragment = new AlumnoApoyandoButtonFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerViewEventoButtons, apoyandoFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return binding.getRoot();
    }
}