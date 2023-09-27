package com.example.proyecto_iot;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_iot.alumno.AlumnoInicioViewPagerAdapter;
import com.example.proyecto_iot.databinding.FragmentAlumnoInicioBinding;
import com.google.android.material.tabs.TabLayout;

public class AlumnoInicioFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // tabs para todos-apoyando
        /*
        TabLayout tabInicio = getView().findViewById(R.id.tabLayout);
        ViewPager2 viewPagerInicio = getView().findViewById(R.id.viewPager);
        AlumnoInicioViewPagerAdapter adapter = new AlumnoInicioViewPagerAdapter(getActivity());
        viewPagerInicio.setAdapter(adapter);

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

         */

        return inflater.inflate(R.layout.fragment_alumno_inicio, container, false);
    }
}