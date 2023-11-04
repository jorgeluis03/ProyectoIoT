package com.example.proyecto_iot.alumno.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.proyecto_iot.databinding.FragmentAlumnosEventoMapHowToGetBinding;

public class AlumnosEventoMapHowToGet extends Fragment {
    FragmentAlumnosEventoMapHowToGetBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAlumnosEventoMapHowToGetBinding.inflate(inflater, container, false);
        binding.buttonBack.setOnClickListener(view -> {
            getActivity().finish();
        });
        return binding.getRoot();
    }
}
