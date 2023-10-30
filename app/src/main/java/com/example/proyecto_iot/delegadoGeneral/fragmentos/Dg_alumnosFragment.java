package com.example.proyecto_iot.delegadoGeneral.fragmentos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_iot.R;
import com.example.proyecto_iot.databinding.FragmentDgAlumnosBinding;
import com.example.proyecto_iot.delegadoGeneral.Dg_Activity;
import com.google.android.material.tabs.TabLayout;

public class Dg_alumnosFragment extends Fragment {
    FragmentDgAlumnosBinding binding;
    ViewPager2 viewPager2;
    TabLayout tabLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDgAlumnosBinding.inflate(inflater,container,false);
        // Cambiar el contenido del Toolbar
        ((Dg_Activity) requireActivity()).setToolbarContent("Alumnos");
        tabLayout = binding.tabLayoutAlumnosDg;
        viewPager2 = binding.viewPagerAlumnos;

        viewPager2.setAdapter(new AdaptadorFragment(requireActivity().getSupportFragmentManager(),getLifecycle()));
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return binding.getRoot();
    }

    public class AdaptadorFragment extends FragmentStateAdapter{

        public AdaptadorFragment(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }


        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if(position==0){
                return new Dg_alumnos_registrFragment();
            }
            if (position==1){
                return new Dg_alumnos_pendFragment();
            }
            if (position==2){
                return new Dg_alumnos_baneadosFragment();
            }
            return new Dg_alumnos_registrFragment();
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }


}