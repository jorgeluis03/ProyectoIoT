package com.example.proyecto_iot.delegadoGeneral;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.databinding.FragmentDgCrearActividadBinding;

public class Dg_CrearActividadFragment extends Fragment {
    FragmentDgCrearActividadBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDgCrearActividadBinding.inflate(inflater,container,false);





        return binding.getRoot();
    }
}