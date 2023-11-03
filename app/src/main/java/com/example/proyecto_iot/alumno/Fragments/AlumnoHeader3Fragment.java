package com.example.proyecto_iot.alumno.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.databinding.FragmentAlumnoHeader3Binding;

public class AlumnoHeader3Fragment extends Fragment {
    private FragmentAlumnoHeader3Binding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAlumnoHeader3Binding.inflate(inflater, container, false);
        Bundle bundle = getArguments();
        if (bundle != null){
            String header = bundle.getString("header", "nothing");
            binding.textHeader3.setText(header);
        }

        binding.buttonBack3.setOnClickListener(view -> {
            getActivity().finish();
        });

        return binding.getRoot();
    }
}