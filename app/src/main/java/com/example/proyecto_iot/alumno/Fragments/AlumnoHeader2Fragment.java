package com.example.proyecto_iot.alumno.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.AlumnoPerfilActivity;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.databinding.FragmentAlumnoHeader2Binding;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

public class AlumnoHeader2Fragment extends Fragment {

    FragmentAlumnoHeader2Binding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAlumnoHeader2Binding.inflate(inflater, container, false);

        Bundle bundle = getArguments();
        if (bundle != null){
            String header = bundle.getString("header", "nothing");
            binding.textHeader2.setText(header);
        }

        binding.buttonPerfil.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), AlumnoPerfilActivity.class);
            startActivity(intent);
        });

        cargarFoto();

        return binding.getRoot();
    }

    private void cargarFoto(){
        try (FileInputStream fileInputStream = getActivity().openFileInput("userData");
             FileReader fileReader = new FileReader(fileInputStream.getFD());
             BufferedReader bufferedReader = new BufferedReader(fileReader)){

            String jsonData = bufferedReader.readLine();
            Gson gson = new Gson();
            Alumno alumno = gson.fromJson(jsonData, Alumno.class);

            RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL); // Almacenamiento en cache
            Glide.with(getActivity())
                    .load(alumno.getFotoUrl())
                    .apply(requestOptions)
                    .into(binding.buttonPerfil);

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}