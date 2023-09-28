package com.example.proyecto_iot.alumno.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_iot.alumno.AlumnoPerfilActivity;
import com.example.proyecto_iot.databinding.FragmentAlumnoHeader0Binding;
import com.example.proyecto_iot.databinding.FragmentPackageAlumnoHeaderBinding;

public class AlumnoHeader0Fragment extends Fragment {

    FragmentAlumnoHeader0Binding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAlumnoHeader0Binding.inflate(inflater, container, false);
        binding.profileButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), AlumnoPerfilActivity.class);
            startActivity(intent);
        });
        return binding.getRoot();
    }
}