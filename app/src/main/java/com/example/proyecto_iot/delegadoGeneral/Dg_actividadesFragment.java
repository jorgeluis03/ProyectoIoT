package com.example.proyecto_iot.delegadoGeneral;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.proyecto_iot.databinding.FragmentDgActividadesBinding;

public class Dg_actividadesFragment extends Fragment {
    FragmentDgActividadesBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentDgActividadesBinding.inflate(inflater,container,false);


        return binding.getRoot();
    }

}