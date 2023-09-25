package com.example.proyecto_iot.alumno;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.databinding.FragmentPackageAlumnoHeaderBinding;

public class PackageAlumnoHeaderFragment extends Fragment {

    FragmentPackageAlumnoHeaderBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPackageAlumnoHeaderBinding.inflate(inflater, container, false);
        binding.profileButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), AlumnoPerfilActivity.class);
            startActivity(intent);
        });
        return binding.getRoot();
    }
}