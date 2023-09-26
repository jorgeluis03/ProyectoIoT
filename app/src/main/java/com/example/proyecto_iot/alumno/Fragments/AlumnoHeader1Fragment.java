package com.example.proyecto_iot.alumno.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_iot.databinding.FragmentAlumnoHeader1Binding;

public class AlumnoHeader1Fragment extends Fragment {

    FragmentAlumnoHeader1Binding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAlumnoHeader1Binding.inflate(inflater, container, false);
        //Bundle bundle = requireArguments();
        //String header = bundle.getString("header", "nothing");
        //binding.textHeader.setText(header);

        binding.buttonBack.setOnClickListener(view -> {
            getActivity().finish();
        });
        return binding.getRoot();
    }
}