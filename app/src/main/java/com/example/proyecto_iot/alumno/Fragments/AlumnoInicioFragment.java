package com.example.proyecto_iot.alumno.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.alumno.AlumnoInicioViewPagerAdapter;
import com.example.proyecto_iot.databinding.FragmentAlumnoInicioBinding;
import com.google.android.material.tabs.TabLayout;

public class AlumnoInicioFragment extends Fragment {

    FragmentAlumnoInicioBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAlumnoInicioBinding.inflate(inflater, container, false);

        // tabs para todos-apoyando
        TabLayout tabInicio = binding.tabLayout;
        ViewPager2 viewPagerInicio = binding.viewPager;
        viewPagerInicio.setAdapter(new AlumnoInicioViewPagerAdapter(getParentFragment().getActivity()));

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
}