package com.example.proyecto_iot.alumno.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.Objetos.Donacion;
import com.example.proyecto_iot.alumno.RecyclerViews.ListaDonacionesAdapter;
import com.example.proyecto_iot.databinding.FragmentAlumnoDonacionesBinding;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.IOException;
import java.util.ArrayList;

public class AlumnoDonacionesFragment extends Fragment {

    FragmentAlumnoDonacionesBinding binding;

    ImageView imageDonacion;

    ArrayList<Donacion> donationList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAlumnoDonacionesBinding.inflate(inflater, container, false);

        binding.buttonDonacionesYape.setOnClickListener(view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
            View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_donacion_cuenta, (LinearLayout) view.findViewById(R.id.dialogCuentaContainer));
            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        });

        binding.buttonDonar.setOnClickListener(view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
            View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_donar, (ConstraintLayout) view.findViewById(R.id.bottomSheetContainer));

            // conf de botones de dialog donar
            bottomSheetView.findViewById(R.id.buttonDialogDonar).setOnClickListener(viewDialog -> {
                Toast.makeText(getContext(), "pipipi", Toast.LENGTH_SHORT).show();
            });

            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();

            bottomSheetView.findViewById(R.id.buttonSubirImagen).setOnClickListener(viewDialog -> {
                // abrir galeria
            });
        });

        donationList.add(new Donacion("Lionel Andrés Messi Cuccittini", "11:30 hrs", "S/2000.00","28 sep. 2023"));
        donationList.add(new Donacion("Luquita Moura", "14:30 hrs", "S/100.00","22 sep. 2023"));
        donationList.add(new Donacion("kunni", "18:00 hrs", "S/100.00","18 ago. 2023"));
        donationList.add(new Donacion("James R.", "15:40 hrs", "S/150.00","10 ago. 2023"));

        ListaDonacionesAdapter adapter = new ListaDonacionesAdapter(getContext(), donationList, donacion -> {
            // Aquí puedes manejar lo que sucede cuando se hace clic en una donación
        });

        binding.rvDonaciones.setAdapter(adapter);
        binding.rvDonaciones.setLayoutManager(new LinearLayoutManager(getContext()));

        return binding.getRoot();
    }
}
