package com.example.proyecto_iot.alumno.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_iot.alumno.AlumnoInicioViewPagerAdapter;
import com.example.proyecto_iot.alumno.Entities.Alumno;
import com.example.proyecto_iot.databinding.FragmentAlumnoInicioBinding;
import com.example.proyecto_iot.delegadoActividad.DaInicioViewPagerAdapter;
import com.example.proyecto_iot.delegadoGeneral.dto.ActividadesDto;
import com.example.proyecto_iot.delegadoGeneral.entity.Actividades;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AlumnoInicioFragment extends Fragment {

    FragmentAlumnoInicioBinding binding;
    Alumno alumno;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAlumnoInicioBinding.inflate(inflater, container, false);

        Intent intent = getActivity().getIntent();

        // tabs para todos-apoyando
        TabLayout tabInicio = binding.tabLayout;
        ViewPager2 viewPagerInicio = binding.viewPager;
        if (ifDelegadoActividad()){
            tabInicio.addTab(tabInicio.newTab().setText("Mis actividades"),1);
            viewPagerInicio.setAdapter(new DaInicioViewPagerAdapter(getActivity()));
        }
        else {
            viewPagerInicio.setAdapter(new AlumnoInicioViewPagerAdapter(getActivity()));
        }

        tabInicio.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerInicio.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPagerInicio.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabInicio.getTabAt(position).select();
            }
        });

        return binding.getRoot();
    }
    public boolean ifDelegadoActividad(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        boolean valido = false;
        if (currentUser != null) {
            try (FileInputStream fileInputStream = this.getActivity().openFileInput("userData");
                 FileReader fileReader = new FileReader(fileInputStream.getFD());
                 BufferedReader bufferedReader = new BufferedReader(fileReader)){

                String jsonData = bufferedReader.readLine();
                Gson gson = new Gson();
                alumno = gson.fromJson(jsonData, Alumno.class);
                ArrayList<Actividades> actividades = alumno.getActividadesId();
                if (actividades==null){
                    valido = false;
                }else {
                    for (Actividades a: actividades){
                        if (a.getEstado().equals("abierto")){
                            valido = true;
                        }
                    }
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        return valido;
    }
}

