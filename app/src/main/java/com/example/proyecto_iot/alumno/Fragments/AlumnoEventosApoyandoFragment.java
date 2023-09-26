package com.example.proyecto_iot.alumno.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.databinding.FragmentAlumnoEventosApoyandoBinding;

public class AlumnoEventosApoyandoFragment extends Fragment {

    private FragmentAlumnoEventosApoyandoBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAlumnoEventosApoyandoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}