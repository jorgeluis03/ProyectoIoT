package com.example.proyecto_iot.alumno.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.databinding.FragmentAlumnoHeader4Binding;

public class AlumnoHeader4Fragment extends Fragment {
    private FragmentAlumnoHeader4Binding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAlumnoHeader4Binding.inflate(inflater, container, false);
        binding.buttonBack.setOnClickListener(view -> {
            getActivity().finish();
        });
        return binding.getRoot();
    }
}