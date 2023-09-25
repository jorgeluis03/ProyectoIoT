package com.example.proyecto_iot.alumno;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;


import com.example.proyecto_iot.R;
import com.example.proyecto_iot.databinding.FragmentPackageAlumnoButtonBinding;

public class PackageAlumnoButtonFragment extends Fragment {

    FragmentPackageAlumnoButtonBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPackageAlumnoButtonBinding.inflate(inflater, container, false);

        binding.buttonNotifications.setOnClickListener(v -> {
            // Cambiar el icono del botón
            binding.buttonNotifications.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_bell_outline, 0, 0, 0);

            // Iniciar la nueva Activity
            Intent intent = new Intent(getActivity(), AlumnoNotificacionesActivity.class);
            startActivity(intent);
        });
        return binding.getRoot();
    }

    private void replaceFragment(Fragment fragment) {
        if (getActivity() != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.bottomFragmentContainer, fragment);
            fragmentTransaction.commit();
        }
    }
}
