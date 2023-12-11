package com.example.proyecto_iot.alumno.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.proyecto_iot.databinding.FragmentAlumnoHeaderDonacionesBinding;

public class AlumnoHeaderDonaciones extends Fragment {
    FragmentAlumnoHeaderDonacionesBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAlumnoHeaderDonacionesBinding.inflate(inflater, container, false);
        //Bundle bundle = requireArguments();
        //String header = bundle.getString("header", "nothing");
        //binding.textHeader.setText(header);

        binding.buttonBack.setOnClickListener(view -> {
            getActivity().finish();
        });
        return binding.getRoot();
    }
}
