package com.example.proyecto_iot.alumno.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.AlumnoPerfilActivity;
import com.example.proyecto_iot.databinding.FragmentAlumnoHeader2Binding;

public class AlumnoHeader2Fragment extends Fragment {

   FragmentAlumnoHeader2Binding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAlumnoHeader2Binding.inflate(inflater, container, false);

        Bundle bundle = getArguments();
        if (bundle != null){
            String header = bundle.getString("header", "nothing");
            binding.textHeader2.setText(header);
        }

        binding.buttonPerfil.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), AlumnoPerfilActivity.class);
            startActivity(intent);
        });
        return binding.getRoot();
    }
}