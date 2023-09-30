package com.example.proyecto_iot.alumno.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_iot.alumno.Objetos.Donacion;
import com.example.proyecto_iot.alumno.Objetos.Notificacion;
import com.example.proyecto_iot.alumno.RecyclerViews.ListaDonacionesAdapter;
import com.example.proyecto_iot.databinding.FragmentAlumnoDonacionesBinding;
import java.util.ArrayList;

public class AlumnoDonacionesFragment extends Fragment {

    FragmentAlumnoDonacionesBinding binding;

    ArrayList<Donacion> donationList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAlumnoDonacionesBinding.inflate(inflater, container, false);

        binding.buttonDonar.setOnClickListener(view -> {
            mostrarDialogDonacion();
        });

        donationList.add(new Donacion("Lionel Andrés Messi Cuccittini", "28 sep. 2023","S/2000.00"));
        donationList.add(new Donacion("Luquita Moura", "22 sep. 2023 - 14:30 hrs","S/100.00"));
        donationList.add(new Donacion("kunni", "18 ago. 2023 - 18:00 hrs","S/100.00"));
        donationList.add(new Donacion("James R.", "10 ago. 2023 - 15:40 hrs","S/150.00"));

        ListaDonacionesAdapter adapter = new ListaDonacionesAdapter();
        adapter.setContext(getContext());
        adapter.setDonacionList(donationList);

        binding.rvDonaciones.setAdapter(adapter);
        binding.rvDonaciones.setLayoutManager(new LinearLayoutManager(getContext()));



        return binding.getRoot();
    }

    void mostrarDialogDonacion(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("hola");
        alertDialog.show();
    }
}